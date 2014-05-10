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
import org.dailywork.bus.messages.general.StateReset;
import org.dailywork.i18n.Messages;
import org.dailywork.projects.Project;
import org.dailywork.projects.ProjectProgress;
import org.dailywork.projects.ProjectsManager;
import org.dailywork.time.Time;
import org.dailywork.ui.menu.Exit;
import org.dailywork.ui.menu.ShowOptions;
import org.dailywork.ui.menu.ShowProjects;

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

    private JPopupMenu popupMenu = new JPopupMenu();
    private ButtonGroup projectsGroup;

    private Map<Project, JMenuItem> projectMenuItems = new HashMap<>();

    @PostConstruct
    public void initialize() {
        reloadMenu();

        bus.subscribe(new ProjectListener(), ProjectProgress.Updated.class);
        bus.subscribe(new ResetState(), StateReset.class);
    }

    private class ProjectListener implements Subscriber<ProjectProgress.Updated> {
        @Override
        public void receive(ProjectProgress.Updated update) {
            updateProjectProgress(update.getModel());
        }
    }

    private void updateProjectProgress(final ProjectProgress projectProgress) {
        Project project = projectProgress.getProject();
        final JMenuItem menuItem = projectMenuItems.get(project);
        final String text = projectMenuItemLabel(project, projectProgress);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                menuItem.setText(text);
                menuItem.setEnabled(!projectProgress.isFinished());
            }
        });
    }

    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }

    public void reloadMenu() {
        popupMenu.removeAll();
        projectMenuItems.clear();

        projectsGroup = new ButtonGroup();
        for (Project project : projectsManager.getProjects()) {
            JMenuItem item = projectMenuItem(projectsGroup, project);
            projectMenuItems.put(project, item);
            popupMenu.add(item);
        }
        popupMenu.addSeparator();

        popupMenu.add(menuItem("Projects...", injector.getInstance(ShowProjects.class)));
        popupMenu.add(menuItem("Options...", injector.getInstance(ShowOptions.class)));
        //TODO: prepare new About page
        //menu.add(menuItem("About", injector.getInstance(ShowAboutWindow.class)));

        popupMenu.addSeparator();
        popupMenu.add(menuItem("Close", new Exit()));
    }

    private String projectMenuItemLabel(Project project, ProjectProgress projectProgress) {
        String prefix = "";
        String suffix = "";

        if (projectProgress.isFinished()) {
            prefix = "<html><strike>";
            suffix = "</strike></html>";
        }

        Time currentTime = projectProgress.getTime();
        Time dailyTime = new Time(project.getDailyTimeMins());
        String displayName = projectProgress.getProject().getDisplayName();

        return prefix + String.format("(%s/%s) %s", currentTime, dailyTime, displayName) + suffix;
    }

    private JMenuItem projectMenuItem(final ButtonGroup projectsGroup, final Project project) {
        ProjectProgress projectProgress = projectsManager.getProgressForProject(project);
        String itemLabel = projectMenuItemLabel(project, projectProgress);
        final JRadioButtonMenuItem item = new JRadioButtonMenuItem(itemLabel);
        projectsGroup.add(item);
        item.setEnabled(!projectProgress.isFinished());

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

    private class ResetState implements Subscriber<StateReset> {

        @Override
        public void receive(StateReset message) {
            projectsGroup.clearSelection();
        }
    }

}
