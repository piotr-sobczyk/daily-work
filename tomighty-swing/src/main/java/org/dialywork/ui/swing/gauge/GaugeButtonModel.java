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

package org.dialywork.ui.swing.gauge;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultButtonModel;

public class GaugeButtonModel extends DefaultButtonModel {

    private final List<Light> lights;

    public GaugeButtonModel(int numberOfLights) {
        lights = new ArrayList<Light>();
        for (int number = 0; number < numberOfLights; number++)
            lights.add(new Light(number));
    }

    public List<Light> lights() {
        return lights;
    }

    public void turnNextLightOn() {
        Light light = findFirstLightOff();
        if (light != null)
            light.turnOn();
    }

    public boolean areAllLightsOn() {
        return numberOfLightsOn() == lights.size();
    }

    public void turnAllLightsOff() {
        for (Light light : lights)
            light.turnOff();
    }

    private Light findFirstLightOff() {
        for (Light light : lights)
            if (!light.isOn())
                return light;
        return null;
    }

    private int numberOfLightsOn() {
        int count = 0;
        for (Light light : lights)
            if (light.isOn())
                count++;
        return count;
    }

}
