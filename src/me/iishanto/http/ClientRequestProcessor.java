package me.iishanto.http;

import me.iishanto.Toolkit;
import me.iishanto.Capture.ScreenCapture;
import me.iishanto.system.EventProcessor;

import java.io.*;
import java.net.URL;
import java.util.Scanner;

public class ClientRequestProcessor {
    HttpParser httpParser;
    HttpIO httpIO;
    public ClientRequestProcessor(InputStream iStream, OutputStream oStream) {
        httpIO =new HttpIO(iStream,oStream);
        parseRequest();
    }

    private void parseRequest() {
        Scanner scanner = new Scanner(httpIO.getInputStream());
        StringBuilder header = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine() + "\n";
            header.append(line);
            if (line.length() == 1) break;
        }
        httpParser = new HttpParser(header.toString());
    }

    public void start() {
        if (httpParser._Get.get("get") != null) {
            if (httpParser._Get.get("get").equals("live")) {
                new Thread(new ScreenCapture(httpIO.getOutputStream())).start();
            } else if (httpParser._Get.get("get").equals("file")) {
                sendData(Toolkit.getInstance().getResource("/html/"+ httpParser._Get.get("link")));
            } else if (httpParser._Get.get("get").equals("ws")) {
                ws();
            }
        } else {
            sendData(Toolkit.getInstance().getResource("/html/index.html"));
        }
    }

    private void ws() {
        try {
            String wsk = httpParser._Header.get("Sec-WebSocket-Key");
            httpIO.setAsWebsocket(wsk);
            httpIO.sendHeaders();
            /*
            @Todo: we may make a way to send feedback response to the client
             */
            new Thread(new EventProcessor(httpIO)).start();

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    private void sendData(URL file){
        httpIO.send(file);
        httpIO.closeOutputStream();
    }
}
