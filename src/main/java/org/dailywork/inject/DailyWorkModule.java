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

package org.dailywork.inject;

import static com.google.inject.Scopes.SINGLETON;

import org.dailywork.bus.Bus;
import org.dailywork.bus.DefaultBus;
import org.dailywork.config.Configuration;
import org.dailywork.config.Options;
import org.dailywork.config.ProjectsStore;
import org.dailywork.i18n.Messages;
import org.dailywork.projects.ProjectsManager;
import org.dailywork.resources.cache.Caches;
import org.dailywork.sound.SoundPlayer;
import org.dailywork.sound.Sounds;
import org.dailywork.time.Timer;
import org.dailywork.ui.PopupMenu;
import org.dailywork.ui.Window;
import org.dailywork.ui.swing.gauge.Gauge;
import org.dailywork.ui.theme.Look;
import org.dailywork.ui.tray.AwtTray;
import org.dailywork.ui.tray.Tray;
import org.dailywork.ui.tray.TrayManager;

import com.google.inject.AbstractModule;

public class DailyWorkModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Bus.class).to(DefaultBus.class).in(SINGLETON);

        bind(Timer.class).in(SINGLETON);
        bind(Tray.class).to(AwtTray.class).in(SINGLETON);
        bind(TrayManager.class).in(SINGLETON);
        bind(Window.class).in(SINGLETON);
        bind(Gauge.class).in(SINGLETON);
        bind(Options.class).in(SINGLETON);
        bind(Configuration.class).in(SINGLETON);
        bind(ProjectsStore.class).in(SINGLETON);
        bind(Messages.class).in(SINGLETON);
        bind(Sounds.class).in(SINGLETON);
        bind(SoundPlayer.class).in(SINGLETON);
        bind(Caches.class).in(SINGLETON);
        bind(Look.class).in(SINGLETON);
        bind(PopupMenu.class).in(SINGLETON);

        bind(ProjectsManager.class).asEagerSingleton();
    }

}
