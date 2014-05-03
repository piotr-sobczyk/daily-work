package org.dailywork.ui.projects;

import net.miginfocom.swing.MigLayout;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.dailywork.i18n.Messages;
import org.dailywork.projects.Project;
import org.dailywork.projects.ProjectsManager;
import org.dailywork.resources.Resources;

import com.google.inject.Injector;

public class ProjectsDialog extends JDialog {

    public static final int MAX_PROJECTS = 5;

    @Inject
    private Messages messages;
    @Inject
    private Injector injector;
    @Inject
    private ProjectsManager projectsManager;
    @Inject
    private Resources resources;

    private DefaultListModel projectsListModel;
    private JList<Project> projectsList;

    private JButton addButton;
    private JButton removeButton;

    private void reloadProjects(){
        projectsListModel.clear();
        for(Project project: projectsManager.getProjects()){
            projectsListModel.addElement(project);
        }

        addButton.setEnabled(projectsListModel.getSize() < MAX_PROJECTS);
        removeButton.setEnabled(projectsListModel.getSize() > 0);
    }

    @PostConstruct
    public void initialize() {
        setSize(new Dimension(200,150));
        setResizable(false);
        setTitle("Projects");

        addButton = button("/add.png");
        removeButton = button("/remove.png");

        initializeProjectsList();

        addComponents();
        addBehavior();

        setLocationRelativeTo(null);
    }

    private void initializeProjectsList(){
        projectsListModel = new DefaultListModel();
        reloadProjects();
        projectsList = new JList<Project>(projectsListModel);
    }

    private void addComponents(){
        setLayout(new MigLayout("insets 10", "[grow]", "[][grow,fill]"));

        add(addButton, "split 2");
        add(removeButton, "wrap");
        add(projectsList,"grow");
    }

    private boolean confirmProjectDeletion(Project selectedProject){
        int result = JOptionPane.showConfirmDialog(null,"Are you sure to remove project: `" +
                selectedProject.getName() + "`?","Project deletion",JOptionPane.WARNING_MESSAGE);
        return result == JOptionPane.OK_OPTION;
    }

    private void addBehavior(){
        projectsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Project project = projectsList.getSelectedValue();

                    EditProjectDialogCtrl editProjectDialogCtrl = injector.getInstance(EditProjectDialogCtrl.class);
                    editProjectDialogCtrl.setProject(project);
                    editProjectDialogCtrl.showDialog();
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditProjectDialogCtrl editProjectDialogCtrl = injector.getInstance(EditProjectDialogCtrl.class);
                editProjectDialogCtrl.showDialog();
                reloadProjects();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Project selectedProject = projectsList.getSelectedValue();

                if(confirmProjectDeletion(selectedProject)){
                    projectsManager.removeProject(selectedProject);
                    reloadProjects();
                }
            }
        });

        ActionListener escListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        };
        getRootPane().registerKeyboardAction(escListener,
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

    }

    private JButton button(String iconName){
        Image img = resources.image(iconName);
        final JButton button = new JButton(new ImageIcon(img));

        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBorderPainted(true);
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBorderPainted(false);
            }
        });

        return button;
    }

    public void showDialog() {
        setVisible(true);
    }

}
