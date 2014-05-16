package org.dailywork.ui.state.work;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.dailywork.bus.messages.ui.ChangeUiState;
import org.dailywork.ui.state.UiStateSupport;

public class WorkToBeStarted extends UiStateSupport {

    @Override
    protected String title() {
        return "Ready to start?";
    }

    @Override
    protected Component createContent() {
        return null;
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
