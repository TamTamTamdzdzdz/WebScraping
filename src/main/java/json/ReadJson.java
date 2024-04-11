package json;

import org.example.Article;
import org.example.BlogCopy;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ReadJson {
    public static void main(String[] args) {
        int start = 0;
        int end = 1000;
        String BASEURL = "https://cointelegraph.com/";
        String userAgent = "Mozilla/5,0(Window NT 10.0; Win64; x64) AppleWebKit/537.36(KHTML,like Gecko) Chrome/107.0.0.0 Safari/537.36";
        try{
            String content = new String(Files.readAllBytes(Paths.get("File and category.json")));
            JSONParser parser = new JSONParser();
            JSONArray jsonArrayRead = (JSONArray)parser.parse(content);
            System.out.println(jsonArrayRead.size());
            JSONArray jsonArray = new JSONArray();
            int idx = 1;
//            for (Object jsonObject: jsonArrayRead){
            for(int i = start; i <= end; i++){
                Object jsonObject = jsonArrayRead.get(i);
                try{
                    System.out.println(idx++);
                    Article article = new Article();
                    JSONObject jsonObject1 = (JSONObject)jsonObject;
                    String url = (String)jsonObject1.get("link");
                    String link = BASEURL + url;
                    article.setArticleLink(link);
                    article.setWebsiteSource(BASEURL);
                    String category = (String)jsonObject1.get("category");
                    article.setCategory(category);
                    try{
                        Document document = Jsoup.connect(link).userAgent(userAgent).get();
                        String title = document.selectFirst(".post__title").text();
                        article.setArticleTitle(title);
                        Elements hashTag = document.select(".tags-list__item");
                        String tags = Article.combineString(hashTag, ", ");
                        String author = document.selectFirst(".post-meta__author-name").text();
                        article.setTags(tags);
                        article.setAuthor(author);
                        article.setArticleType("Blog");
                        Elements contentElements = document.select(".post-content > p, .post-content > blockquote");
                        article.setDetailedContent(Article.combineString(contentElements, " "));
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
                        article.addJsonObject(jsonArray);
                        System.out.println(url + "------" + category + "-----" + title);
                    }catch (Exception e1){
                        e1.printStackTrace();
                    }

                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }
            FileWriter fileWriter = new FileWriter("ReadJson " + start + "-" + end + ".json");
            fileWriter.write(jsonArray.toJSONString());
            fileWriter.flush();
        } catch (IOException e) {
//            throw new RuntimeException(e);
            System.out.println("IOExcept: " + e.getMessage());
        } catch (ParseException e) {
//            throw new RuntimeException(e);
            System.out.println("ParseExcept: " +e.getMessage());
        }
    }
}
