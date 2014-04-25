package org.dailywork.ui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Inject;

import org.dailywork.ui.options.OptionsDialog;

import com.google.inject.Injector;

public class ShowOptions implements ActionListener {

    @Inject
    private Injector injector;

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        OptionsDialog dialog = injector.getInstance(OptionsDialog.class);
        dialog.showDialog();
    }

}
