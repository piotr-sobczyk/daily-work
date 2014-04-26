package org.dailywork.ui.projects;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.JDialog;

import org.dailywork.i18n.Messages;

public class ProjectsDialog extends JDialog {

    @Inject
    private Messages messages;

    @PostConstruct
    public void initialize() {
        setTitle(messages.get("Options"));
        pack();
        setLocationRelativeTo(null);
    }

    public void showDialog() {
        setVisible(true);
    }

}
