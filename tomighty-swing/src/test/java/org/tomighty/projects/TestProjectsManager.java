package org.tomighty.projects;

import junit.framework.Assert;

import org.junit.Test;

public class TestProjectsManager {

    @Test
    public void testNormalizeProjectName() throws Exception {
        Assert.assertEquals("My project name is...", ProjectsManager.normalizeProjectName("My project name is nice"));
    }
}
