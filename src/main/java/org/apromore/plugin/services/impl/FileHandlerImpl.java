package org.apromore.plugin.services.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zkoss.util.media.Media;

import java.io.*;
import java.util.logging.FileHandler;

@RestController
@RequestMapping("/files/")
public class FileHandlerImpl {
  private String temporalDir = null;
  private static final Logger logger = LogManager.getLogger(FileHandler.class);

  public FileHandlerImpl(){
    generateDirectory();
  }

  private void generateDirectory(){
    this.temporalDir= new File("").getAbsolutePath();
    File directory = new File(this.temporalDir+"/files/");
    if(!directory.exists()){
      logger.info("No files directory detected. Make a files directory. ");
      directory.mkdir();
    }
  }

//  public void writeFiles(String value){
//
//  }
  public void writeFiles(Media media){
    BufferedInputStream in = null;
    BufferedOutputStream out = null;
    try{
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