package org.apromore.plugin.services.impl;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.apromore.plugin.services.FileHandlerService;
import org.springframework.stereotype.Service;
import org.zkoss.util.media.Media;
import static java.nio.file.attribute.PosixFilePermission.*;

/**
 * Implement the file handle service.
 */
@Service("fileHandlerService")
public class FileHandlerImpl implements FileHandlerService {
    private static final int BUFFER_SIZE = 1024;
    private static final String UPLOAD_FAILED = "Upload Failed";
    private static final String UPLOAD_SUCCESS = "Upload Success";
    private String tempDir = null;
    private ImpalaJdbc impalaJdbc;

    /**
     * Create a directory to save the output files to.
     */
    private void generateDirectory() {
        String temporalDir = new File("").getAbsolutePath();
        File directory = new File(temporalDir + "/files/");
        this.tempDir = temporalDir + "/files/";
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
     * @param media the input file.
     * @return return the message to show on client side.
     */
    public String writeFiles(Media media) {
        generateDirectory();
        InputStream fIn = media.getStreamData();
        BufferedInputStream in = new BufferedInputStream(fIn);
        BufferedOutputStream out = null;

        try {
            File file = new File(this.tempDir + media.getName());

            System.out.println(file.toString());

            OutputStream fOut = new FileOutputStream(file);
            out = new BufferedOutputStream(fOut);
            byte buffer[] = new byte[BUFFER_SIZE];
            int ch = in.read(buffer);
            while (ch != -1) {
                out.write(buffer, 0, ch);

                ch = in.read(buffer);
            }

            in.close();
            if (out != null) {
                out.close();
            }

            changeFilePermission(this.tempDir + media.getName());

        } catch (IOException e) {
            e.printStackTrace();
            return UPLOAD_FAILED;
        } catch (Exception e) {
            e.printStackTrace();
            return UPLOAD_FAILED;
        } finally {
            return UPLOAD_SUCCESS;
        }
    }

    /**
     * Add the file to the Impala and get a snippet
     * @param fileName File Name
     * @param limit  Limit of the rows
     * @return return the snippet of the table.
     */
    public List<String> addTableGetSnippet(String fileName, int limit) {

        List<String> resultsList = null;
        impalaJdbc = new ImpalaJdbc();

        String tableName = fileName.split("\\.")[0];

        System.out.println("Adding To Table: " + tableName + " | "+ fileName);

        // Adding the file into the Impala as a table
        boolean isTableAdded =  impalaJdbc.addTable(tableName, fileName);

        if(isTableAdded){
            System.out.println("Table sucessfully Added!!");
            resultsList = impalaJdbc.executeQuery("SELECT * FROM "
                                            + tableName + " LIMIT " + limit);
        }

        impalaJdbc = null;

        return resultsList;
    }

    private void changeFilePermission(String filePath) throws Exception {
        Path path = Paths.get(filePath);

        PosixFileAttributeView posixView = Files.getFileAttributeView(path,
                PosixFileAttributeView.class);
        if (posixView == null) {
            System.out.format("POSIX attribute view  is not  supported%n.");
            return;
        }
        updatePermissions(posixView);
    }

    private void updatePermissions(PosixFileAttributeView posixView)
            throws Exception {
        Set<PosixFilePermission> permissions = EnumSet.of(OWNER_READ, OWNER_WRITE,
                                                GROUP_READ, OTHERS_READ);
        posixView.setPermissions(permissions);
        System.out.println("Permissions set successfully to rw-r--r--.");
    }
}
