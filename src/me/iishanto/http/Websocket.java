package me.iishanto.http;

import me.iishanto.Toolkit;
import java.io.InputStream;
import java.io.OutputStream;

public class Websocket extends Thread {
    private final InputStream is;
    private final OutputStream os;
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
        int read;
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
                System.err.println(e.getLocalizedMessage());
                break;
            }
        }
    }
}
