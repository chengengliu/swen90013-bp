package org.apromore.plugin.services.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apromore.plugin.services.FileHandleService;
import org.springframework.stereotype.Service;
import org.zkoss.util.media.Media;

import java.io.*;
import java.util.logging.FileHandler;

/**
 * Implement the file handle service.
 */
@Service("fileHandleService")
public class FileHandlerImpl implements FileHandleService {
    private static final Logger logger = LogManager
            .getLogger(FileHandler.class);
    private static final int FILE_SIZE = 100;// 100k
    private String temporalDir = null;

    /**
    * Constructor.
    */
    public FileHandlerImpl() {
    }

    /**
     * Create a directory to save the output files to.
     */
    private void generateDirectory() {
        String temporalDir = new File("").getAbsolutePath();
        File directory = new File(temporalDir + "/files/");
        this.temporalDir = temporalDir + "/files/";
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    /**
     * Output the files to the specified directory.
     * @return a file
     */
    public File outputFiles() {
        File dir = new File(this.temporalDir);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            //Select one file and return it. This is for demo purposes.
            for (File f: directoryListing) {
                return f;
            }
        }
        return null;
    }

    /**
     * Writes the input file to an output buffer.
     * @param media - The input file.
     */
    public void writeFiles(Media media) {
        generateDirectory();
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            System.out.println(this.temporalDir);
            InputStream fin = media.getStreamData();
            in = new BufferedInputStream(fin);
            File file = new File(this.temporalDir + media.getName());
            OutputStream fout = new FileOutputStream(file);
            out = new BufferedOutputStream(fout);
            byte buffer[] = new byte[1024];
            int ch = in.read(buffer);
            while (ch != -1) {
                out.write(buffer, 0, ch);
                ch = in.read(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}
