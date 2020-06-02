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
    public void handle(char ch){
        System.err.println("You pressed "+ch+" make a way out for keyboard handling");
    }
}
