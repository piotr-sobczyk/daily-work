package org.tomighty.ui;

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

import org.tomighty.bus.Bus;
import org.tomighty.bus.Subscriber;
import org.tomighty.bus.messages.projects.ProjectTimeChanged;
import org.tomighty.config.Projects;
import org.tomighty.i18n.Messages;
import org.tomighty.projects.Project;
import org.tomighty.projects.ProjectsManager;
import org.tomighty.ui.menu.Exit;
import org.tomighty.ui.menu.ShowOptions;

import com.google.inject.Injector;

public class PopupMenu {

    @Inject
    private Injector injector;
    @Inject
    private Messages messages;
    @Inject
    private Projects projects;
    @Inject
    private Bus bus;
    @Inject
    private ProjectsManager projectsManager;


    private JPopupMenu popupMenu;

    private Map<Project, JMenuItem> projectMenuItems = new HashMap<>();

    @PostConstruct
    public void initialize() {
        popupMenu = createComponent();
        bus.subscribe(new ProjectTimeChangedHandler(), ProjectTimeChanged.class);
    }

    private class ProjectTimeChangedHandler implements Subscriber<ProjectTimeChanged> {
        @Override
        public void receive(ProjectTimeChanged message) {
            updateProjectTimes();
        }
    }

    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }

    public void updateProjectTimes() {
        for (final Project project : projectMenuItems.keySet()) {
            if (project.isFinished()) {
                continue;
            }

            final JMenuItem menuItem = projectMenuItems.get(project);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    menuItem.setText(projectMenuItemLabel(project));
                    menuItem.repaint();
                }
            });
        }
    }

    public void markProjectAsFinished(Project project) {
        JMenuItem menuItem = projectMenuItems.get(project);
        String text = "<html><strike>" + projectMenuItemLabel(project) + "</strike></html>";
        menuItem.setEnabled(false);
        menuItem.setText(text);
    }

    private JPopupMenu createComponent() {
        JPopupMenu menu = new JPopupMenu();

        ButtonGroup projectsGroup = new ButtonGroup();
        for (Project project : projects.getProjects()) {
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
