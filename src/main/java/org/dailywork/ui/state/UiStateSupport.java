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

package org.dailywork.ui.state;

import net.miginfocom.swing.MigLayout;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.inject.Inject;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.dailywork.i18n.Messages;
import org.dailywork.ui.UiState;
import org.dailywork.ui.swing.laf.SexyArrowButtonUI;
import org.dailywork.ui.swing.laf.SexyButtonUI;
import org.dailywork.ui.swing.laf.SexyLabel;
import org.dailywork.ui.theme.Colors;

import com.google.common.eventbus.EventBus;
import com.google.inject.Injector;

public abstract class UiStateSupport implements UiState {

    @Inject
    private Injector injector;
    @Inject
    private SexyArrowButtonUI arrowButtonUI;

    @Inject
    protected SexyLabel labelFactory;
    @Inject
    protected EventBus eventBus;
    @Inject
    protected Messages messages;

    protected abstract String title();

    protected abstract Component createContent();

    protected abstract Action[] primaryActions();

    protected abstract Action[] secondaryActions();

    @Override
    public final Component render() throws Exception {
        return createComponent();
    }

    @Override
    public Colors colors() {
        return null;
    }

    @Override
    public void afterRendering() {
    }

    @Override
    public void beforeDetaching() {
    }

    private JPanel createComponent() {
        JPanel component = createPanel(new MigLayout());
        component.add(createHeader(), "dock north");

        Component content = createContent();
        if (content == null) {
            content = createPanel(new MigLayout());
        }
        component.add(content, "dock center");

        component.add(createButtons(), "dock south");
        return component;
    }

    private JPanel createHeader() {
        JPanel panel = createPanel(new MigLayout("insets 3"));
        String title = title();
        if (title != null) {
            panel.add(labelFactory.small(title), "dock center");
        }
        return panel;
    }

    private Component createButtons() {
        Action[] actions = primaryActions();
        JPanel buttons = createPanel(new GridLayout(1, actions.length, 3, 0));
        for (Action action : actions) {
            injector.injectMembers(action);
            JButton button = new JButton(action);
            button.setOpaque(false);
            button.setUI(SexyButtonUI.INSTANCE);
            buttons.add(button);
        }
        return buttons;
    }

    private static JPanel createPanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setOpaque(false);
        return panel;
    }

}
