package org.apromore.plugin.services.impl;

import java.io.*;

import org.apromore.plugin.PluginConfig;
import org.apromore.plugin.services.FileHandlerService;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.zkoss.util.media.Media;



/**
 * Unit test class for FileHandlerImpl. Test includes file saving test
 * and directory test.
 */
@ContextConfiguration(classes = PluginConfig.class)
@RunWith(SpringRunner.class)
public class FileHandlerImplTest {
    private static final String UPLOAD_FAILED = "Upload Failed";
    private static final String UPLOAD_SUCCESS = "Upload Success";

    // Inject dependencies managed by Spring.
    @Autowired
    FileHandlerService service;

    Media media;
    InputStream inputStream;
    BufferedInputStream bufferedInputStream;
    ByteArrayInputStream byteArrayInputStream;

    /**
     * Preparation for test.
     */
    @Before
    public void setup() {
        media = EasyMock.createMock(Media.class);
        inputStream = EasyMock.createMock(InputStream.class);
    }

    /**
     *  Test if string file is successfully saved.
     */
    @Test
    public void writeStringFilesTest() throws IOException {
        String mockString = "test";
        byteArrayInputStream = EasyMock.createMockBuilder(
                ByteArrayInputStream.class)
                .withConstructor(byte[].class)
                .withArgs((Object) mockString.getBytes())
                .createMock();
        inputStream = byteArrayInputStream;

        EasyMock.expect(media.isBinary()).andReturn(false);
        EasyMock.expect(media.getStringData()).andReturn(mockString);

        EasyMock.replay(byteArrayInputStream);
        EasyMock.replay(media);

        Assert.assertEquals(service.writeFiles(media), UPLOAD_SUCCESS);
        EasyMock.verify(media);
    }

    /**
     * Test if stream file is successfully saved.
     */
    @Test
    public void writeStreamFilesTest() throws IOException {
        bufferedInputStream = EasyMock.createMockBuilder(
                BufferedInputStream.class)
                .withConstructor(InputStream.class)
                .withArgs(inputStream)
                .createMock();

        EasyMock.expect(media.isBinary()).andReturn(true);
        EasyMock.expect(media.getStreamData()).andReturn(inputStream);

        bufferedInputStream.close();
        EasyMock.expectLastCall();

        EasyMock.replay(inputStream);
        EasyMock.replay(bufferedInputStream);
        EasyMock.replay(media);

        Assert.assertEquals(service.writeFiles(media), UPLOAD_SUCCESS);
        EasyMock.verify(media);
    }

    /**
     * Test if null content is properly caught.
     */
    @Test(expected = NullPointerException.class)
    public void writeFileFailTest() {
        bufferedInputStream = EasyMock.createMockBuilder(
                BufferedInputStream.class)
                .withConstructor(InputStream.class)
                .withArgs(null)
                .createMock();

        EasyMock.expect(media.isBinary()).andReturn(true);
        EasyMock.expect(media.getStreamData()).andReturn(null);
        EasyMock.replay(media);

        service.writeFiles(media);
        EasyMock.verify(media);
    }
}
