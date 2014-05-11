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

package org.dailywork.ui.tray;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.dailywork.bus.messages.config.TimeOnTrayConfigChanged;
import org.dailywork.bus.messages.ui.TrayClick;
import org.dailywork.bus.timer.TimerStopped;
import org.dailywork.bus.timer.TimerTick;
import org.dailywork.config.Configuration;
import org.dailywork.config.Options;
import org.dailywork.i18n.Messages;
import org.dailywork.resources.TrayIcons;
import org.dailywork.time.Time;

public class TrayManager implements Runnable {

    @Inject
    private Configuration config;
    @Inject
    private Options options;
    @Inject
    private EventBus eventBus;
    @Inject
    private Messages messages;
    @Inject
    private TrayIcons icons;
    private TrayIcon trayIcon;

    @PostConstruct
    public void initialize() {
        eventBus.register(this);

        trayIcon = new TrayIcon(icons.tomato());
        trayIcon.addMouseListener(new TrayListener());
        trayIcon.setImageAutoSize(true);
    }

    @Override
    public void run() {
        SystemTray tray = SystemTray.getSystemTray();
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        boolean firstRun = config.asBoolean("firstRun", true);
        if (firstRun) {
            showWelcomeMessage(trayIcon);
            config.set("firstRun", false);
        }
    }

    private void showWelcomeMessage(TrayIcon icon) {
        String caption = messages.get("First time using Daily Work?");
        String message = messages.get("Click on the tomato icon to start using it");
        icon.displayMessage(caption, message, MessageType.INFO);
    }

    private void showTomatoIcon() {
        Image image = icons.tomato();
        trayIcon.setImage(image);
    }

    private class TrayListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            eventBus.post(new TrayClick(mouseEvent.getLocationOnScreen()));
        }
    }

        @Subscribe
        public void updateTimeOnTray(TimerTick tick) {
            if (options.ui().showTimeOnTray()) {
                Time time = tick.getTime();
                Image image = icons.time(time);
                trayIcon.setImage(image);
            }
        }

        @Subscribe
        public void removeTimeFromTray(TimeOnTrayConfigChanged configuration) {
            if (!configuration.shouldShowTimeOnTray()) {
                showTomatoIcon();
            }
        }

        @Subscribe
        public void showTomatoIconWhenTimerStops(TimerStopped end) {
            showTomatoIcon();
        }

}
