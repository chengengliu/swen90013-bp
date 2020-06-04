package org.apromore.plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apromore.plugin.services.FileHandlerService;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Messagebox;

/**
 * Model for the upload view.
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class FileUploadViewModel {
    private static final String NULL_UPLOAD_MESSAGE = "No file is selected";
    private static final String ERROR = "Error";

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
     * @throws IOException 
     */
    @Command("onFileUpload")
    public void onFileUpload() throws IOException {
        Media media = Fileupload.get();
        if (media != null) {
            String returnMessage;
            returnMessage = fileHandlerService.writeFiles(media);
            Messagebox.show(returnMessage);
        } else {
            Messagebox.show(
                NULL_UPLOAD_MESSAGE,
                ERROR,
                Messagebox.OK,
                Messagebox.ERROR
            );
        }
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
