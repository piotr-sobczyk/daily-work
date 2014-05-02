package org.dailywork.ui.projects;

import net.miginfocom.swing.MigLayout;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import org.dailywork.projects.Project;
import org.dailywork.projects.ProjectsManager;

public class EditProjectDialog extends JDialog{

    @Inject
    private ProjectsManager projectsManager;

    private JTextField projectNameTf;
    private JSpinner dailyTimeSpinner;
    private JButton saveButton;
    private JButton cancelButton;

    private Project project;

    public void setProject(Project project) {
        this.project = project;
        projectNameTf.setText(project.getName());
        dailyTimeSpinner.setValue(project.getDailyTimeMins());
    }

    @PostConstruct
    public void initialize() {
        setTitle("Edit project");
        setSize(new Dimension(100,60));
        setResizable(false);
        setModal(true);

        setLayout(new MigLayout("insets 10","[30]15[125]"));

        projectNameTf = new JTextField();

        SpinnerModel spinnerModel = new SpinnerNumberModel(15,1,180,5);
        dailyTimeSpinner = new JSpinner(spinnerModel);
        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveProject();
                setVisible(false);
            }
        });

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        add(new JLabel("Project name"), "growx");
        add(projectNameTf,"growx, wrap");

        add(new JLabel("Daily time"),"growx");
        add(dailyTimeSpinner,"alignx left, wrap 20");

        add(saveButton,"split 2, span 2, gapright 5, alignx center");
        add(cancelButton);

        pack();
        setLocationRelativeTo(null);
    }

    public void showDialog() {
        setVisible(true);
    }

    private void saveProject(){
        String projectName = projectNameTf.getText();
        int dailyTime = (int) dailyTimeSpinner.getValue();

        if(project == null) {
            projectsManager.addProject(projectName,dailyTime);
        }else{
            project.setName(projectName);
            project.setDailyTimeMins(dailyTime);
            projectsManager.updateProjectMetadata(project);
        }

    }

}
