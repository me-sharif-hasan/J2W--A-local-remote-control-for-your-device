package me.iishanto.http;

import java.util.HashMap;
import java.util.Map;

public class HttpParser {
    private final String[] lines;
    public Map<String, String> _Get = new HashMap<>();
    public Map<String, String> _Header = new HashMap<>();

    public HttpParser(String header) {
        lines = explode(header);
        parseHeaders();
        parseGet();
    }

    private String[] explode(String data) {
        return data.split("\n");
    }

    private String get(int i) {
        return lines[i];
    }

    private void parseHeaders() {
        for (int i = 1; i < lines.length; i++) {
            String[] s = lines[i].split(":", 2);
            if (s.length >= 2) {
                _Header.put(s[0], s[1]);
            }
        }
    }

    public void parseGet() {
        String[] span = get(0).split(" ");
        StringBuilder param = new StringBuilder();
        if (span[0].equals("GET") && span[1].length() != 1) {
            for (int i = 2; i < span[1].length(); i++) param.append(span[1].charAt(i));
            String[] params = param.toString().split("&");
            for (String s : params) {
                String[] ln = s.split("=");
                if (ln.length < 2) continue;
                _Get.put(ln[0], ln[1]);
            }
        }
    }
}
