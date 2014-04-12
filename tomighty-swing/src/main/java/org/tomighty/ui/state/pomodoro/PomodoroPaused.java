package org.tomighty.ui.state.pomodoro;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.inject.Inject;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;

import org.tomighty.bus.messages.ui.ChangeUiState;
import org.tomighty.time.Timer;
import org.tomighty.ui.state.UiStateSupport;

public class PomodoroPaused extends UiStateSupport {

    @Inject
    private Timer timer;

    private JLabel remainingTime;

    @Override
    protected String title() {
        return "Pomodoro (paused)";
    }

    @Override
    protected Component createContent() {
        remainingTime = labelFactory.big();
        return remainingTime;
    }

    @Override
    public void afterRendering() {
        remainingTime.setText(timer.getTime().toString());
    }

    @Override
    protected Action[] primaryActions() {
        return new Action[] {
                new Resume()
        };
    }

    @Override
    protected Action[] secondaryActions() {
        return new Action[0];
    }

    @SuppressWarnings("serial")
    private class Resume extends AbstractAction {
        public Resume() {
            super("Resume");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            bus.publish(new ChangeUiState(Pomodoro.class));
        }
    }

}
