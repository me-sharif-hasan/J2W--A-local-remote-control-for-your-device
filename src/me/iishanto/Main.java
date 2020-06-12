package me.iishanto;

import me.iishanto.http.J2wServer;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println(Toolkit.getInstance().getDir());
        JFrame jFrame=createWindow();
        Label label1 = new Label("Port: ",SwingConstants.CENTER);

        label1.setBounds(100,100,50,30);
        label1.setBackground(Color.lightGray);
        JTextField jTextFieldForPort=new JTextField(20);
        jTextFieldForPort.setBounds(155,100,300,30);
        jTextFieldForPort.setBorder(new LineBorder(Color.gray,0));
        jTextFieldForPort.setText("8080");

        JTextArea jTextAreaForIP = new JTextArea();
        jTextAreaForIP.setBounds(0 ,200, 500,500);

        List<String> lst= Toolkit.getInstance().getIp();
        jTextAreaForIP.setText("Ready\n");
        jTextAreaForIP.setText("Start the server please\n");

        JButton jButtonOne=new JButton("Start server");
        JButton jButtonTwo=new JButton("Exit");

        jButtonOne.setBounds(100,150,200,35);
        jButtonTwo.setBounds(330,150,100,35);
        jButtonOne.setBackground(Color.gray);
        jButtonTwo.setBackground(Color.red);
        jButtonOne.setBorder(new LineBorder(Color.gray,0));
        jButtonTwo.setBorder(new LineBorder(Color.gray,0));

        jButtonTwo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeWindow(jFrame);
                System.exit(200);
            }
        });

        jButtonOne.addActionListener(new ActionListener() {
            Thread t=null;
            J2wServer j2wServer=null;
            @Override
            public void actionPerformed(ActionEvent e) {
                String port="";
                port=jTextFieldForPort.getText();
                try{
                    if(j2wServer==null) {
                        int prt = Integer.parseInt(port);
                        Toolkit.getInstance().setPort(prt);
                        j2wServer=new J2wServer(Toolkit.getInstance().getPort());
                        t = new Thread(j2wServer);
                        t.start();
                        jButtonOne.setText("Stop");
                        jTextAreaForIP.append("Server started at port: "+Toolkit.getInstance().getPort()+"\n\n");
                        jTextAreaForIP.append("Browse from your browser by one of those addresses:\n");
                        showIp(jTextAreaForIP);
                    }else{
                        if(t!=null){
                            j2wServer.serverStop();
                            j2wServer=null;
                            t.interrupt();
                            jButtonOne.setText("Start server");
                            jTextAreaForIP.setText("Server closed!\nStart again please\n");
                        }else{
                            Toolkit.getInstance().alert("You must run the server first");
                        }
                    }
                }catch (Exception x){
                    jTextAreaForIP.setText(x.getLocalizedMessage());
                    JOptionPane.showMessageDialog(null,"Error: "+x.getLocalizedMessage());
                }
            }
        });

        jFrame.add(jButtonOne);
        jFrame.add(jButtonTwo);
        jFrame.add(jTextFieldForPort);
        jFrame.add(label1);
        jFrame.add(jTextAreaForIP);
        jFrame.setVisible(true);
    }
    private static JFrame createWindow(){
        JFrame jFrame=new JFrame("J2W - control panel");
        jFrame.setIconImage(Toolkit.getInstance().icon().getImage());
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(null);
        /*jFrame.setUndecorated(true);*/
        jFrame.setBounds(300,50,500,500);
        return jFrame;
    }
    private static void closeWindow(JFrame jFrame){
        jFrame.setVisible(false);
        jFrame.dispose();
    }
    private static void showIp(JTextArea jTextArea){
        List<String> lst=Toolkit.getInstance().getIp();
        for(String a:lst){
            jTextArea.append("-->http://"+a+"\n");
        }
    }
}
