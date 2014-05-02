package org.dailywork.projects;

public class Project {
    private static final int DISPLAY_NAME_TRIM_THRESHOLD = 18;

    private String name;
    private int dailyTimeMins;

    public Project(String name, int dailyTimeMins) {
        this.name = name;
        this.dailyTimeMins = dailyTimeMins;
    }

    public int getDailyTimeMins() {
        return dailyTimeMins;
    }

    public void setDailyTimeMins(int dialyTimeMins) {
        this.dailyTimeMins = dialyTimeMins;
    }

    public String getDisplayName() {
        if (name.length() > DISPLAY_NAME_TRIM_THRESHOLD) {
            return name.substring(0, DISPLAY_NAME_TRIM_THRESHOLD) + "...";
        }
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
