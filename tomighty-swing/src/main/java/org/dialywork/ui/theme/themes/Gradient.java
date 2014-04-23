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

package org.dialywork.ui.theme.themes;

import java.awt.Color;

import org.dialywork.ui.theme.CachedTheme;
import org.dialywork.ui.theme.Colors;
import org.dialywork.ui.theme.Look;
import org.dialywork.ui.util.Canvas;

public class Gradient extends CachedTheme {

    @Override
    public void paint(Canvas canvas, Look look) {
        Colors colors = look.colors();
        Color background = colors.background();
        canvas.paintGradient(background);
    }

}
