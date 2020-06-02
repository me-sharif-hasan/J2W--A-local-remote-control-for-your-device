package me.iishanto;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Base64;

public class Toolkit {
    private static Toolkit instance=null;
    private String wshash="258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
    private String key=null;
    private String wsPrimary=null;
    private int serverPort=8080;
    private final String dir=System.getProperty("user.dir")+"/src/me";

    private Toolkit(){
        //
    }

    public void calculate(String primary){
        try {
            wsPrimary = primary;
            this.key=Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1").digest((wsPrimary + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes(StandardCharsets.UTF_8)));
        }catch(Exception e){
            System.out.println(e.getLocalizedMessage()+" exception in Toolkit.java: "+24);
        }
        }

    public String getWsKey(){return this.key; }
    public int getPort(){
        return serverPort;
    }
    public void setPort(int port){serverPort=port;}
    public String getDir(){
        return dir;
    }

    public void alert(String s){
        JOptionPane.showMessageDialog(null,s);
    }

    /*
    @This method is for decoding the web socket encrypted message
    @Todo: Need to write a method to encode a message, and we have to make both method more efficient
     */
    public byte[] decodeWs(byte []data,int len){
        byte []sk=new byte[4];
        byte []wse=new byte[1024];
        byte []decode=new byte[1024];
        int tl=0;
        for(int i=2;i<6;i++){
            sk[tl]=data[i];
            tl++;
        }
        tl=0;
        for(int i=6;i<len;i++){
            wse[tl]=data[i];
            tl++;
        }
        for(int i=0;i<tl;i++){
            decode[i]=(byte)(wse[i]^sk[i&0x3]);
        }
        return decode;
    }

    public URL getResource(String s){
        return this.getClass().getResource(s);
    }
    public File URL2File(URL fr){
        File temp=null;
        try {
            InputStream is=fr.openStream();
            temp=File.createTempFile(String.valueOf(System.currentTimeMillis()),".tmp");
            OutputStream os=new FileOutputStream(temp);
            os.write(is.readAllBytes());
            temp.deleteOnExit();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public String getFileMimeType(URL fr){
        String type="";
        try {
            type=Files.probeContentType(Paths.get(fr.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return type;
    }
    public InputStream getResourceStream(URL fr){
        InputStream is=null;
        try {
            is=fr.openStream();
        }catch (Exception e){
            e.printStackTrace();
        }
        return is;
    }
    public File getCursor(){
        URL cursorUrl=getResource("/html/cursor.png");
        return URL2File(cursorUrl);
    }
    public ImageIcon icon(){
        byte []iconBytes=null;
        try {
            URL iconURL=getResource("/icon.png");
            iconBytes=getResourceStream(iconURL).readAllBytes();
        }catch (Exception e){

        }
        return new ImageIcon(iconBytes);
    }
    public static Toolkit getInstance(){
        if(instance==null) {
            instance = new Toolkit();
        }
        return instance;
    }
}
