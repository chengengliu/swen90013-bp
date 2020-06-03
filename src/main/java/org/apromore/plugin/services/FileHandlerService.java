package org.apromore.plugin.services;

import java.io.File;
import java.io.IOException;

import org.apromore.plugin.services.impl.IllegalFileTypeException;
import org.zkoss.util.media.Media;

/**
 * Interface for creating file services.
 */
public interface FileHandlerService {
    /**
     * Writes the input file to an output stream.
     *
     * @param media the input file.
     * @return return the message to show on client side.
     * @throws IllegalFileTypeException if the file type is unsupported
     */
    String writeFiles(Media media) throws IOException, IllegalFileTypeException;

    /**
     * Output the file.
     *
     * @return returns a file.
     */
    File outputFiles();

}
