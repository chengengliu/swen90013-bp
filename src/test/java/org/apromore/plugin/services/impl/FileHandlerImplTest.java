package org.apromore.plugin.services.impl;

import org.apromore.plugin.PluginConfig;
import org.apromore.plugin.services.FileHandlerService;
import org.easymock.EasyMock;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.zkoss.util.media.Media;

import java.io.InputStream;
import java.io.IOException;


/**
 * Unit test class for FileHandlerImpl. Test includes file saving test
 * and directory test.
 */
@ContextConfiguration(classes = PluginConfig.class)
@RunWith(SpringRunner.class)
public class FileHandlerImplTest {
    private static final String UPLOAD_FAILED = "Upload Failed";
    private static final String UPLOAD_SUCCESS = "Upload Success";

    FileHandlerService service;
    Media media;

    /**
     * Preparation for test.
     */
    @Before
    public void setup() {
        service = EasyMock.createMock(FileHandlerService.class);
        media = EasyMock.createMock(Media.class);
    }

    /**
     *  Test if string file is successfully saved.
     */
    @Test
    public void writeStringFilesTest() {
        String mockString = "test";
        EasyMock.expect(media.isBinary()).andReturn(false);
        EasyMock.expect(media.getStringData()).andReturn(mockString);
        EasyMock.expect(service.writeFiles(media)).andReturn(UPLOAD_SUCCESS);
        EasyMock.replay(service, media);
    }

    /**
     * Test if stream file is successfully saved.
     */
    @Test
    public void writeStreamFilesTest() throws IOException {
        InputStream inputStream = EasyMock.createMock(InputStream.class);
        EasyMock.expect(inputStream.read()).andReturn(1);
        EasyMock.expect(media.isBinary()).andReturn(true);
        EasyMock.expect(media.getStreamData()).andReturn(inputStream);
        EasyMock.expect(service.writeFiles(media)).andReturn(UPLOAD_SUCCESS);
        EasyMock.replay(service, media, inputStream);
    }

    /**
     * Test if fail message is correctly returned.
     */
    @Test
    public void writeFileFailTest() {
        Media media = null;
        EasyMock.expect(service.writeFiles(media)).andReturn(UPLOAD_FAILED);
        EasyMock.replay(service);
    }
}
