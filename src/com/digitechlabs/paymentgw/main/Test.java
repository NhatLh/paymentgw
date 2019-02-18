package com.digitechlabs.paymentgw.main;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Test {

    public static void main(String[] args) {
        long id = System.currentTimeMillis();
        String stringID = Long.toString(id, 36);

        System.out.println(stringID.toUpperCase());

        System.out.println(Character.MAX_RADIX);
        
        
        int a = -1000;
        
        int b = Math.abs(a);
                
        System.out.println(b);
    }

    public static Map<String, List<String>> splitQuery(String uri) throws UnsupportedEncodingException {
        if (uri.contains("?")) {
            uri = uri.split("\\?")[1];
        }
        final Map<String, List<String>> query_pairs = new LinkedHashMap<>();
        final String[] pairs = uri.split("&");
        for (String pair : pairs) {
            final int idx = pair.indexOf("=");
            final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
            if (!query_pairs.containsKey(key)) {
                query_pairs.put(key, new LinkedList<>());
            }
            final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
            query_pairs.get(key).add(value);
        }
        return query_pairs;
    }
}
