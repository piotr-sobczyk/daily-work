package org.tomighty.projects;

import junit.framework.Assert;

import org.junit.Test;

public class TestProject {

    @Test
    public void testGetDisplayName() throws Exception {
        Project project = new Project("My project name is nice", 10);
        Assert.assertEquals("My project name is...", project.getDisplayName());
    }

}
