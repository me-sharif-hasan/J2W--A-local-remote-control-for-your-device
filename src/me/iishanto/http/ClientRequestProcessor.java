package me.iishanto.http;

import me.iishanto.Toolkit;
import me.iishanto.image.Capture;
import me.iishanto.system.EventProcessor;

import java.io.*;
import java.util.Scanner;

public class ClientRequestProcessor {
    InputStream inputStream;
    OutputStream outputStream;
    HttpParser httpParser;
    HttpIO httpSendData;
    public ClientRequestProcessor(InputStream iStream, OutputStream oStream) {
        inputStream = iStream;
        outputStream = oStream;
        httpSendData=new HttpIO(inputStream,outputStream);
        parseRequest();
    }

    private void parseRequest() {
        Scanner scanner = new Scanner(inputStream);
        String header = "";
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine() + "\n";
            header += line;
            if (line.length() == 1) break;
        }
        httpParser = new HttpParser(header);
    }

    public void start() {
        if (httpParser._Get.get("get") != null) {
            if (httpParser._Get.get("get").equals("live")) {
                new Thread(new Capture(outputStream)).start();
            } else if (httpParser._Get.get("get").equals("file")) {
                sendData(Toolkit.getInstance().getDir() + "/html/" + (String) httpParser._Get.get("link"));
            } else if (httpParser._Get.get("get").equals("ws")) {
                ws("text/html");
            }
        } else {
            sendData(Toolkit.getInstance().getDir() + "/html/index.html");
        }
    }

    private void ws(String type) {
        try {
            String wsk = (String) httpParser._Header.get("Sec-WebSocket-Key");
            httpSendData.setAsWebsocket(wsk);
            httpSendData.sendHeaders();
            /*
            @Todo: we may make a way to send feedback response to the client
             */
            new Thread(new EventProcessor(inputStream,outputStream)).start();

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }


    private void sendData(String link) {
        try {
            httpSendData.send(new File(link));
            httpSendData.closeOutputStream();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage() + " exception in SystemController.java: " + 92);
            Thread.currentThread().interrupt();
        }
    }
}
