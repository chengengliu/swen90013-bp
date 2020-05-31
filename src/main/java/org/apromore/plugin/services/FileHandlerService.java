package org.apromore.plugin.services;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.zkoss.util.media.Media;

/**
 *Interface for creating file services.
 */
public interface FileHandlerService {

    /**
     * Writes the input file to an output stream.
     * @param media the input file.
     * @return return the message to show on client side.
     */
    String writeFiles(Media media) throws IOException;

    /**
     * Add the file to the Impala and get a snippet.
     * @param fileName File Name
     * @param limit  Limit of the rows
     * @return return the snippet of the table.
     */
    List<List<String>> addTableGetSnippet(String fileName, int limit);

    /**
     * Output the file.
     * @return returns a file.
     */
    File outputFiles();
}
