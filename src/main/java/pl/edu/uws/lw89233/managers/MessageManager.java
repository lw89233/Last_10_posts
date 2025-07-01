package pl.edu.uws.lw89233.managers;

import java.util.HashMap;

public final class MessageManager {

    private final HashMap<String, String> requestDict;

    public MessageManager(String request) {
        requestDict = parseRequest(request);
    }

    public HashMap<String, String> parseRequest(String request) {
        String[] requestPairs = request.split("#");
        HashMap<String, String> requestDictTemp = new HashMap<>();

        for (String requestPair : requestPairs) {
            String[] keyValuePair = requestPair.split(":", 2);
            if (keyValuePair.length == 2) {
                requestDictTemp.put(keyValuePair[0], keyValuePair[1]);
            }
        }

        return requestDictTemp;
    }

    public String getAttribute(String key) {
        return requestDict.getOrDefault(key, null);
    }
}
