package me.iishanto.system;

import me.iishanto.HardwareSimulators.Keyboard;
import me.iishanto.HardwareSimulators.Mouse;
import me.iishanto.http.HttpIO;
import me.iishanto.http.Websocket;

import java.io.InputStream;
import java.io.OutputStream;

public class EventProcessor extends Websocket {
    Mouse mouse;
    Keyboard keyboard;
    public EventProcessor(InputStream inputStream, OutputStream outputStream){
        super(inputStream,outputStream);
        init();
    }
    public EventProcessor(HttpIO httpIO){
        super(httpIO.getInputStream(),httpIO.getOutputStream());
        init();
    }
    private void init(){
        mouse=new Mouse();
        keyboard=new Keyboard();
        super.registerChild(this);
    }
    public void run(){
        super.read();
    }
    public void receiver(byte []data){
        job(new String(data));
    }


    private void job(String s){
        //System.out.println(s);
        try {
            String []parts=s.split(",");
            String X="",Y="";int x=0,y=0;
            try {
                if(parts.length>=2) {
                    X = clean(parts[1]);
                    x=Integer.parseInt(X);
                }
                if(parts.length>=3) {
                    Y = clean(parts[2]);
                    y = Integer.parseInt(Y);
                }
            }catch (Exception e){
                System.out.println(e.getLocalizedMessage());
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
            }else if(parts[0].equals("MouseRightDown")){
                mouse.rightButtonDown();
            }else if(parts[0].equals("MouseRightUp")){
                mouse.rightButtonUp();
            }else if(parts[0].equals("ScrollUp")){
                mouse.scrollUp();
            }else if(parts[0].equals("ScrollDown")){
                mouse.scrollDown();
            }else if(parts[0].equals("KbdUp")){
               keyboard.handle(x,false);
            }else if(parts[0].equals("KbdDown")){
                keyboard.handle(x,true);
            }
        }catch (Exception e){
            System.err.println(e.getLocalizedMessage());
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
