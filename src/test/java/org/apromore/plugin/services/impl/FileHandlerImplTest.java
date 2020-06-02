package org.apromore.plugin.services.impl;

import java.io.*;

import org.apromore.plugin.PluginConfig;
import org.apromore.plugin.services.FileHandlerService;
import org.easymock.EasyMockSupport;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.zkoss.util.media.Media;
import static org.easymock.EasyMock.*;

/**
 * Unit test class for FileHandlerImpl. Tests include file saving test and
 * directory test.
 */
@ContextConfiguration(classes = PluginConfig.class)
@RunWith(SpringRunner.class)
public class FileHandlerImplTest extends EasyMockSupport {
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
        media = createMock(Media.class);
        inputStream = createMock(InputStream.class);
    }

    /**
     * Test if string file is successfully saved.
     */
    @Test
    public void writeStringFilesTest() {
        String mockString = "test";

        expect(media.isBinary()).andReturn(false);
        expect(media.getStringData()).andReturn(mockString);

        replayAll();

        try {
            Assert.assertEquals(service.writeFiles(media), UPLOAD_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
        }

        verifyAll();
    }

    /**
     * Test if stream file is successfully saved.
     */
    @Test
    public void writeStreamFilesTest() throws IOException {
        bufferedInputStream = createMockBuilder(BufferedInputStream.class)
                .withConstructor(InputStream.class)
                .withArgs(inputStream)
                .createMock();

        expect(media.isBinary()).andReturn(true);
        expect(media.getStreamData()).andReturn(inputStream);

        bufferedInputStream.close();
        expectLastCall();

        replayAll();

        Assert.assertEquals(service.writeFiles(media), UPLOAD_SUCCESS);

        verifyAll();
    }

    /**
     * Test if null content is properly caught.
     */
    @Test(expected = NullPointerException.class)
    public void writeFileFailTest() {
        bufferedInputStream = createMockBuilder(BufferedInputStream.class)
                .withConstructor(InputStream.class)
                .withArgs(null)
                .createMock();

        expect(media.isBinary()).andReturn(true);
        expect(media.getStreamData()).andReturn(null);

        replayAll();

        try {
            service.writeFiles(media);
        } catch (IOException e) {
            e.printStackTrace();
        }

        verifyAll();
    }
}
