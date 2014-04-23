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

package org.dialywork.ui.state.bursts;

import java.awt.Component;
import java.awt.Image;

import javax.inject.Inject;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.dialywork.resources.Images;
import org.dialywork.ui.state.UiStateSupport;

public class BurstFinished extends UiStateSupport {

    @Inject
    private Images images;

    @Override
    protected String title() {
        return messages.get("Project finished");
    }

    @Override
    protected boolean displaysGauge() {
        return false;
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
        };
    }

    @Override
    protected Action[] secondaryActions() {
        return null;
    }

}
