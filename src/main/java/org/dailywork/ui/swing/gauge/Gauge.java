/*
 * Copyright (c) 2010-2012 Célio Cidral Junior.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package org.dailywork.ui.swing.gauge;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Inject;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.dailywork.bus.messages.ui.UiStateChanged;
import org.dailywork.ui.UiState;
import org.dailywork.ui.state.work.Work;
import org.dailywork.ui.state.work.WorkFinished;
import org.dailywork.ui.swing.laf.GaugeButtonUI;
import org.dailywork.ui.util.Geometry;

@SuppressWarnings("serial")
public class Gauge extends JPanel{

    private static final int NUMBER_OF_LIGHTS = 4;
    private static final Dimension BUTTON_SIZE = GaugeButtonUI.sizeFor(NUMBER_OF_LIGHTS);
    private static final Dimension GAUGE_SIZE = Geometry.increase(4, BUTTON_SIZE);

    private final GaugeButtonModel buttonModel = new GaugeButtonModel(NUMBER_OF_LIGHTS);

    @Inject
    public Gauge(EventBus eventBus) {
        configureAppearance();

        add(createButton());

        eventBus.register(this);
    }

    private void configureAppearance() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        setOpaque(false);
        setPreferredSize(GAUGE_SIZE);
        setSize(GAUGE_SIZE);
    }

    private JButton createButton() {
        JButton button = new JButton();
        button.setModel(buttonModel);
        button.setUI(new GaugeButtonUI());
        button.setOpaque(false);
        button.setPreferredSize(BUTTON_SIZE);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonModel.turnNextLightOn();
                turnAllLightsOffIfAllAreOn();
            }
        });
        return button;
    }

    @Subscribe
    public void changeUiState(UiStateChanged message) {
        UiState uiState = message.uiState();

        if (uiState instanceof WorkFinished) {
            buttonModel.turnNextLightOn();
        } else if (uiState instanceof Work) {
            turnAllLightsOffIfAllAreOn();
        }
    }

    private void turnAllLightsOffIfAllAreOn() {
        if (buttonModel.areAllLightsOn())
            buttonModel.turnAllLightsOff();
    }

}
