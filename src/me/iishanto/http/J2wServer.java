package me.iishanto.http;

import me.iishanto.Toolkit;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class J2wServer extends Thread {
    private final int port;
    private boolean listening=false;
    private ServerSocket serverSocket=null;
    private final List <Socket> connections= new ArrayList<>();
    public J2wServer(int port){
        this.port=port;
    }
    public void listen() throws IOException {
        serverSocket=new ServerSocket(port);
        listening=true;
        while (true){
            Socket socket=serverSocket.accept();
            Toolkit.getInstance().log("Client "+socket.getRemoteSocketAddress()+" connected\n");
            connections.add(socket);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                    new ClientRequestProcessor(socket.getInputStream(),socket.getOutputStream()).start();
                    } catch (IOException e) {
                        System.out.println(e.getLocalizedMessage()+" exception in J2wServer.java: "+29);
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }).start();

        }
    }
    public void run(){
        try{
            listen();
        }catch (Exception e){
            if(!listening) Toolkit.getInstance().alert("Error to start the server: "+e.getLocalizedMessage());
            currentThread().interrupt();
            return;
        }
    }
    public boolean serverStop(){
        try {
            serverSocket.close();
            for(Socket each:connections){
                each.close();
            }
            System.out.println("Server closed!");
            return true;
        }catch (Exception e){
            Toolkit.getInstance().alert("Error: "+e.getLocalizedMessage());
            return false;
        }
    }
}
