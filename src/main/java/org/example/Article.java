package org.example;

import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Article {
    private String articleLink = "";
    private String websiteSource = "";
    private String articleType = "";
    private String articleSummary = "";
    private String articleTitle = "";
    private String detailedContent = "";
    private String date = "";
    private String tags = "";
    private String author = "";
    private String category = "";
    private JSONObject jsonObject = new JSONObject();
    public static String userAgent = "Mozilla/5,0(Window NT 10.0; Win64; x64) AppleWebKit/537.36(KHTML,like Gecko) Chrome/107.0.0.0 Safari/537.36";
    public String getArticleLink() {
        return articleLink;
    }
    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }
    public String getWebsiteSource() {
        return websiteSource;
    }
    public void setWebsiteSource(String websiteSource) {
        this.websiteSource = websiteSource;
    }
    public String getArticleType() {
        return articleType;
    }
    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }
    public String getArticleSummary() {
        return articleSummary;
    }
    public void setArticleSummary(String articleSummary) {
        this.articleSummary = articleSummary;
    }
    public String getArticleTitle() {
        return articleTitle;
    }
    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }
    public String getDetailedContent() {
        return detailedContent;
    }
    public void setDetailedContent(String detailedContent) {
        this.detailedContent = detailedContent;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTags() {
        return tags;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void addJsonObject(JSONArray jsonArray){
        jsonObject.put("author", author);
        jsonObject.put("title", articleTitle);
        jsonObject.put("content", detailedContent);
        jsonObject.put("tags", tags);
        jsonObject.put("type", articleType);
        jsonObject.put("date", date);
        jsonObject.put("category", category);
        jsonObject.put("source", websiteSource);
        jsonObject.put("link", articleLink);
        jsonObject.put("summary", articleSummary);
        jsonArray.add(jsonObject);
    }
    public static String combineString(Elements elements, String separator){
        StringBuffer resultBuffer = new StringBuffer();
        StringBuffer separateBuffer = new StringBuffer(separator);
        int n = elements.size();
        if (n == 0){
            return "";
        }
        for (int i = 0; i < n - 1; i++){
            Element element = elements.get(i);
            resultBuffer.append(new StringBuffer(element.text())).append(separateBuffer);
        }
        resultBuffer.append(new StringBuffer(elements.last().text()));
        return resultBuffer.toString();
    }

}
