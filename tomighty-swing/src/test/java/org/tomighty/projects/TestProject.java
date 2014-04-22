package org.tomighty.projects;

import junit.framework.Assert;

import org.junit.Test;
import org.tomighty.mock.bus.MockBus;

public class TestProject {

    @Test
    public void testGetDisplayName() throws Exception {
        Project project = new Project("My project name is nice", 10, new MockBus());
        Assert.assertEquals("My project name is...", project.getDisplayName());
    }

}
