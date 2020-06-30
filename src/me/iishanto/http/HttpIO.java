package me.iishanto.http;

import me.iishanto.Toolkit;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpIO {
    private final OutputStream outputStream;
    private InputStream inputStream;
    boolean websocket=false;
    private String headers="";
    private String contentType="";
    private final Map<java.lang.constant.Constable, Serializable> headerList= new HashMap<>();

    public HttpIO(InputStream is, OutputStream os){
        outputStream=os;
        inputStream=is;
    }
    public HttpIO(OutputStream os){outputStream=os;}
    public void setAsWebsocket(String Sec_WebSocket_Key){
        if (Sec_WebSocket_Key == null) {
            return;
        }
        if (Sec_WebSocket_Key.charAt(0) == ' ') {
            Sec_WebSocket_Key = Sec_WebSocket_Key.substring(1);
        }
        if (Sec_WebSocket_Key.charAt(Sec_WebSocket_Key.length() - 1) == ' ') {
            Sec_WebSocket_Key = Sec_WebSocket_Key.substring(0, Sec_WebSocket_Key.length() - 1);
        }
        Toolkit.getInstance().calculate(Sec_WebSocket_Key);
        websocket=true;
    }

    private void setHeaders(){
        StringBuilder stringBuilder=new StringBuilder();
        if(websocket){
            stringBuilder.append("HTTP/1.1 101 Switching Protocols\r\n");
            stringBuilder.append("Upgrade: websocket\r\n");
            stringBuilder.append("Connection: Upgrade\r\n");
            stringBuilder.append("Sec-WebSocket-Accept: ").append(Toolkit.getInstance().getWsKey());
            stringBuilder.append("\r\n\r\n");
        }else{
            Iterator<Map.Entry<java.lang.constant.Constable, Serializable>> it = headerList.entrySet().iterator();
            stringBuilder.append("HTTP/1.1 200 OK\r\n");
            while (it.hasNext()) {
                Map.Entry<java.lang.constant.Constable, Serializable> pair = it.next();
                stringBuilder.append(pair.getKey()).append(":").append(pair.getValue()).append("\r\n");
                it.remove(); // avoids a ConcurrentModificationException
            }
            stringBuilder.append("\r\n");
        }
        headers=stringBuilder.toString();
    }
    public void sendHeaders(){
        setHeaders();
        sendToOs(headers.getBytes());
    }
    public void closeOutputStream(){
        try {
            outputStream.close();
        } catch (IOException e) {
            System.out.println("Exception in HttpSendData line 50: "+e.getLocalizedMessage());
        }
    }
    public void prepareHeaders(String type,String value){
        headerList.put(type,value);
    }
    public void prepareHeaders(String type,long value){
        headerList.put(type,value);
    }
    public void send(URL url){
        File file=Toolkit.getInstance().URL2File(url);
        try {
            byte []buff=new FileInputStream(file).readAllBytes();
            if (contentType.equals("")) {
                try {
                    contentType = Toolkit.getInstance().getFileMimeType(url);
                    prepareHeaders("Content-Type",contentType);
                    prepareHeaders("Content-Length",buff.length);
                } catch (Exception e) {
                    System.out.println("Exception in HttpSendData line 49: " + e.getLocalizedMessage());
                }
            }
            sendHeaders();
            sendToOs(buff);
        }catch (Exception e){
            System.out.println("Exception in HttpSendData line 77: "+e.getLocalizedMessage());
        }
    }
    public void sendToOs(byte []buff){
        sendBinary(buff);
        flush();
    }
    public boolean sendBinary(byte []buff) {
        try {
            outputStream.write(buff);
            Toolkit.getInstance().dataTransfer(buff.length);
            return true;
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
        return false;
    }
    public void flush(){
        try {
            outputStream.flush();
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
    public long getData(byte[] buff){
        try {
            return inputStream.read(buff);
        }catch (Exception e){
            System.err.println(e.getLocalizedMessage());
        }
        return -1;
    }
    public InputStream getInputStream(){
        return inputStream;
    }
    public OutputStream getOutputStream(){
        return outputStream;
    }
}
