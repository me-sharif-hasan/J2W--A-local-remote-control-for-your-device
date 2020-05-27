package me.iishanto.system;

import me.iishanto.HardwareSimulators.Mouse;
import me.iishanto.http.Websocket;

import java.io.InputStream;
import java.io.OutputStream;

public class EventProcessor extends Websocket {
    Mouse mouse;
    public EventProcessor(InputStream inputStream, OutputStream outputStream){
        super(inputStream,outputStream);
        mouse=new Mouse();
        super.registerChild(this);
    }
    public void run(){
        super.read();
    }
    public void receiver(byte []data){
        job(new String(data));
    }


    private void job(String s){
        try {
            //System.out.println(s);
            String []parts=s.split(",");
            String X,Y;int x=0,y=0;
            try {
                X=clean(parts[1]);
                Y=clean(parts[2]);
                x=Integer.parseInt(X);
                y=Integer.parseInt(Y);
            }catch (Exception e){
                System.out.println(e.getLocalizedMessage()+" exception in WebsocketHandler.java: "+40);
            }
            int px=mouse.getX();
            int py=mouse.getY();
            if(parts[0].equals("Click")) {/*Click event handling*/
                mouse.leftClick(px,py);
            }else if(parts[0].equals("PointerMove")){/*Moving of a pointer*/
                mouse.move(x, y);
            }else if(parts[0].equals("RightClick")){/*RightClick handler*/
                /*
                @A bug in front-end js, when context menu even fired, somehow pointer_move() function get called
                 */
                mouse.rightClick(px,py);
            }else if(parts[0].equals("MouseLeftDown")){
                mouse.leftButtonDown();
            }else if(parts[0].equals("MouseLeftUp")){
                mouse.leftButtonUp();
            }else if(parts[0].equals("ScrollUp")){
                mouse.scrollUp();
            }else if(parts[0].equals("ScrollDown")){
                mouse.scrollDown();
            }
        }catch (Exception e){
            System.out.println(e.getLocalizedMessage()+" exception in WebsocketHandler.java: "+67);
        }
    }

    private String clean(String arg){
        String X="";
        for(int i=1;i<arg.length();i++){
            if(arg.charAt(i)=='$') break;
            X+=arg.charAt(i);
        }
        return X;
    }
}
