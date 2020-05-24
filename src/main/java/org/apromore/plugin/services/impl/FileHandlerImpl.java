package org.apromore.plugin.services.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.FileHandler;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apromore.plugin.services.FileHandlerService;
import org.springframework.stereotype.Service;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.select.annotation.WireVariable;

/**
 * Implement the file handle service.
 */
@Service("fileHandlerService")
public class FileHandlerImpl implements FileHandlerService {
    private static final int BUFFER_SIZE = 1024;
    private static final String UPLOAD_FAILED = "Upload Failed";
    private static final String UPLOAD_SUCCESS = "Upload Success";
    private static final Logger logger = LogManager
            .getLogger(FileHandler.class);
    private String tempDir = null;

//    @WireVariable
//    private ImpalaJdbc impalaJdbc;

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

    private List <String> addTableGetSnippet(String fileName){
        List<String> resultsList = null;
        ImpalaJdbc impalaJdbc = new ImpalaJdbc();

        String tableName = fileName.split("\\.")[0];

        System.out.println("Adding To Table: " + tableName + " | "+ fileName);

//        boolean isTableAdded =  impalaJdbc.addTable(tableName, fileName);
        boolean isTableAdded =  true;

        if(isTableAdded){
            System.out.println("Table sucessfully Added!!");
            resultsList = impalaJdbc.executeQuery("SELECT * FROM "
                                                + tableName + " LIMIT 10");
        }

        return resultsList;
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

            List <String> resultsList = addTableGetSnippet(media.getName());

        } catch (IOException e) {
            e.printStackTrace();
            return UPLOAD_FAILED;
        } catch (Exception e) {
            e.printStackTrace();
            return UPLOAD_FAILED;
        } finally {
            try {
                in.close();
                if (out != null) {
                    out.close();
                }
                return UPLOAD_SUCCESS;
            } catch (IOException e) {
                e.printStackTrace();
                return UPLOAD_FAILED;
            }
        }
    }
}
