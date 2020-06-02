package org.apromore.plugin.services.impl;

import static java.nio.file.attribute.PosixFilePermission.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.util.EnumSet;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apromore.plugin.services.FileHandlerService;
import org.springframework.stereotype.Service;
import org.zkoss.util.media.Media;

/**
 * Implement the file handle service.
 */
@Service("fileHandlerService")
public class FileHandlerServiceImpl implements FileHandlerService {
    private static final int BUFFER_SIZE = 1024;
    private static final String UPLOAD_FAILED = "Upload Failed";
    private static final String UPLOAD_SUCCESS = "Upload Success";
    private String tempDir = System.getProperty("java.io.tmpdir") +
        System.getenv("DATA_STORE");

    /**
     * Create a directory to save the output files to.
     *
     * @param path name of the file to create a directory for
     * @throws IOException
     */
    private void generateDirectory(String path) throws IOException {
        new File(path).mkdirs();
        changeFilePermission(path);
    }

    /**
     * Output the files to the user who request download.
     *
     * @return a file
     */
    public File outputFiles() {
        File dir = new File(this.tempDir);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            // Select one file and return it. This is for demo purposes.
            for (File f : directoryListing) {
                return f;
            }
        }
        return null;
    }

    /**
     * Writes the input file to an output buffer.
     *
     * @param media the input file.
     * @return return the message to show on client side.
     * @throws IllegalFileTypeException if file type is not supported
     */
    public String writeFiles(Media media) throws IllegalFileTypeException {
        String fileName = media.getName();
        String path;

        if (fileName.endsWith(".csv")) {
            path = this.tempDir + "/" +
                FilenameUtils.removeExtension(fileName) + "_csv" + "/" +
                fileName;
        } else {
            path = this.tempDir + "/" +
                FilenameUtils.removeExtension(fileName) + "/" + fileName;
        }

        if (!(
            fileName.endsWith(".csv") ||
            fileName.endsWith("dat") ||
            fileName.endsWith(".parq") ||
            fileName.endsWith(".parquet"))) {
            throw new IllegalFileTypeException("File must be csv or parquet.");
        }

        try {
            if (fileName.endsWith(".csv")) {
                generateDirectory(
                    this.tempDir + "/" + FilenameUtils.removeExtension(fileName) +
                        "_csv");
            }

            generateDirectory(
                this.tempDir + "/" + FilenameUtils.removeExtension(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return UPLOAD_FAILED;
        }


        File file = new File(path);

        try (
            InputStream fIn = (
                media.isBinary() ?
                media.getStreamData() :
                new ByteArrayInputStream(media.getStringData().getBytes()));
            OutputStream fOut = new FileOutputStream(file, false);
            BufferedInputStream in = new BufferedInputStream(fIn);
            BufferedOutputStream out = new BufferedOutputStream(fOut);
        ) {
            byte buffer[] = new byte[BUFFER_SIZE];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }

            changeFilePermission(path);
        } catch (Exception e) {
            e.printStackTrace();
            return UPLOAD_FAILED;
        }

        return UPLOAD_SUCCESS;
    }

    /**
     * Change the File permission so that impala could read the files in the
     * volume.
     *
     * @param filePath Path of the file in the volume.
     * @throws IOException
     * @throws Exception for the file permission change failure.
     */
    private void changeFilePermission(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Set<PosixFilePermission> permissions = EnumSet.of(
            OWNER_READ,
            OWNER_WRITE,
            OWNER_EXECUTE,
            GROUP_READ,
            GROUP_WRITE,
            GROUP_EXECUTE,
            OTHERS_READ,
            OTHERS_WRITE,
            OTHERS_EXECUTE);

        PosixFileAttributeView posixView = Files
                .getFileAttributeView(path, PosixFileAttributeView.class);

        if (posixView == null) {
            System.out.format("POSIX attribute view  is not  supported%n.");
            return;
        }

        posixView.setPermissions(permissions);
        System.out.println("Permissions set successfully to rw-r--r--.");
    }
}
