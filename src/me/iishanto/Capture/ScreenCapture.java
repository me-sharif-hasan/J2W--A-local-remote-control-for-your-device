package me.iishanto.Capture;

import me.iishanto.Toolkit;
import me.iishanto.http.HttpIO;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ScreenCapture extends Thread {
    OutputStream outputStream;
    HttpIO httpIO;
    public ScreenCapture(OutputStream outputStream){
        this.outputStream=outputStream;
        httpIO=new HttpIO(outputStream);
    }
    public void run(){
        try {
            httpIO.sendBinary("HTTP/1.1 200 OK\r\nContent-Type: multipart/x-mixed-replace; boundary=--BoundaryString\r\n\r\n\r\n".getBytes());
            Image cursor=null;
            try{
                cursor=ImageIO.read(Toolkit.getInstance().getCursor());
            }catch (Exception e){
                System.err.println("Can't read cursor file "+e.getLocalizedMessage());
            }
            while (true) {
                BufferedImage bufferedImage = new Robot().createScreenCapture(new Rectangle(java.awt.Toolkit.getDefaultToolkit().getScreenSize()));
                ByteArrayOutputStream tmp=new ByteArrayOutputStream();
                try {
                    Point pos=MouseInfo.getPointerInfo().getLocation();
                    int x=(int)pos.getX();
                    int y=(int)pos.getY();
                    if(cursor!=null){
                        Graphics2D graphics2D;
                        graphics2D = bufferedImage.createGraphics();
                        graphics2D.drawImage(cursor,x-3,y-2,16,25,null);
                    }
                }catch (Exception e){
                    System.err.println("Can't draw cursor: "+e.getLocalizedMessage());
                }
                ImageIO.write(bufferedImage, "jpeg", tmp);
                boolean sendHead=httpIO.sendBinary(("--BoundaryString\r\n" + "Content-type: image/jpeg\r\n" + "Content-Length: " + tmp.size() + "\r\n\r\n").getBytes());
                boolean sendData=httpIO.sendBinary(tmp.toByteArray());
                if(!(sendData&&sendHead)){
                    break;
                }
            }
        } catch (AWTException | IOException e) {
            System.err.println(e.getLocalizedMessage());
            currentThread().interrupt();
        }
    }
}
