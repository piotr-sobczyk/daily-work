package org.dailywork.ui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Inject;

import org.dailywork.ui.about.AboutDialog;

import com.google.inject.Injector;

public class ShowAboutWindow implements ActionListener {

    @Inject
    private Injector injector;

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        AboutDialog about = injector.getInstance(AboutDialog.class);
        about.showDialog();
    }

}
