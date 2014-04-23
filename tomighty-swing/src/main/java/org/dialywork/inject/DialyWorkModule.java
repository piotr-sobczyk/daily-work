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

package org.dialywork.inject;

import static com.google.inject.Scopes.SINGLETON;

import org.dialywork.bus.Bus;
import org.dialywork.bus.DefaultBus;
import org.dialywork.config.Configuration;
import org.dialywork.config.Options;
import org.dialywork.config.Projects;
import org.dialywork.i18n.Messages;
import org.dialywork.projects.ProjectsManager;
import org.dialywork.resources.cache.Caches;
import org.dialywork.sound.SoundPlayer;
import org.dialywork.sound.Sounds;
import org.dialywork.time.Timer;
import org.dialywork.ui.PopupMenu;
import org.dialywork.ui.Window;
import org.dialywork.ui.swing.gauge.Gauge;
import org.dialywork.ui.theme.Look;
import org.dialywork.ui.tray.AwtTray;
import org.dialywork.ui.tray.Tray;
import org.dialywork.ui.tray.TrayManager;

import com.google.inject.AbstractModule;

public class DialyWorkModule extends AbstractModule {

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
        bind(Projects.class).in(SINGLETON);
        bind(Messages.class).in(SINGLETON);
        bind(Sounds.class).in(SINGLETON);
        bind(SoundPlayer.class).in(SINGLETON);
        bind(Caches.class).in(SINGLETON);
        bind(Look.class).in(SINGLETON);
        bind(PopupMenu.class).in(SINGLETON);

        bind(ProjectsManager.class).asEagerSingleton();
    }

}
