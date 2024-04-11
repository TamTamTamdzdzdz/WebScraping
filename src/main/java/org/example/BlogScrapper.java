package org.example;

import org.json.JSONObject;
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

public class BlogScrapper {
    public String getHtml(String baseUrl, int scrollDown){
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        WebDriver driver = new EdgeDriver();
        try{
            driver.get(baseUrl);
            Thread.sleep(1000);
            for (int i = 0; i < scrollDown; i++){
                ((JavascriptExecutor)driver).executeScript("window.scrollTo(0, document.body.scrollHeight - 1000)");
                Thread.sleep(2000);
                System.out.println("Iteration: " + i);
            }
            Thread.sleep(10000);
            String html = driver.getPageSource();
            driver.quit();
            FileWriter fileWriter = new FileWriter("Html.txt");
            fileWriter.write(html);
            fileWriter.flush();
            return html;

        } catch (InterruptedException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    public void writeLinkAndCategory(String html) throws IOException {
        Document document = Jsoup.parse(html);
        Elements elements = document.select(".post-card");
        System.out.println("Elements size: " + elements.size());
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
        JSONArray jsonArray = new JSONArray();
        for (Map.Entry<String, String> couple: linkCategory.entrySet()){
            String link = couple.getKey();
            String category = couple.getValue();
            try{
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("link", link);
                jsonObject.put("category", category);
                jsonArray.add(jsonObject);
            }
            catch (Exception e){
                System.out.println("Error when write file: " + e.getMessage());
            }
        }
        FileWriter fileWriter = new FileWriter("File and category.json");
        fileWriter.write(jsonArray.toJSONString());
        System.out.println("JsonArray size: " + jsonArray.size());
        fileWriter.flush();
    }
    public static void main(String[] args) throws IOException {
        String userAgent = "Mozilla/5,0(Window NT 10.0; Win64; x64) AppleWebKit/537.36(KHTML,like Gecko) Chrome/107.0.0.0 Safari/537.36";
//        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
//        WebDriver driver = new EdgeDriver();
        String baseUrl = "https://cointelegraph.com/";
        HashSet<String> urlUnique = new HashSet<>();

        BlogScrapper blogScrapper = new BlogScrapper();
        String html = blogScrapper.getHtml(baseUrl, 500);
        blogScrapper.writeLinkAndCategory(html);
//        try{
//            driver.get(baseUrl);
//            Thread.sleep(1000);
//            for (int i = 0; i < 1000; i++){
//                ((JavascriptExecutor)driver).executeScript("window.scrollTo(0, document.body.scrollHeight - 1000)");
//                Thread.sleep(5000);
//            }
//            int idx = 1;
////            List<WebElement> blogs = driver.findElements(By.cssSelector(".post-card"));
//            JSONArray jsonArray = new JSONArray();
//            String html = driver.getPageSource();
//            driver.quit();
//
//            Document document = Jsoup.parse(html);
//            Elements elements = document.select(".post-card");
//            System.out.println(elements.size());
//            LinkedHashMap<String, String> linkCategory = new LinkedHashMap<>();
//            for (Element element: elements){
//                try{
//                    String link = element.selectFirst("header > a").attr("href");
//                    String category = element.selectFirst(".post-card__figure").text();
//                    linkCategory.put(link, category);
//                }
//                catch (Exception e){
//                    System.out.println("Error when inserting hashmap: " + e.getMessage());
//                }
//            }
//            for (Map.Entry<String, String> couple: linkCategory.entrySet()){
//                System.out.println(idx++);
//                String link = baseUrl + couple.getKey();
//                String category = couple.getValue();
//                try{
//                    JSONObject jsonObject = new JSONObject();
//                    document = Jsoup.connect(link).userAgent(userAgent).get();
//                    String title = document.selectFirst(".post__title").text();
//                    System.out.println(title);
//                    // Tags
//                    Elements hashTag = document.select(".tags-list__item");
//                    StringBuffer tagBuffer = new StringBuffer();
//                    StringBuffer separateTag = new StringBuffer(", ");
//                    for (Element htag: hashTag) {
//                        tagBuffer.append(new StringBuffer(htag.text().substring(1))).append(separateTag);
//                    }
//                    String tags = separateTag.toString();
//                    String author = document.selectFirst(".post-meta__author-name").text();
//                    // Date
//                    String date = document.selectFirst(".post-meta__publish-date").text();
//                    if (date.contains("hour")) {
//                        int hour = Integer.parseInt(date.substring(0, date.indexOf(" ")));
//                        LocalDateTime now = LocalDateTime.now();
//                        LocalDateTime dateFormat = now.minus(hour, ChronoUnit.HOURS);
//                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
//                        date = dateFormat.format(dateTimeFormatter);
//                    } else if (date.contains("minute")) {
//                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
//                        date = LocalDateTime.now().format(dateTimeFormatter);
//                    }
//                    String type = "Blog";
//                    String summary = "";
//                    StringBuffer contentBuffer = new StringBuffer();
//                    StringBuffer separateContent = new StringBuffer(" ");
//                    Elements contentElements = document.select(".post-content > p, .post-content > blockquote");
//                    for (Element contentElement : contentElements) {
//                        contentBuffer.append(new StringBuffer(contentElement.text())).append(separateContent);
//                    }
//                    String content = contentBuffer.toString();
//                    jsonObject.put("author", author);
//                    jsonObject.put("title", title);
//                    jsonObject.put("content", content);
//                    jsonObject.put("tags", tags);
//                    jsonObject.put("type", type);
//                    jsonObject.put("date", date);
//                    jsonObject.put("category", category);
//                    jsonObject.put("source", baseUrl);
//                    jsonObject.put("link", link);
//                    jsonObject.put("summary", summary);
//                    jsonArray.add(jsonObject);
//
//                }
//                catch (Exception e1){
//                    System.out.println("Error when connecting Jsoup: " + e1.getMessage());
//                }
//            }
//            FileWriter fileWriter = new FileWriter("Blog.json");
//            fileWriter.write(jsonArray.toJSONString());
//            fileWriter.flush();
//
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
    }
}