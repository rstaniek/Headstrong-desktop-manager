package com.headstrongpro.desktop.model.resource;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by rajmu on 17.05.08.
 */
public class ResourceFactoryTest {
    @Test
    public void getResource() throws Exception {
        ResourceFactory rf = new ResourceFactory();
        Resource resource = rf.getResource(2, "asd", "sdf", "sdfsdf", false, 3);

        assertEquals(resource.toString(), "AudioFile");
    }

}