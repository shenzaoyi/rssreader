package com.example.rsser.Utils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlParserWithSAX {
    public Map<String, String> rssSource; // 存储基础的 RSS 源信息
    public List<Map<String, String>> items; // 存储条目信息

    public void parseXml(String xml) {
        try {
            xml = xml.trim();
            if (xml.endsWith("null")) {
                xml = xml.substring(0, xml.length() - 4).trim(); // 去掉尾部的 "null"
            }

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            rssSource = new HashMap<>();
            items = new ArrayList<>();

            DefaultHandler handler = new DefaultHandler() {
                private Map<String, String> currentItem = null;
                private StringBuilder currentValue = new StringBuilder();
                private boolean inChannel = false; // 是否处于 <channel> 标签
                private boolean inItem = false; // 是否处于 <item> 标签

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    currentValue.setLength(0); // 每次进入新元素时清空字符缓存
                    if (qName.equalsIgnoreCase("channel")) {
                        inChannel = true; // 开始解析 <channel>
                    } else if (qName.equalsIgnoreCase("item")) {
                        inItem = true; // 开始解析 <item>
                        currentItem = new HashMap<>(); // 初始化当前条目
                    }
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (inChannel && !inItem) {
                        // 当在 <channel> 中，但不在 <item> 中，解析 RSS 源信息
                        if (qName.equalsIgnoreCase("title") || qName.equalsIgnoreCase("link") || qName.equalsIgnoreCase("description")) {
                            rssSource.put(qName.toLowerCase(), extractLimitedText(currentValue.toString(), qName.equalsIgnoreCase("description") ? 50 : Integer.MAX_VALUE));
                        }
                        if (qName.equalsIgnoreCase("channel")) {
                            inChannel = false; // 结束 <channel>
                        }
                    } else if (inItem) {
                        // 当在 <item> 中，解析条目的信息
                        if (qName.equalsIgnoreCase("title") || qName.equalsIgnoreCase("link") || qName.equalsIgnoreCase("description")) {
                            currentItem.put(qName.toLowerCase(), currentValue.toString());
                        }
                        if (qName.equalsIgnoreCase("item")) {
                            items.add(currentItem); // 当前条目解析完成，添加到列表
                            currentItem = null; // 重置当前条目
                            inItem = false; // 结束 <item>
                        }
                    }
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    currentValue.append(ch, start, length); // 收集字符数据
                }

                // 提取指定长度的文本，超过长度时进行截断
                private String extractLimitedText(String text, int maxLength) {
                    text = text.trim(); // 去掉两端空格
                    return text.length() > maxLength ? text.substring(0, maxLength) : text;
                }
            };

            // 解析 XML 字符串
            saxParser.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")), handler);

        } catch (Exception e) {
            System.out.println("XML Parsing Error: ");
            e.printStackTrace();
        }
    }

    public  void outputToFile() throws IOException {
        // 结果 rssSource & items
        // 存储到文件
        String outputPath = "output.txt";
        PrintWriter writer = new PrintWriter(new FileWriter(outputPath));

        // 写入 RSS 源信息
        writer.println("RSS Source Information:");
        for (Map.Entry<String, String> entry : this.rssSource.entrySet()) {
            writer.println("==========================");
            writer.println(" " + entry.getKey() + ": " + entry.getValue());
            writer.println("==========================");
        }

        // 写入 Item 信息
        writer.println("\nItems:");
        for (Map<String, String> item : this.items) {
            for (Map.Entry<String, String> entry : item.entrySet()) {
                writer.println("==========================");
                writer.println(" " + entry.getKey() + ": " + entry.getValue());
                writer.println("==========================");
            }
        }
        writer.close(); // 关闭写入器
    }
}