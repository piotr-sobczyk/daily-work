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

package org.dailywork.ui.options;

import java.awt.Component;
import java.awt.GridLayout;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.dailywork.config.Options;
import org.dailywork.i18n.Messages;
import org.dailywork.ui.util.FieldFactory;

@SuppressWarnings("serial")
public class Times extends OptionPanel implements OptionGroup {

    @Inject
    private Options options;
    @Inject
    private Messages messages;

    private JFormattedTextField pomodoro;
    private JFormattedTextField breakTime;

    @PostConstruct
    public void initialize() {
        pomodoro = addField("Burst");
        breakTime = addField("Break");
    }

    @Override
    public String name() {
        return messages.get("Times");
    }

    @Override
    public Component asComponent() {
        return this;
    }

    @Override
    public void readConfiguration() {
        pomodoro.setValue(options.time().pomodoro());
        breakTime.setValue(options.time().breakTime());
    }

    @Override
    public void saveConfiguration() {
        options.time().pomodoro(valueOf(pomodoro));
        options.time().breakTime(valueOf(breakTime));
    }

    private int valueOf(JFormattedTextField field) {
        String text = field.getText();
        return Integer.parseInt(text);
    }

    private JFormattedTextField addField(String name) {
        JFormattedTextField field = FieldFactory.createIntegerField(1, 2);
        JLabel label = new JLabel(messages.get(name), JLabel.TRAILING);
        label.setLabelFor(field);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3, 5, 5));
        panel.add(label);
        panel.add(field);
        panel.add(new JLabel(messages.get("minutes")));
        add(panel);
        return field;
    }

}
