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

package org.dialywork.ui.theme.colors;

import java.awt.Color;

import org.dialywork.ui.theme.Colors;

public class Red implements Colors {

    private static final Colors INSTANCE = new Red();
    private static final Color BACKGROUND = new Color(89, 18, 21);
    private static final Color BORDER = BACKGROUND.darker();

    public static Colors instance() {
        return INSTANCE;
    }

    @Override
    public Color text() {
        return Color.WHITE;
    }

    @Override
    public Color background() {
        return BACKGROUND;
    }

    @Override
    public Color shadow() {
        return BORDER;
    }

}
