package org.dailywork.ui;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.ButtonGroup;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;

import org.dailywork.bus.Bus;
import org.dailywork.bus.Subscriber;
import org.dailywork.i18n.Messages;
import org.dailywork.projects.Project;
import org.dailywork.projects.ProjectsManager;
import org.dailywork.ui.menu.Exit;
import org.dailywork.ui.menu.ShowOptions;

import com.google.inject.Injector;

public class PopupMenu {

    @Inject
    private Injector injector;
    @Inject
    private Messages messages;
    @Inject
    private Bus bus;
    @Inject
    private ProjectsManager projectsManager;


    private JPopupMenu popupMenu;

    private Map<Project, JMenuItem> projectMenuItems = new HashMap<>();

    @PostConstruct
    public void initialize() {
        popupMenu = createComponent();
        bus.subscribe(new ProjectListener(), Project.Updated.class);
    }

    private class ProjectListener implements Subscriber<Project.Updated> {
        @Override
        public void receive(Project.Updated update) {
            updateMenuItemForProject(update.getModel());
        }
    }

    private void updateMenuItemForProject(final Project project) {
        String prefix = "";
        String suffix = "";
        if (project.isFinished()) {
            prefix = "<html><strike>";
            suffix = "</strike></html>";
        }

        final JMenuItem menuItem = projectMenuItems.get(project);
        final String text = prefix + projectMenuItemLabel(project) + suffix;

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                menuItem.setText(text);
                menuItem.setEnabled(!project.isFinished());
            }
        });
    }

    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }

    private JPopupMenu createComponent() {
        JPopupMenu menu = new JPopupMenu();

        ButtonGroup projectsGroup = new ButtonGroup();
        for (Project project : projectsManager.getProjects()) {
            JMenuItem item = projectMenuItem(projectsGroup, project);
            projectMenuItems.put(project, item);
            menu.add(item);
        }
        menu.addSeparator();

        menu.add(menuItem("Options", injector.getInstance(ShowOptions.class)));
        //TODO: prepare new About page
        //menu.add(menuItem("About", injector.getInstance(ShowAboutWindow.class)));

        menu.addSeparator();
        menu.add(menuItem("Close", new Exit()));

        return menu;
    }

    private String projectMenuItemLabel(Project project) {
        return String.format("(%s/%s) %s", project.getTime(), project.getTotalDailyTime(), project.getDisplayName());
    }

    private JMenuItem projectMenuItem(final ButtonGroup projectsGroup, final Project project) {
        final JRadioButtonMenuItem item = new JRadioButtonMenuItem(projectMenuItemLabel(project));
        projectsGroup.add(item);

        item.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    projectsManager.changeProject(project);
                }
            }
        });

        return item;
    }

    private JMenuItem menuItem(String text, ActionListener listener) {
        JMenuItem item = new JMenuItem(messages.get(text));
        item.addActionListener(listener);
        return item;
    }

}
