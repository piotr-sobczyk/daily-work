package org.dailywork.ui.state.work;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;

import javax.inject.Inject;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.dailywork.bus.messages.ui.ChangeUiState;
import org.dailywork.resources.Images;
import org.dailywork.ui.state.UiStateSupport;

public class WorkToBeStarted extends UiStateSupport {

    @Inject
    private Images images;

    @Override
    protected String title() {
        return "Ready to start?";
    }

    @Override
    protected Component createContent() {
        Image image = images.tomato();
        ImageIcon imageIcon = new ImageIcon(image);
        return new JLabel(imageIcon);
    }

    @Override
    protected Action[] primaryActions() {
        return new Action[] {
                new Start()
        };
    }

    @Override
    protected Action[] secondaryActions() {
        return new Action[0];
    }

    @SuppressWarnings("serial")
    private class Start extends AbstractAction {
        public Start() {
            super("Let's go!");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            eventBus.post(new ChangeUiState(Work.class));
        }
    }

}
