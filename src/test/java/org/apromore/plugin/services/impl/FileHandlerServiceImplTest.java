package org.apromore.plugin.services.impl;

import java.io.*;

import org.apromore.plugin.PluginConfig;
import org.apromore.plugin.services.FileHandlerService;
import org.easymock.EasyMockSupport;
import org.easymock.internal.matchers.LessThan;
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
public class FileHandlerServiceImplTest extends EasyMockSupport {
    private static final String UPLOAD_FAILED = "Upload Failed";
    private static final String UPLOAD_SUCCESS = "Upload Success";

    // Inject dependencies managed by Spring.
    @Autowired
    FileHandlerService service;

    Media media;
    Media media1;
    Media media2;
    Media[] medias11;
    InputStream inputStream;
    BufferedInputStream bufferedInputStream;
    ByteArrayInputStream byteArrayInputStream;

    /**
     * Preparation for test.
     */
    @Before
    public void setup() {
        media = createMock(Media.class);
        inputStream = new ByteArrayInputStream("data".getBytes());

        media1 = createMock(Media.class);
        media2 = createMock(Media.class);

        medias11 = new Media[11];
        for (int i = 0; i < 11; i++) {
            Media temp = createMock(Media.class);
            medias11[i] = temp;
        }
    }

    /**
     * Test if string file is successfully saved.
     */
    @Test
    public void writeStringFilesTest() throws IOException {
        String mockString = "test";

        expect(media.getName()).andReturn("file.parquet");
        expect(media.isBinary()).andReturn(false);
        expect(media.getReaderData()).andReturn(new StringReader(mockString));

        replayAll();
        Media[] medias = { media };

        try {
            Assert.assertEquals(service.writeFiles(medias), UPLOAD_SUCCESS);
        } catch (IOException | IllegalFileTypeException e) {
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

        expect(media.getName()).andReturn("file.parquet");
        expect(media.isBinary()).andReturn(true);
        expect(media.getStreamData()).andReturn(inputStream);

        bufferedInputStream.close();
        expectLastCall();

        replayAll();
        Media[] medias = { media };

        try {
            Assert.assertEquals(service.writeFiles(medias), UPLOAD_SUCCESS);
        } catch (IllegalFileTypeException e) {
            e.printStackTrace();
        }

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

        expect(media.getName()).andReturn("file.parquet");
        expect(media.isBinary()).andReturn(true);
        expect(media.getStreamData()).andReturn(null);

        replayAll();
        Media[] medias = { media };

        try {
            service.writeFiles(medias);
        } catch (IOException | IllegalFileTypeException e) {
            e.printStackTrace();
        }

        verifyAll();
    }

    /**
     * Test if 2 files are successfully saved.
     */
    @Test
    public void write2FilesTest() throws IOException {
        bufferedInputStream = createMockBuilder(BufferedInputStream.class)
                .withConstructor(InputStream.class)
                .withArgs(inputStream)
                .createMock();

        expect(media1.getName()).andReturn("file1.parquet");
        expect(media1.isBinary()).andReturn(true);
        expect(media1.getStreamData()).andReturn(inputStream);

        expect(media2.getName()).andReturn("file2.csv");
        expect(media2.isBinary()).andReturn(true);
        expect(media2.getStreamData()).andReturn(inputStream);

        bufferedInputStream.close();
        expectLastCall();

        replayAll();
        Media[] medias = { media1, media2 };

        try {
            Assert.assertEquals(service.writeFiles(medias), UPLOAD_SUCCESS);
        } catch (IllegalFileTypeException e) {
            e.printStackTrace();
        }

        verifyAll();
    }
}
