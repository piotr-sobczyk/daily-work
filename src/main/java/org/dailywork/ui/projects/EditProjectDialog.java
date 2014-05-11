package org.dailywork.ui.projects;

import net.miginfocom.swing.MigLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;

import org.dailywork.resources.Resources;

class EditProjectDialog extends JDialog {
    private JTextField projectNameTf;
    private JSpinner dailyTimeSpinner;
    private JButton saveButton;
    private JButton cancelButton;
    private JLabel errorMsgLabel;

    private Resources resources;
    private EditProjectDialogCtrl controller;

    public EditProjectDialog(EditProjectDialogCtrl controller, Resources resources) {
        this.controller = controller;
        this.resources = resources;

        initialize();
    }

    private void initialize() {
        setSize(new Dimension(100, 60));
        setResizable(false);
        setModal(true);

        setLayout(new MigLayout("insets 10", "[30]15[125]"));

        projectNameTf = new JTextField();

        SpinnerModel spinnerModel = new SpinnerNumberModel(15, 1, 180, 5);
        dailyTimeSpinner = new JSpinner(spinnerModel);
        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveProject();
            }
        });

        registerKeyActions();

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.cancelRequested();
            }
        });

        add(new JLabel("Project name"), "growx");
        add(projectNameTf, "growx, wrap");

        add(new JLabel("Daily time (minutes)"), "growx");
        add(dailyTimeSpinner, "alignx left, wrap 10");

        errorMsgLabel = new JLabel();
        //Image errorIcon = resources.image("/error.png");
        //errorMsgLabel.setIcon(new ImageIcon(errorIcon));
        errorMsgLabel.setVisible(false);

        add(errorMsgLabel, "span 2, wrap 10");

        add(saveButton, "split 2, span 2, gapright 5, alignx center");
        add(cancelButton);

        pack();
        setLocationRelativeTo(null);
    }


    private void registerKeyActions() {
        getRootPane().setDefaultButton(saveButton);
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

    public void displayErrorMessage(String errorMsg) {
        errorMsgLabel.setText(errorMsg);
        errorMsgLabel.setVisible(true);

        //We know to highlight project name text field because currently it's
        //the only property of project that undergoes validation
        projectNameTf.setBorder(new LineBorder(Color.red));
    }

    public void showAddProjectDialog() {
        setTitle("New project");
        showDialog();
    }

    public void showEditProjectDialog() {
        setTitle("Edit project");
        showDialog();
    }

    private void showDialog() {
        setVisible(true);
    }

    public void hideDialog() {
        setVisible(false);
    }

    private void saveProject() {
        controller.projectSaveRequested(this);
    }

    public void setProjectName(String projectName) {
        projectNameTf.setText(projectName);
    }

    public String getProjectName() {
        return projectNameTf.getText();
    }

    public void setDailyTime(int dailyTime) {
        dailyTimeSpinner.setValue(dailyTime);
    }

    public int getDailyTime() {
        return (int) dailyTimeSpinner.getValue();
    }

}
