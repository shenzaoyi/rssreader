package com.example.rsser.Utils;

import com.example.rsser.DAO.Source;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

// 根据 URL 获取xml
public class Http {
    public String fetchOptimized(String urlString) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);  // 连接超时
            connection.setReadTimeout(5000);     // 读取超时

            try (BufferedReader bufferedReader =
                         new BufferedReader(new InputStreamReader(connection.getInputStream()))) {

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
            }
            connection.disconnect();
        } catch (Exception e) {
            System.err.println("Fetch Error: " + e.getMessage());
        }
        return stringBuilder.toString();
    }
}