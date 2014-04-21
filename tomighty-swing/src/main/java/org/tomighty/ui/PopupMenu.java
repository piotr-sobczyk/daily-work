package org.tomighty.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import org.tomighty.bus.messages.projects.ProjectChanged;
import org.tomighty.bus.messages.projects.ProjectTimeChanged;
import org.tomighty.bus.messages.ui.ChangeUiState;
import org.tomighty.config.Projects;
import org.tomighty.i18n.Messages;
import org.tomighty.projects.Project;
import org.tomighty.projects.ProjectsManager;
import org.tomighty.ui.menu.Exit;
import org.tomighty.ui.menu.ShowOptions;
import org.tomighty.ui.state.InitialState;

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

    private JPopupMenu popupMenu;

    private Map<Project, JMenuItem> projectMenuItems = new HashMap<>();

    @PostConstruct
    public void initialize() {
        popupMenu = create();
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

    private String projectMenuItemLabel(Project project) {
        String label = ProjectsManager.normalizeProjectName(project.getName());
        return String.format("(%s/%s) %s", project.getTime(), project.getTotalDialyTime(), label);
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

    private JPopupMenu create() {
        JPopupMenu menu = new JPopupMenu();

        ButtonGroup projectsGroup = new ButtonGroup();
        for (Project project : projects.getProjects()) {
            JMenuItem item = createProjectMenuItem(projectsGroup, project);
            projectMenuItems.put(project, item);
            menu.add(item);
        }
        menu.addSeparator();

        menu.add(menuItem("Options", injector.getInstance(ShowOptions.class)));
        //TODO: prepare new About page
        //menu.add(menuItem("About", injector.getInstance(ShowAboutWindow.class)));

        //TODO: Development productivity hack, remove on production
        menu.addSeparator();
        menu.add(menuItem("Reload", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                injector.getInstance(Window.class).initialize();
                injector.getInstance(Bus.class).publish(new ChangeUiState(InitialState.class));
            }
        }));

        menu.addSeparator();
        menu.add(menuItem("Close", new Exit()));

        return menu;
    }

    private JMenuItem createProjectMenuItem(ButtonGroup projectsGroup, Project project) {
        JMenuItem item = new JRadioButtonMenuItem(projectMenuItemLabel(project));
        projectsGroup.add(item);
        item.addActionListener(new SelectProject(project));
        return item;
    }

    private JMenuItem menuItem(String text, ActionListener listener) {
        JMenuItem item = new JMenuItem(messages.get(text));
        item.addActionListener(listener);
        return item;
    }

    private class SelectProject implements ActionListener {
        private Project project;

        private SelectProject(Project project) {
            this.project = project;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            bus.publish(new ProjectChanged(project));
        }
    }

}
