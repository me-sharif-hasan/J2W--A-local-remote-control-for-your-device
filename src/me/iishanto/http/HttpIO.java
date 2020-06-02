package me.iishanto.http;

import me.iishanto.Toolkit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class HttpIO {
    private InputStream inputStream;
    public OutputStream outputStream;
    boolean websocket=false;
    private String headers="";
    private String contentType="";
    private String wsKey="";
    public HttpIO(InputStream is, OutputStream os){
        inputStream=is;
        outputStream=os;
    }
    public void setAsWebsocket(String Sec_WebSocket_Key){
        if (Sec_WebSocket_Key == null) {
            return;
        }
        if (Sec_WebSocket_Key.charAt(0) == ' ') {
            Sec_WebSocket_Key = Sec_WebSocket_Key.substring(1, Sec_WebSocket_Key.length());
        }
        if (Sec_WebSocket_Key.charAt(Sec_WebSocket_Key.length() - 1) == ' ') {
            Sec_WebSocket_Key = Sec_WebSocket_Key.substring(0, Sec_WebSocket_Key.length() - 1);
        }
        Toolkit.getInstance().calculate(Sec_WebSocket_Key);
        websocket=true;
    }
    public void setAsHttp(){
        websocket=false;
    }
    private void setHeaders(){
        if(websocket){
            headers="HTTP/1.1 101 Switching Protocols\r\n" +
                    "Upgrade: websocket\r\n" +
                    "Connection: Upgrade\r\n" +
                    "Sec-WebSocket-Accept: " + Toolkit.getInstance().getWsKey() +
                    "\r\n\r\n";
        }else{
            headers="HTTP/1.1 200 OK\r\n" +
                    "Content-Type: " + contentType + "\r\n" +
                    "\r\n\r\n";
        }
    }
    public void sendHeaders(){
        setHeaders();
        sendToOs(headers);
    }
    public void closeOutputStream(){
        try {
            outputStream.close();
        } catch (IOException e) {
            System.out.println("Exception in HttpSendData line 50: "+e.getLocalizedMessage());
        }
    }
    public void setContentType(String type){
        contentType=type;
    }
    public long send(File file){
        if(contentType.equals("")){
            try {
                contentType= Files.probeContentType(Path.of(file.getAbsolutePath()));
            } catch (IOException e) {
                System.out.println("Exception in HttpSendData line 49: "+e.getLocalizedMessage());
            }
        }
        try {
            sendHeaders();
            String data = "";
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                sendToOs(scanner.nextLine() + "\n");
            }
        }catch (Exception e){
            System.out.println("Exception in HttpSendData line 60: "+e.getLocalizedMessage());
        }
        return 0;
    }
    public long send(String s){
        sendHeaders();
        sendToOs(s);
        return 0;
    }
    public long send(InputStream fsi){
        return 0;
    }
    private void sendToOs(String s){
        try {
            outputStream.write(s.getBytes());
            outputStream.flush();
        }catch (Exception e){
            System.out.println("Exception in HttpSendData line 79: "+e.getLocalizedMessage());
        }
    }
}
