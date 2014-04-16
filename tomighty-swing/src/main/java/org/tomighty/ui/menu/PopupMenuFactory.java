package org.tomighty.ui.menu;

import java.awt.event.ActionListener;

import javax.inject.Inject;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

import org.tomighty.config.Projects;
import org.tomighty.i18n.Messages;

import com.google.inject.Injector;

public class PopupMenuFactory {

    @Inject
    private Injector injector;
    @Inject
    private Messages messages;
    @Inject
    private Projects projects;

    public JPopupMenu create(Action[] items) {
        JPopupMenu menu = new JPopupMenu();

        ButtonGroup projectsGroup = new ButtonGroup();
        for (String project : projects.getProjects()) {
            JMenuItem item = new JRadioButtonMenuItem(project);
            projectsGroup.add(item);
            menu.add(item);
        }
        menu.addSeparator();

        if (items != null && items.length > 0) {
            for (Action action : items) {
                injector.injectMembers(action);
                JMenuItem item = new JMenuItem(action);
                menu.add(item);
            }

            menu.addSeparator();
        }

        menu.add(menuItem("Options", injector.getInstance(ShowOptions.class)));
        //TODO: prepare new About page
        //menu.add(menuItem("About", injector.getInstance(ShowAboutWindow.class)));
        menu.addSeparator();
        menu.add(menuItem("Close", new Exit()));

        return menu;
    }

    private JMenuItem menuItem(String text, ActionListener listener) {
        JMenuItem item = new JMenuItem(messages.get(text));
        item.addActionListener(listener);
        return item;
    }

}
