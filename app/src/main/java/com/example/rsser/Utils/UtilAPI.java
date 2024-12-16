package com.example.rsser.Utils;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import java.util.List;
import java.util.Map;

public class UtilAPI {
    private static final String TAG = "UtilAPI";
    private XmlParserWithSAX xmlParserWithSAX;

    public void ParseXml(String url) {
        try {
            Long start = System.currentTimeMillis();
            Http http = new Http();
            String xmlContent = http.fetchOptimized(url);
            Long end = System.currentTimeMillis();
            System.out.println("Fetch takes : " + (end - start));

            // 增加非空检查
            if (xmlContent == null || xmlContent.trim().isEmpty()) {
                Log.e(TAG, "XML内容为空");
                return;
            }

            xmlParserWithSAX = new XmlParserWithSAX();
            xmlParserWithSAX.parseXml(xmlContent);
        } catch (Exception e) {
            Log.e(TAG, "解析XML出错", e);
        }
    }

    public Map<String, String> getSourceInfo() {
        if (xmlParserWithSAX == null) {
            Log.w(TAG, "xmlParserWithSAX is null");
            return null;
        }
        return xmlParserWithSAX.rssSource;
    }
    public List<Map<String, String>> getItemInfo() {
        return xmlParserWithSAX.items;
    }
}