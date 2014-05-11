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

package org.dailywork.ui.swing.laf;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicPanelUI;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.dailywork.bus.messages.ui.LookChanged;
import org.dailywork.ui.theme.Look;
import org.dailywork.ui.theme.Theme;
import org.dailywork.ui.util.Canvas;

public class SexyPanelUI extends BasicPanelUI {

    @Inject
    private Look look;
    private List<JComponent> installedComponents = new ArrayList<JComponent>();

    @Inject
    public SexyPanelUI(EventBus bus) {
        bus.register(this);
    }

    @Subscribe
    public void changeLook(LookChanged message) {
        for (JComponent c : installedComponents) {
            c.repaint();
        }
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        c.setLayout(new BorderLayout());
        c.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        installedComponents.add(c);
    }

    @Override
    public void paint(Graphics g, JComponent component) {
        Canvas canvas = new Canvas(component.getSize());
        Theme theme = look.theme();
        theme.paint(canvas);
        canvas.drawBorder(look.colors().shadow());
        g.drawImage(canvas.image(), 0, 0, null);
    }

}
