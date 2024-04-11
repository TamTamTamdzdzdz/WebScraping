package json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;


public class WriteJson {
    public static void main(String[] args) throws IOException {

        String[]name = {"Tam", "Nam"};
        int[]age = {20, 35};
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 2; i++){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("age", age[i]);
            jsonObject.put("name", name[i]);
            jsonArray.add(jsonObject);
        }
        FileWriter fileWriter = new FileWriter("output.json");
        fileWriter.write(jsonArray.toJSONString());
        fileWriter.flush();
        try{
            String content = new String(Files.readAllBytes(Paths.get("output.json")));
            JSONParser parser = new JSONParser();
            JSONArray jsonArray1 = (JSONArray)parser.parse(content);
            for (Object jsonObject: jsonArray1){
                try{
                    JSONObject jsonObject1 = (JSONObject)jsonObject;
                    System.out.println(jsonObject1.get("name"));
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
