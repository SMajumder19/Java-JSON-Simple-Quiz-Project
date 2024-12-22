import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class UserCreation {
    public static void main(String[] args) throws IOException, ParseException {
        String filePath = "./src/main/resources/users.json";
        createAdmin("admin", "1234", filePath);
        createStudent("salman", "1234", filePath);
    }

    public static void createAdmin(String username, String password, String filePath) throws IOException, ParseException {
        FileReader reader = new FileReader(filePath);
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(reader);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("password", password);
        jsonObject.put("role", "admin");
        jsonArray.add(jsonObject);

        FileWriter writer = new FileWriter(filePath);
        writer.write(jsonArray.toJSONString());
        writer.flush();
        writer.close();

        System.out.println("Admin user created successfully!");
        reader.close();
    }

    public static void createStudent(String username, String password, String filePath) throws IOException, ParseException {
        FileReader reader = new FileReader(filePath);
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(reader);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("password", password);
        jsonObject.put("role", "student");
        jsonArray.add(jsonObject);

        FileWriter writer = new FileWriter(filePath);
        writer.write(jsonArray.toJSONString());
        writer.flush();
        writer.close();

        System.out.println("Student user created successfully!");
        reader.close();
    }
}
