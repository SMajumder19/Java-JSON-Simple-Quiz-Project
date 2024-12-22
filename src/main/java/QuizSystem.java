import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class QuizSystem {
    public static void main(String[] args) throws IOException, ParseException {
        startSystem();
    }

    public static void startSystem() throws IOException, ParseException {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter your username");
        String username = sc.nextLine();
        if(checkUsername(username) == false){
            System.out.println("No user by this name. Please try again by restarting the system..");
            return;
        }

        System.out.println("Enter your password");
        String password = sc.nextLine();
        if(checkPassword(password) == false){
            System.out.println("Wrong Password. Please try again by restarting the system.");
            return;
        }

        if(checkRole(username, password).equals("admin")){
            startAdmin(username);
        } else if(checkRole(username, password).equals("student")){
            startStudent(username);
        } else {
            System.out.println("No suitable role found for this user. Please try again for another user by restarting the system.");
        }
    }


    public static boolean checkUsername(String username) throws IOException, ParseException {
        String filePath = "./src/main/resources/users.json";
        FileReader reader = new FileReader(filePath);
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(reader);

        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            String name = jsonObject.get("username").toString();
            if(name.equals(username)){
                return true;
            }
        }

        reader.close();
        return false;
    }

    public static boolean checkPassword(String password) throws IOException, ParseException {
        String filePath = "./src/main/resources/users.json";
        FileReader reader = new FileReader(filePath);
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(reader);

        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            String pass = jsonObject.get("password").toString();
            if(pass.equals(password)){
                return true;
            }
        }

        reader.close();
        return false;
    }

    public static String checkRole(String username, String password) throws IOException, ParseException {
        String filePath = "./src/main/resources/users.json";
        FileReader reader = new FileReader(filePath);
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(reader);

        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            String name = jsonObject.get("username").toString();
            if(name.equals(username)){
                String pass = jsonObject.get("password").toString();
                if(pass.equals(password)){
                    String role = jsonObject.get("role").toString();
                    return role;
                }
            }
        }
        reader.close();
        return "No role found";
    }

    public static void startAdmin(String username) throws IOException, ParseException {
        System.out.println("Welcome " + username + "! Please create new questions in the question bank.");
        String filePath = "./src/main/resources/quiz.json";

        boolean flag = true;
        while(flag){
            flag = addQuestion(filePath);
        }
    }

    public static boolean addQuestion(String filePath) throws IOException, ParseException {
        Scanner sc = new Scanner(System.in);
        FileReader reader = new FileReader(filePath);
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(reader);

        boolean flag = true;
        JSONObject jsonObject = new JSONObject();

        System.out.println("Input your question");
        //https://www.javatpoint.com/software-testing-mcq [Added from here]
        String question = sc.nextLine();
        jsonObject.put("question", question);

        System.out.println("Input option 1:");
        String option = sc.nextLine();
        jsonObject.put("option 1", option);
        System.out.println("Input option 2:");
        option = sc.nextLine();
        jsonObject.put("option 2", option);
        System.out.println("Input option 3:");
        option = sc.nextLine();
        jsonObject.put("option 3", option);
        System.out.println("Input option 4:");
        option = sc.nextLine();
        jsonObject.put("option 4", option);

        System.out.println("What is the answer key?");
        String answer = sc.nextLine();
        jsonObject.put("answerkey", Integer.parseInt(answer));

        jsonArray.add(jsonObject);
        FileWriter writer = new FileWriter(filePath);
        writer.write(jsonArray.toJSONString());
        writer.flush();
        writer.close();
        System.out.println("Saved successfully! Do you want to add more questions? (press s for start and q for quit)");
        String str = sc.nextLine();
        if(str.equals("s")){
            flag = true;
        } else if(str.equals("q")){
            flag = false;
        } else {
            System.out.println("Invalid input is not accepted in this case. Please try again by restarting the system.");
            flag = false;
        }

        reader.close();
        return flag;
    }

    public static void startStudent(String username) throws IOException, ParseException {
        System.out.println("Welcome " + username + " to the quiz! We will throw you 10 questions. Each MCQ mark is 1 and no negative marking. Are you ready? Press 's' to start.");
        String filePath = "./src/main/resources/quiz.json";

        Scanner sc = new Scanner(System.in);
        String more = sc.nextLine();
        if(more.equals("s")){
            startQuiz(filePath);
        } else {
            System.out.println("Invalid input is not accepted in this case. Please try again by restarting the system.");
        }
    }

    public static void startQuiz(String filePath) throws IOException, ParseException {
        Scanner sc = new Scanner(System.in);
        Random random = new Random();
        FileReader reader = new FileReader(filePath);
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(reader);
        
        boolean flag = true;
        while(flag){
            int marks = 0;
            String input = "";
            int answer = -1;
            int convert = -1;
            for(int i = 0; i < 10; i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(random.nextInt(jsonArray.size()));
                System.out.println("[Question " + (i+1) + "] " + jsonObject.get("question").toString());
                System.out.println("1. " + jsonObject.get("option 1").toString());
                System.out.println("2. " + jsonObject.get("option 2").toString());
                System.out.println("3. " + jsonObject.get("option 3").toString());
                System.out.println("4. " + jsonObject.get("option 4").toString());

                input = sc.nextLine();
                try{
                    convert = Integer.parseInt(input);
                } catch (Exception e){
                    marks += 0;
                }

                answer = Integer.parseInt(jsonObject.get("answerkey").toString());

                if(answer == convert){
                    marks++;
                }
            }
            checkMarks(marks);

            System.out.println("Would you like to start again? press s for start or q for quit");
            String more = sc.nextLine();
            if(more.equals("s")){
                flag = true;
            } else if(more.equals("q")){
                flag = false;
            } else {
                System.out.println("Invalid input is not accepted in this case. Please try again by restarting the system.");
                flag = false;
            }
        }
    }

    public static void checkMarks(int marks){
        if(marks >= 8){
            System.out.println("Excellent! You have got " + marks + " out of 10");
        } else if(marks >= 5){
            System.out.println("Good. You have got " + marks + " out of 10");
        } else if(marks >= 2){
            System.out.println("Very poor! You have got " + marks + " out of 10");
        } else {
            System.out.println("Very sorry you are failed. You have got " + marks + " out of 10");
        }
    }

}
