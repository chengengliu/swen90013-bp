package org.apromore.plugin.services;

import org.zkoss.util.media.Media;

import java.io.File;

/**
 *Interface for creating file services.
 */
public interface FileHandlerService {

    /**
     * Writes the input file to an output stream.
     * @param media the input file.
     * @return return the message to show on client side.
     */
    String writeFiles(Media media);

    /**
     * Output the file.
     * @return returns a file.
     */
    File outputFiles();
}
