package org.dialywork.ui.state.bursts;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.inject.Inject;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;

import org.dialywork.bus.messages.ui.ChangeUiState;
import org.dialywork.time.Timer;
import org.dialywork.ui.state.UiStateSupport;

public class BurstPaused extends UiStateSupport {

    @Inject
    private Timer timer;

    private JLabel remainingTime;

    @Override
    protected String title() {
        return "Burst (paused)";
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
            bus.publish(new ChangeUiState(Burst.class));
        }
    }

}
