package me.iishanto.HardwareSimulators;

import java.awt.*;
import java.awt.event.InputEvent;

public class Mouse {
    private Robot robot=null;
    public Mouse(){
        try {
            robot=new Robot();
        }catch (Exception e){
            System.out.println("Error to create robot: "+e.getLocalizedMessage());
        }
    }
    public void leftButtonDown(){
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    }
    public void leftButtonUp(){
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }
    public void rightButtonDown(){
        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
    }
    public void rightButtonUp(){
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
    }

    public void leftClick(int x,int y){
        robot.mouseMove(x, y);
        leftButtonDown();
        leftButtonUp();
    }
    public void rightClick(int x,int y){
        robot.mouseMove(x,y);
        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
    }
    public void scrollUp(){
        robot.mouseWheel(2);
    }
    public void scrollDown(){
        robot.mouseWheel(-2);
    }
    public void move(int x,int y){
        robot.mouseMove(x, y);
    }
    public int getX(){
        return (int)Math.round(MouseInfo.getPointerInfo().getLocation().getX());
    }
    public int getY(){
        return (int)Math.round(MouseInfo.getPointerInfo().getLocation().getY());
    }

}
