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

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicButtonUI;

import org.dailywork.ui.swing.gauge.GaugeButtonModel;
import org.dailywork.ui.swing.gauge.Light;
import org.dailywork.util.Range;

public class GaugeButtonUI extends BasicButtonUI {

    private static final int LIGHT_SIZE = 5;
    private static final int GAP_BETWEEN_LIGHTS = 3;
    private static final int PADDING = GAP_BETWEEN_LIGHTS;

    private static interface BackgroundColor {
        Color BRIGHT = new Color(230, 230, 230);
        Color DARK = new Color(150, 150, 150);

        Range<Color> UNPRESSED = new Range<Color>(BackgroundColor.BRIGHT, BackgroundColor.DARK);
        Range<Color> PRESSED = UNPRESSED.reverse();
    }

    private static interface Border {
        Color COLOR = new Color(75, 75, 75);
        int RADIUS = 6;
    }

    public static Dimension sizeFor(int numberOfLights) {
        int sumOfGaps = GAP_BETWEEN_LIGHTS * (numberOfLights - 1);
        int width = PADDING * 2 + LIGHT_SIZE * numberOfLights + sumOfGaps;
        int height = PADDING * 2 + LIGHT_SIZE;
        return new Dimension(width, height);
    }

    @Override
    public void installUI(JComponent component) {
        super.installUI(component);

        JButton button = (JButton) component;
        button.setBorderPainted(false);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        AbstractButton button = (AbstractButton) c;
        GaugeButtonModel model = (GaugeButtonModel) button.getModel();
        Dimension size = button.getSize();

        boolean isUnpressed = !model.isPressed() || !model.isArmed();
        if (isUnpressed && model.isRollover())
            paintButtonUnpressed(g, size);

        super.paint(g, button);

        if (model.isRollover())
            paintBorder(g, size);

        paintLights(model.lights(), g);
    }

    @Override
    protected void paintButtonPressed(Graphics g, AbstractButton b) {
        paintButton(g, b.getSize(), BackgroundColor.PRESSED);
    }

    private void paintButtonUnpressed(Graphics g, Dimension size) {
        paintButton(g, size, BackgroundColor.UNPRESSED);
    }

    private void paintButton(Graphics g, Dimension size, Range<Color> grandientColors) {
        Graphics2D g2d = createGraphics(g);
        try {
            g2d.setPaint(new GradientPaint(0, 0, grandientColors.start(), 0, size.height, grandientColors.end()));
            g2d.fillRoundRect(0, 0, size.width - 1, size.height - 1, Border.RADIUS, Border.RADIUS);
        } finally {
            g2d.dispose();
        }
    }

    private void paintBorder(Graphics g, Dimension size) {
        Graphics2D g2d = createGraphics(g);
        try {
            g2d.setStroke(new BasicStroke(1f));
            g2d.setColor(Border.COLOR);
            g2d.drawRoundRect(0, 0, size.width - 1, size.height - 1, Border.RADIUS, Border.RADIUS);
        } finally {
            g2d.dispose();
        }
    }

    private Graphics2D createGraphics(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        return g2d;
    }

    private void paintLights(List<Light> lights, Graphics g) {
        LightPainter painter = new LightPainter(LIGHT_SIZE, GAP_BETWEEN_LIGHTS);
        Graphics2D g2d = createGraphics(g);
        try {
            for (Light light : lights)
                painter.paint(light, g2d);
        } finally {
            g2d.dispose();
        }
    }

}
