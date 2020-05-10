package org.apromore.plugin.services;

import org.zkoss.util.media.Media;

import java.io.File;

public interface FileHandleService {
    public void writeFiles(Media media);
    public File outputFiles();
}