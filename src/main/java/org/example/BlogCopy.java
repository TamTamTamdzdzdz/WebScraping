package org.example;

import org.json.simple.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.json.simple.JSONArray;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class BlogCopy extends WebScraper{
    public boolean nextWeb(String nextUrl){
        return false;
    }
    public static void main(String[] args) throws IOException {
        BlogCopy blogCopy = new BlogCopy();
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        WebDriver driver = new EdgeDriver();
        final String BASEURL = "https://cointelegraph.com/";
        HashSet<String> urlUnique = new HashSet<>();
        try{
            driver.get(BASEURL);
            Thread.sleep(1000);
            for (int i = 0; i < 1000; i++){
                ((JavascriptExecutor)driver).executeScript("window.scrollTo(0, document.body.scrollHeight - 1000)");
                Thread.sleep(5000);
            }
            int idx = 1;
//            List<WebElement> blogs = driver.findElements(By.cssSelector(".post-card"));
            JSONArray jsonArray = new JSONArray();
            String html = driver.getPageSource();
            driver.quit();
            blogCopy.connectHtml(html);
            Document document = blogCopy.getDocument();
            Elements elements = document.select(".post-card");
            System.out.println(elements.size());
            LinkedHashMap<String, String> linkCategory = new LinkedHashMap<>();
            for (Element element: elements){
                try{
                    String link = element.selectFirst("header > a").attr("href");
                    String category = element.selectFirst(".post-card__figure").text();
                    linkCategory.put(link, category);
                }
                catch (Exception e){
                    System.out.println("Error when inserting hashmap: " + e.getMessage());
                }
            }
            for (Map.Entry<String, String> couple: linkCategory.entrySet()){
                System.out.println(idx++);
                Article article = new Article();
                String link = BASEURL + couple.getKey();
                article.setCategory(couple.getValue());
                try{

                    article.setArticleLink(link);
                    article.setWebsiteSource(BASEURL);
                    JSONObject jsonObject = new JSONObject();
                    blogCopy.connectUrl(link);
                    String title = document.selectFirst(".post__title").text();
                    System.out.println(title);
                    article.setArticleTitle(title);
                    // Tags
                    Elements hashTag = document.select(".tags-list__item");
                    String tags = Article.combineString(hashTag, ", ");
                    String author = document.selectFirst(".post-meta__author-name").text();
                    // Date
                    String date = document.selectFirst(".post-meta__publish-date").text();
                    if (date.contains("hour")) {
                        int hour = Integer.parseInt(date.substring(0, date.indexOf(" ")));
                        LocalDateTime now = LocalDateTime.now();
                        LocalDateTime dateFormat = now.minus(hour, ChronoUnit.HOURS);
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
                        date = dateFormat.format(dateTimeFormatter);
                    } else if (date.contains("minute")) {
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
                        date = LocalDateTime.now().format(dateTimeFormatter);
                    }
                    article.setDate(date);
                    article.setArticleType("Blog");
                    Elements contentElements = document.select(".post-content > p, .post-content > blockquote");
                    article.setDetailedContent(Article.combineString(contentElements, " "));
                    article.addJsonObject(jsonArray);

                }
                catch (Exception e1){
                    System.out.println("Error when connecting Jsoup: " + e1.getMessage());
                }
            }
            blogCopy.writeFile("BlogTest.json", jsonArray);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}