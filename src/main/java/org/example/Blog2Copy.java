package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashSet;

import org.json.simple.JSONArray;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Blog2Copy extends WebScraper{
    public boolean nextWeb(String nextUrl){
        Document document = getDocument();
        Element nextEle = document.selectFirst(".next");
        if (nextEle != null) {
            nextUrl = nextEle.attr("href");
            return true;
        }
        return false;

    }
    public static void main(String[] args) throws IOException {
        Blog2Copy blog2Copy = new Blog2Copy();
        final String BASE_URL = "https://101blockchains.com/blog/";
        blog2Copy.connectUrl(BASE_URL);
        Document docs = blog2Copy.getDocument();
        JSONArray jsonArray = new JSONArray();
        Elements topics = docs.select("li.cat-item.cat-item").select("a");
        int idx = 1;

        for (int i = 0; i < 1; i++) {
            String nextUrl = topics.get(i).attr("href");
            boolean check = true;
            while (check) {
                check = false;
                blog2Copy.connectUrl(nextUrl);
                Document doc = blog2Copy.getDocument();
                Elements news = doc.select(".entry-title").select("a");
                LinkedHashSet<String> uniqueUrls = new LinkedHashSet<>();
                for (Element n : news) {
                    String newUrl = n.attr("href");
                    uniqueUrls.add(newUrl);
                }
                for (String url : uniqueUrls) {
                    try {
                        Article article = new Article();
                        blog2Copy.connectUrl(url);
                        Document d = blog2Copy.getDocument();
                        System.out.println(idx);
                        idx++;
                        article.setArticleLink(url);
                        article.setWebsiteSource(BASE_URL);
                        article.setArticleType("Blog");
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
                        article.setDetailedContent(contentBuffer.toString());
                        Elements conclusion = new Elements();
                        Elements h3 = d.select(".post-content > h3");
                        if (h3.size() > 0) {
                            Element h3Last = h3.last();
                            conclusion = h3Last.nextElementSiblings();
                        }
                        article.setArticleSummary(Article.combineString(conclusion, " "));
                        Elements info = d.selectFirst(".blog-nav-about").select("li");
                        article.setCategory(info.get(0).text());
                        article.setAuthor(info.get(1).text());
                        article.setDate(info.get(2).text().substring(3));// example: on April 7, 2024
                        article.addJsonObject(jsonArray);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                check = blog2Copy.nextWeb(nextUrl);
            }
        }
        blog2Copy.writeFile("Blog2Test.json", jsonArray);
    }
}