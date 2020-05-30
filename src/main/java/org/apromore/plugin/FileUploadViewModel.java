package org.apromore.plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apromore.plugin.services.FileHandlerService;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.*;

/**
 * Model for the upload view.
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class FileUploadViewModel {
    private static final String NULL_UPLOAD_MESSAGE = "No file is selected";
    private static final String ERROR = "Error";
    private String textTable;

    @WireVariable
    private FileHandlerService fileHandlerService;

    /**
     * Initialise.
     */
    @Init
    public void init() {

    }

    private String createTableOutput(List<String> tableVal){
        String text = "";
        for(String row: tableVal) {
            text += row + "\n";
        }

        System.out.println(text);

        return text;
    }

    /**
     * Describes the actions taken when a file is uploaded.
     */
    @Command("onFileUpload")
    public void onFileUpload() {
        Media media = Fileupload.get();
        if (media != null) {
            String returnMessage;
            try {
                returnMessage = fileHandlerService.writeFiles(media);

                // If the file was written then load in impala and get snippet
                if(returnMessage.equals("Upload Success")){
                    List <String> resultsList = fileHandlerService.addTableGetSnippet(media.getName(), 10);
                    textTable = createTableOutput(resultsList);
                }
                Messagebox.show(returnMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }

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
