package org.apromore.plugin.services.impl;

import java.io.*;

import org.apromore.plugin.PluginConfig;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.zkoss.util.media.Media;

import  static org.junit.Assert.*;



/**
 * Unit test class for FileHandlerImpl. Mainly test on directory creation
 * and files saving.
 */
@ContextConfiguration(classes = PluginConfig.class)
@RunWith(SpringRunner.class)
public class FileHandlerImplTest {
    private File directory;
    private Media media;

    @Autowired
    FileHandlerImpl handler;

    /**
     * Preparation for test.
     */
    @Before
    public void before() {
        directory = new File(new File("").getAbsolutePath() + "/files/");
        media = new Media() {
            @Override
            public boolean isBinary() {
                return true;
            }

            @Override
            public boolean inMemory() {
                return false;
            }

            @Override
            public byte[] getByteData() {
                return new byte[0];
            }

            @Override
            public String getStringData() {
                return "test";
            }

            @Override
            public InputStream getStreamData() {
                return null;
            }

            @Override
            public Reader getReaderData() {
                return null;
            }

            @Override
            public String getName() {
                return "test.txt";
            }

            @Override
            public String getFormat() {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isContentDisposition() {
                return false;
            }
        };
    }

    /**
     * Test the effect of the private method that has the correct behaviour.
     */
    @Test
    public void testGenerateDirectory() {
        assertTrue(directory.exists());
    }

    /**
     * Test that the file is properly saved.
     */
    @Test
    public void testWriteFiles() {
        handler.writeFiles(media);
        File f = new File(directory.toString());
        String name = "test";
        File[] matches = f.listFiles(new FilenameFilter() {
            public boolean accept(File pathname, String name) {
                return name.startsWith("test");
            }
        });
        assertTrue(matches.length >= 1);
    }
}
