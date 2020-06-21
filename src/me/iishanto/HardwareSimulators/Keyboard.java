package me.iishanto.HardwareSimulators;

import java.awt.*;

public class Keyboard {
    private Robot robot=null;
    public Keyboard(){
        try {
            robot=new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
    public void handle(int key,boolean isDown){
        if(isDown){
            robot.keyPress(key);
        }else{
            robot.keyRelease(key);
        }
    }
}
