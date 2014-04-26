package org.dailywork.ui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Inject;

import org.dailywork.ui.projects.ProjectsDialog;

import com.google.inject.Injector;

public class ShowProjects implements ActionListener {

    @Inject
    private Injector injector;

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        ProjectsDialog dialog = injector.getInstance(ProjectsDialog.class);
        dialog.showDialog();
    }

}
