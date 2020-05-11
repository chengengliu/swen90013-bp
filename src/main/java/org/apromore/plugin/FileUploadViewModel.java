package org.apromore.plugin;

import org.apromore.plugin.services.FileHandlerService;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Messagebox;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Model for the upload view.
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class FileUploadViewModel {

    @WireVariable
    private FileHandlerService fileHandlerService;

    /**
     * Initialise.
     */
    @Init
    public void init() {
    }

    /**
     * Describes the actions taken when a file is uploaded.
     */
    @Command("onFileUpload")
    public void onFileUpload() {
        Media media = Fileupload.get();
        String returnMessage = null;
        if (media != null) {
            returnMessage = fileHandlerService.writeFiles(media);
        }
        Messagebox.show(returnMessage);
    }

    /**
     * Describes the actions taken when a file is downloaded.
     */
    @Command("onFileDownload")
    public void onFileDownload() {
        File file = fileHandlerService.outputFiles();
        try {
            Filedownload.save(file, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
