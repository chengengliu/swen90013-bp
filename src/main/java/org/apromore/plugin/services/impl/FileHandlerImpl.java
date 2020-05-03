package org.apromore.plugin.services.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;

import java.io.*;
import java.util.logging.FileHandler;

@RestController
@RequestMapping("/files/")
public class FileHandlerImpl extends GenericForwardComposer  {
  private static final Logger logger = LogManager.getLogger(FileHandler.class);
  private static final int FILE_SIZE = 100;// 100k
  private static final String temporalDir = "/Users/Apple/Documents/Google-Sync/2020S1/YL/Project-Code/files/";

  private Image img;
  private Label msgLb;

  public FileHandlerImpl(){
    generateDirectory();
  }

  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
  }
  public void onClick$uploadBtn() {
    msgLb.setValue("");

    try {
      Media media = Fileupload.get();
      System.out.println("卡住了1");
      if(media == null){
        msgLb.setValue("please select a file");
        return;
      }
      System.out.println("卡住了2");
      String type = media.getContentType().split("/")[0];
      if (type.equals("image")) {
        if (media.getByteData().length > FILE_SIZE * 1024) {
          msgLb.setValue("File size limit " + FILE_SIZE + "k");
          return;
        }
        System.out.println("卡住了3");
        org.zkoss.image.Image picture = (org.zkoss.image.Image) media;
        img.setContent(picture);
      }
      logger.info("Finish uploading. Now write to server. ");
      System.out.println("Finish uploading. Now write to server. ");

      writeFiles(media);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void generateDirectory(){
    String temporalDir= new File("").getAbsolutePath();
    File directory = new File(temporalDir+"/files/");
    if(!directory.exists()){
      logger.info("No files directory detected. Make a files directory. ");
      directory.mkdir();
    }
  }

//  public void writeFiles(String value){
//
//  }
  public void writeFiles(Media media){
//    TODO: Finish the uploading but in an ugly way....
    String temporalDir = "/Users/Apple/Documents/Google-Sync/2020S1/YL/Project-Code/files/";
    generateDirectory();
    BufferedInputStream in = null;
    BufferedOutputStream out = null;
    try{
      System.out.println(this.temporalDir);
      InputStream fin = media.getStreamData();
      in = new BufferedInputStream(fin);
      File file = new File(this.temporalDir+media.getName());
      OutputStream fout = new FileOutputStream(file);
      out = new BufferedOutputStream(fout);
      byte buffer[] = new byte[1024];
      int ch = in.read(buffer);
      while (ch != -1) {
        out.write(buffer, 0, ch);
        ch = in.read(buffer);
      }
    }catch (IOException e){
      e.printStackTrace();
      throw new RuntimeException(e);
    }catch (Exception e){
      e.printStackTrace();
      throw new RuntimeException(e);
    }finally {
      try{
        if(out!=null) out.close();
        if(in!=null) in.close();
      }catch (IOException e){
        e.printStackTrace();
        throw new RuntimeException(e);
      }
    }
  }
}