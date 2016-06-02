package com.wuwind.corelibrary.utils;

import java.security.MessageDigest;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class Md5 {

    public static String Md5(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] data = digest.digest(str.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.length; i++) {
                String result = Integer.toHexString(data[i] & 0xff);
                String temp = null;
                if (result.length() == 1) {
                    temp = "0" + result;
                } else {
                    temp = result;
                }
                sb.append(temp);
            }
            return sb.toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, String> sortMap = new TreeMap<String, String>(
                new MapKeyComparator());

        sortMap.putAll(map);

        return sortMap;
    }

    static class MapKeyComparator implements Comparator<String> {

        @Override
        public int compare(String str1, String str2) {

            return str1.compareTo(str2);
        }
    }
}
