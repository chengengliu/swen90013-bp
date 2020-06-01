package org.apromore.plugin.services.impl;

import java.io.*;

import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.EnumSet;
import java.util.Set;
import static java.nio.file.attribute.PosixFilePermission.*;
import java.util.ArrayList;
import java.util.List;

import org.apromore.plugin.services.FileHandlerService;
import org.springframework.stereotype.Service;
import org.zkoss.util.media.Media;

/**
 * Implement the file handle service.
 */
@Service("fileHandlerService")
public class FileHandlerImpl implements FileHandlerService {
    private static final int BUFFER_SIZE = 1024;
    private static final String UPLOAD_FAILED = "Upload Failed";
    private static final String UPLOAD_SUCCESS = "Upload Success";
    private String tempDir = null;
    private List<String> filePathList = new ArrayList<String>();

    /**
     * Create a directory to save the output files to.
     */
    private void generateDirectory() {
        String temporalDir = new File("").getAbsolutePath();
        File directory = new File(temporalDir + System.getenv("DATA_STORE"));
        this.tempDir = temporalDir + System.getenv("DATA_STORE");
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    /**
     * Output the files to the user who request download.
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
     */
    public String writeFiles(Media media) {
        generateDirectory();
        InputStream fIn;

        if (media.isBinary()) {
            fIn = media.getStreamData();
        } else {
            fIn = new ByteArrayInputStream(media.getStringData().getBytes());
        }

        BufferedInputStream in = new BufferedInputStream(fIn);
        BufferedOutputStream out = null;

        try {

            File file = new File(this.tempDir + media.getName());

            OutputStream fOut = new FileOutputStream(file);
            out = new BufferedOutputStream(fOut);
            byte buffer[] = new byte[BUFFER_SIZE];
            int ch = in.read(buffer);

            while (ch != -1) {
                out.write(buffer, 0, ch);

                ch = in.read(buffer);
            }

            changeFilePermission(this.tempDir + media.getName());

        } catch (Exception e) {
            e.printStackTrace();
            return UPLOAD_FAILED;
        } finally {
            try {
                in.close();
                if (out != null) {
                    out.close();
                }
                filePathList.add(this.tempDir + media.getName());
                return UPLOAD_SUCCESS;
            } catch (IOException e) {
                e.printStackTrace();
                return UPLOAD_FAILED;
            }
        }
    }

    /**
     * Change the File permission so that impala could read the
     * files in the volume.
     *
     * @param filePath Path of the file in the volume.
     * @throws Exception for the file permission change failure.
     */
    private void changeFilePermission(String filePath) throws Exception {
        Path path = Paths.get(filePath);
        Set<PosixFilePermission> permissions = EnumSet.of(OWNER_READ,
                                                          OWNER_WRITE,
                                                          GROUP_READ,
                                                          OTHERS_READ);

        PosixFileAttributeView posixView = Files.getFileAttributeView(path,
                PosixFileAttributeView.class);

        if (posixView == null) {
            System.out.format("POSIX attribute view  is not  supported%n.");
            return;
        }

        posixView.setPermissions(permissions);
        System.out.println("Permissions set successfully to rw-r--r--.");
    }

    /**
     * Returns a list of file paths.
     * @return a list of file paths.
     */
    public List<String> getFilePathList() {
        return filePathList;
    }
}
