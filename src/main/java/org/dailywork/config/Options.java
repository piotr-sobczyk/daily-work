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

package org.dailywork.config;

import java.io.File;

import javax.inject.Inject;

import org.dailywork.bus.Bus;
import org.dailywork.bus.messages.config.TimeOnTrayConfigChanged;
import org.dailywork.bus.messages.ui.LookChanged;
import org.dailywork.ui.theme.Theme;
import org.dailywork.ui.theme.themes.Shiny;

public class Options {

    private static final String TIME_POMODORO = "option.time.bursts";
    private static final String TIME_BREAK = "option.time.break";
    private static final String UI_THEME = "option.ui.theme";
    private static final String UI_AUTOHIDE_WINDOW = "option.ui.window.autohide";
    private static final String UI_DRAGGABLE_WINDOW = "option.ui.window.draggable";
    private static final String UI_SHOW_TIME_ON_TRAY = "option.ui.showTimeOnTray";
    private static final String SOUND_WIND = "option.sound.timer.wind";
    private static final String SOUND_TICTAC = "option.sound.timer.tictac";
    private static final String SOUND_DING = "option.sound.timer.ding";

    @Inject
    private Configuration config;
    @Inject
    private Bus bus;

    private UserInterfaceOptions ui = new UserInterfaceOptions();
    private SoundOptions sound = new SoundOptions();
    private TimeOptions time = new TimeOptions();

    public UserInterfaceOptions ui() {
        return ui;
    }

    public SoundOptions sound() {
        return sound;
    }

    public TimeOptions time() {
        return time;
    }

    public class TimeOptions {

        public int pomodoro() {
            return config.asInt(TIME_POMODORO, 25);
        }

        public void pomodoro(int minutes) {
            config.set(TIME_POMODORO, minutes);
        }

        public int breakTime() {
            return config.asInt(TIME_BREAK, 5);
        }

        public void breakTime(int minutes) {
            config.set(TIME_BREAK, minutes);
        }

    }

    public class UserInterfaceOptions {
        public boolean autoHideWindow() {
            return config.asBoolean(UI_AUTOHIDE_WINDOW, true);
        }

        public void autoHide(boolean autoHide) {
            config.set(UI_AUTOHIDE_WINDOW, autoHide);
        }

        public Theme theme() {
            return config.asObject(UI_THEME, Shiny.class);
        }

        public void theme(Class<? extends Theme> clazz) {
            Class<? extends Theme> currentClass = config.asClass(UI_THEME, Shiny.class);
            if (!clazz.equals(currentClass)) {
                config.set(UI_THEME, clazz);
                bus.publish(new LookChanged());
            }
        }

        public boolean showTimeOnTray() {
            return config.asBoolean(UI_SHOW_TIME_ON_TRAY, true);
        }

        public void showTimeOnTray(boolean show) {
            if (show != showTimeOnTray()) {
                config.set(UI_SHOW_TIME_ON_TRAY, show);
                bus.publish(new TimeOnTrayConfigChanged(show));
            }
        }

        public boolean draggableWindow() {
            return config.asBoolean(UI_DRAGGABLE_WINDOW, false);
        }

        public void draggableWindow(boolean draggable) {
            config.set(UI_DRAGGABLE_WINDOW, draggable);
        }
    }

    public class SoundOptions {
        private SoundConfig wind = new SoundConfig(SOUND_WIND);
        private SoundConfig tictac = new SoundConfig(SOUND_TICTAC);
        private SoundConfig ding = new SoundConfig(SOUND_DING);

        public SoundConfig wind() {
            return wind;
        }

        public SoundConfig tictac() {
            return tictac;
        }

        public SoundConfig ding() {
            return ding;
        }
    }

    public class SoundConfig {
        private String enabledKey;
        private String fileKey;

        public SoundConfig(String key) {
            this.enabledKey = key + ".enabled";
            this.fileKey = key + ".file";
        }

        public boolean enabled() {
            return config.asBoolean(enabledKey, true);
        }

        public void enable(boolean enable) {
            config.set(enabledKey, enable);
        }

        public File file() {
            return config.asFile(fileKey);
        }

        public void file(File file) {
            config.set(fileKey, file);
        }
    }

}
