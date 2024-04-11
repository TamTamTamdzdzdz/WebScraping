package org.example;
import org.json.simple.JSONArray;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;

public abstract class WebScraper {
    private final String userAgent = "Mozilla/5,0(Window NT 10.0; Win64; x64) AppleWebKit/537.36(KHTML,like Gecko) Chrome/107.0.0.0 Safari/537.36";
    private Document document;
    public Document getDocument(){
        return document;
    }

    public void connectUrl(String url){
        try {
            document = Jsoup.connect(url).userAgent(userAgent).get();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public void connectHtml(String html){
        document = Jsoup.parse(html);
    }
    public Elements selectQuery(String query){
        return document.select(query);
    }
    public void writeFile(String fileName, JSONArray jsonArray){
        try{
        FileWriter fileWriter = new FileWriter(fileName);
        fileWriter.write(jsonArray.toJSONString());
        fileWriter.flush();
        }
        catch (IOException ioException){
            ioException.printStackTrace();
        }
    }
    public abstract boolean nextWeb(String nextUrl);
}
