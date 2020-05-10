package org.apromore.plugin;

import org.apromore.plugin.services.FileHandleService;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Fileupload;

import java.io.File;
import java.io.FileNotFoundException;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class FileUploadViewModel  {

    @WireVariable
    private FileHandleService fileHandleService;

    public FileUploadViewModel(){
    }

    @Init
    public void init(){
    }

    @Command("onFileUpload")
    public void onFileUpload() {
        Media media = Fileupload.get();
        if(media!=null){
            fileHandleService.writeFiles(media);
        }
    }
    @Command("onFileDownload")
    public void onFileDownload(){
        File file = fileHandleService.outputFiles();
        try{
            Filedownload.save(file,null);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
}