package org.apromore.plugin.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apromore.plugin.services.impl.IllegalFileTypeException;
import org.zkoss.util.media.Media;

/**
 * Interface for creating file services.
 */
public interface FileHandlerService {
    /**
     * Writes the input files to an output stream.
     *
     * @param medias the input files.
     * @return return the message to show on client side.
     * @throws IllegalFileTypeException if the file type is unsupported
     */
    String writeFiles(Media[] medias)
                    throws IOException, IllegalFileTypeException;

    /**
     * Output a file.
     *
     * @return returns a file.
     */
    File outputFile();

    /**
     * Outputs all files.
     *
     * @return returns a list of files.
     */
    ArrayList<File> outputFiles();
}
