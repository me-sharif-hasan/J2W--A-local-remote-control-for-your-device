package me.iishanto.Capture;

import me.iishanto.Toolkit;
import me.iishanto.http.HttpIO;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

public class ScreenCapture extends Thread {
    OutputStream outputStream;
    HttpIO httpIO;
    static Robot robot;
    static Image cursor=null;
    static Rectangle screenRectangle;
    private static boolean captureRunning=false;

    private static ByteArrayOutputStream currentFrame=new ByteArrayOutputStream();

    public ScreenCapture(OutputStream outputStream){
        this.outputStream=outputStream;
        httpIO=new HttpIO(outputStream);
        try {
            robot=new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        screenRectangle=new Rectangle(java.awt.Toolkit.getDefaultToolkit().getScreenSize());
        try{
            cursor=ImageIO.read(Toolkit.getInstance().getCursor());
        }catch (Exception e){
            System.err.println("Can't read cursor file "+e.getLocalizedMessage());
        }
    }
    public void run(){
        capture();
            httpIO.sendBinary("HTTP/1.1 200 OK\r\nContent-Type: multipart/x-mixed-replace; boundary=--BoundaryString\r\n\r\n".getBytes());
            while (true) {
                ByteArrayOutputStream tmp=currentFrame;
                if(tmp.size()==0) continue;
                boolean sendHead=httpIO.sendBinary(("--BoundaryString\r\n" + "Content-type: image/jpeg\r\n" + "Content-Length: " + tmp.size() + "\r\n\r\n").getBytes());
                boolean sendData=httpIO.sendBinary(tmp.toByteArray());
                if(!(sendData&&sendHead)){
                    break;
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    }

    private static void capture(){
        Toolkit.getInstance().log("New capture request\n");
        if(captureRunning) {
            Toolkit.getInstance().log("Capture request denied! robot already running\n");
            return;
        }
        captureRunning=true;
        new Thread(() -> {
            Toolkit.getInstance().log("Screen capturing started\n");
            while (true) {
                BufferedImage bufferedImage = robot.createScreenCapture(screenRectangle);
                try {
                    Point pos = MouseInfo.getPointerInfo().getLocation();
                    int x = (int) pos.getX();
                    int y = (int) pos.getY();
                    if (cursor != null) {
                        Graphics2D graphics2D = bufferedImage.createGraphics();
                        graphics2D.drawImage(cursor, x - 3, y - 2, 16, 25, null);
                    }
                    ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                    ImageIO.write(bufferedImage, "jpeg", byteArrayOutputStream);
                    currentFrame=byteArrayOutputStream;
                } catch (Exception e) {
                    System.err.println(e.getLocalizedMessage());
                }
            }
        }).start();
    }
}
