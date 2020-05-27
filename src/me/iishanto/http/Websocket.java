package me.iishanto.http;

import me.iishanto.HardwareSimulators.Mouse;
import me.iishanto.Toolkit;
import java.io.InputStream;
import java.io.OutputStream;

public class Websocket extends Thread {
    private InputStream is;
    private OutputStream os;
    private Mouse mouse=new Mouse();
    Websocket childClass=null;
    public Websocket(InputStream inputStream, OutputStream outputStream){
        is=inputStream;
        os=outputStream;
    }
    public void registerChild(Websocket child){childClass=child;}
    public void run(){
        read();
    }
    public void receiver(byte []data){
        //
    }

    protected void read(){
        int read=-1;
        byte []buff=new byte[1024];
        while (true){
            try {
                read = is.read(buff);
                if(read==-1) {
                    continue;
                }
                /*
                @Todo: Have receive and decode web socket request more efficiently
                 */
                byte []decoded=Toolkit.getInstance().decodeWs(buff, read);
                if(childClass!=null) {
                    childClass.receiver(decoded);
                }else {
                    System.out.println(new String(decoded));
                }
            }catch (Exception e){
                System.out.println(e.getLocalizedMessage()+" exception in WebsocketHandler.java: "+88);
                break;
            }
        }
    }
}
