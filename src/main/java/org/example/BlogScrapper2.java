package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashSet;

import org.json.simple.JSONArray;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class BlogScrapper2{
    public static void main(String[] args) throws IOException {

        final String BASE_URL = "https://101blockchains.com/blog/";
        String userAgent = Article.userAgent;
        Document docs = Jsoup.connect(BASE_URL).userAgent(userAgent).get();
        JSONArray jsonArray = new JSONArray();
        Elements topics = docs.select("li.cat-item.cat-item").select("a");
        int idx = 1;

        for (int i = 0; i < topics.size(); i++) {
            String nextUrl = topics.get(i).attr("href");
            boolean check = true;
            while (check) {
                check = false;
                Document doc = Jsoup.connect(nextUrl).userAgent(userAgent).get();
                Elements news = doc.select(".entry-title").select("a");
                LinkedHashSet<String> uniqueUrls = new LinkedHashSet<>();
                for (Element n : news) {
                    String newUrl = n.attr("href");
                    uniqueUrls.add(newUrl);
                }
                for (String url : uniqueUrls) {
                    try {
                        Article article = new Article();
                        Document d = Jsoup.connect(url).userAgent(userAgent).get();
                        System.out.println(idx);
                        idx++;
                        String link = url;
                        String source = BASE_URL;
                        String type = "Blog";
                        String title = d.select(".pho-main-heading").text();
                        System.out.println(title);
                        // Select content which is not copy box
                        Elements text = d.select(".post-content > p, .post-content > h1, .post-content > h2, .post-content > h3, .post-content > h4, .post-content > h5, .post-content > h6");
                        StringBuffer contentBuffer = new StringBuffer();
                        for (Element subText : text) {
                            if (!subText.text().contains("<span")) { // Remove video
                                StringBuffer add = new StringBuffer(subText.text()).append(new StringBuffer(" "));
                                contentBuffer.append(add);
                            }
                        }
                        String content = contentBuffer.toString();                        
                        String tags = "";
                        Elements conclusion = new Elements();
                        Elements h3 = d.select(".post-content > h3");                        
                        if (h3.size() > 0) {
                            Element h3Last = h3.last();
                            conclusion = h3Last.nextElementSiblings();
                       }
                        String summary = Article.combineString(conclusion, " ");
                        Elements info = d.selectFirst(".blog-nav-about").select("li");
                        String category = info.get(0).text();
                        String author = info.get(1).text();
                        String date = info.get(2).text().substring(3); // example: on April 7, 2024
                        System.out.println(category + " " + author + "----" + date + "---------" + summary);
                        article.addJsonObject(jsonArray);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                Element nextEle = doc.selectFirst(".next");
                if (nextEle != null) {
                    nextUrl = nextEle.attr("href");
                    check = true;
                }
            }
        }
        FileWriter fileWriter = new FileWriter("Blog3.json");
        fileWriter.write(jsonArray.toJSONString());
        fileWriter.flush();
        System.out.println(jsonArray.size());
    }
}