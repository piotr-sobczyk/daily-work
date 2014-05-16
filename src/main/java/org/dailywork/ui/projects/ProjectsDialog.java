package org.dailywork.ui.projects;

import net.miginfocom.swing.MigLayout;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
    private JButton editButton;
    private JButton removeButton;
    private JButton okButton;

    private void reloadProjects() {
        projectsListModel.clear();
        for (Project project : projectsManager.getProjects()) {
            projectsListModel.addElement(project);
        }

        addButton.setEnabled(projectsListModel.getSize() < MAX_PROJECTS);
    }

    @PostConstruct
    public void initialize() {
        setSize(new Dimension(280, 200));
        setResizable(false);
        setTitle("Projects");

        addButton = new JButton("Add...");
        editButton = new JButton("Edit...");
        editButton.setEnabled(false);
        removeButton = new JButton("Remove");
        removeButton.setEnabled(false);

        initializeProjectsList();

        addComponents();
        addBehavior();

        setLocationRelativeTo(null);
    }

    private void initializeProjectsList() {
        projectsListModel = new DefaultListModel();
        reloadProjects();
        projectsList = new JList<Project>(projectsListModel);

        Font oldFont = projectsList.getFont();
        projectsList.setFont(new Font(oldFont.getName(), oldFont.getStyle(), 14));
    }

    private void addComponents() {
        setLayout(new MigLayout("fill, insets 10", "[grow]10[30,fill]","[]8[]"));
        JScrollPane projectsListScrollPane = new JScrollPane(projectsList);

        add(projectsListScrollPane, "spany 3, grow");
        add(addButton, "top, wrap push");
        add(editButton, "wrap");
        add(removeButton, "wrap");
        add(new JSeparator(), "span 2, growx, wrap 3");
        okButton = new JButton("Ok");
        add(okButton);
    }

    private boolean confirmProjectDeletion(Project selectedProject) {
        int result = JOptionPane.showConfirmDialog(null, "Are you sure to remove project: `" +
                selectedProject.getName() + "`?", "Project deletion", JOptionPane.WARNING_MESSAGE);
        return result == JOptionPane.OK_OPTION;
    }

    private void addBehavior() {
        projectsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                editButton.setEnabled(true);
                removeButton.setEnabled(true);
            }
        });

        projectsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editSelectedProject();
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

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSelectedProject();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Project selectedProject = projectsList.getSelectedValue();

                if (confirmProjectDeletion(selectedProject)) {
                    projectsManager.removeProject(selectedProject);
                    reloadProjects();
                }
            }
        });

        ActionListener windowClosingListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        };
        okButton.addActionListener(windowClosingListener);
        getRootPane().registerKeyboardAction(windowClosingListener,
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

    }

    private void editSelectedProject() {
        Project project = projectsList.getSelectedValue();

        EditProjectDialogCtrl editProjectDialogCtrl = injector.getInstance(EditProjectDialogCtrl.class);
        editProjectDialogCtrl.setProject(project);
        editProjectDialogCtrl.showDialog();
        reloadProjects();
    }

    public void showDialog() {
        setVisible(true);
    }

}
