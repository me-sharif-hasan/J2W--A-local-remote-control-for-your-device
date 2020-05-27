package me.iishanto.image;

import me.iishanto.Toolkit;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Capture extends Thread {
    OutputStream outputStream;
    public Capture(OutputStream outputStream){
        this.outputStream=outputStream;
    }
    public void run(){
        try {
            String httpResponse="HTTP/1.1 200 OK\r\n" +
                    "Content-Type: multipart/x-mixed-replace; boundary=--BoundaryString\r\n" +
                    "\r\n\r\n";
            outputStream.write(httpResponse.getBytes());
            Image cursor=null;
            try{
                cursor=ImageIO.read(Toolkit.getInstance().getCursor());
            }catch (Exception e){
                System.out.println(e.getLocalizedMessage()+" exception in Capture.java: "+26);
            }
            while (true) {
                BufferedImage bufferedImage = new Robot().createScreenCapture(new Rectangle(java.awt.Toolkit.getDefaultToolkit().getScreenSize()));
                ByteArrayOutputStream tmp=new ByteArrayOutputStream();
                try {
                    Point pos=MouseInfo.getPointerInfo().getLocation();
                    int x=(int)pos.getX();
                    int y=(int)pos.getY();
                    if(cursor!=null){
                        Graphics2D graphics2D=bufferedImage.createGraphics();
                        graphics2D.drawImage(cursor,x-2,y-1,16,25,null);
                    }
                }catch (Exception e){
                    System.out.println(e.getLocalizedMessage()+" exception in Capture.java: "+42);
                }

                ImageIO.write(bufferedImage, "jpeg", tmp);
                if(outputStream==null){
                    currentThread().interrupt();
                    return;
                }
                outputStream.write(("--BoundaryString\r\n" + "Content-type: image/jpeg\r\n" + "Content-Length: " + tmp.size() + "\r\n\r\n").getBytes());
                outputStream.write(tmp.toByteArray());
                outputStream.flush();
                //TimeUnit.MILLISECONDS.sleep(20);
            }
        } catch (AWTException | IOException e) {
            System.out.println(e.getLocalizedMessage()+" exception in Capture.java: "+52);
            currentThread().interrupt();
            return;
        }
    }
}
