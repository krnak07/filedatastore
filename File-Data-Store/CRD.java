import org.json.simple.JSONObject;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CRD implements fileDataStore{
    private String path;
    private File fileObj;
    private static boolean isWriting = false;
    private static boolean isDeleting = false;

    private void setFile() {
        try {
            fileObj = new File(path);
            if (fileObj.createNewFile()) {

            } else {
                if(fileObj.length() > 1073741824 ){
                    System.out.println("error : size of the file exceeds 1GB.\nChoose another File.");
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public CRD() {
        path = "data-store.txt";
        setFile();
    }
    public CRD(String pathname) {
        path = pathname;
        setFile();
    }

    @Override
    protected void finalize() {
        System.out.println("ended");
    }

    public String create(String key, JSONObject value){
        return create(key,value,0);
    }

    @Override
    public String create(String key, JSONObject value, int TTL) {
        while(isDeleting){

        }
        boolean readResult = read(key).containsKey("error");
        isWriting = true;
        if(key.length() <= 32 && value.toString().length() <= 16000 && readResult){
            try {
                Date date = new Date();
                FileWriter myWriter = new FileWriter(path,true);
                JSONObject jObj = new JSONObject();
                jObj.put("key",key);
                jObj.put("value",value);
                jObj.put("date-created",date.toString());
                jObj.put("TTL",TTL*1000);
                myWriter.write(jObj.toString()+"\n");
                myWriter.close();
                isWriting = false;
                return "Created";
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        else{
            isWriting = false;
            if(key.length() > 32){
                return "error : Key length exceeded 32 characters";
            }
            if(value.toString().length() > 16000){
                return "error : value length exceeded 16KB";
            }
            if(!read(key).containsKey("error")){
                return "error : Key already exists";
            }
        }
        isWriting = false;
        return "null";
    }

    private boolean checkTTL(JSONObject json) throws java.text.ParseException {
        Date date = new Date();
        Date dateC = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(json.get("date-created").toString());
        if((long)json.get("TTL") <= 0){
            return true;
        }
        if(date.getTime() - dateC.getTime() <= (long)json.get("TTL")){
            return true;
        }
        return false;
    }
    @Override
    public JSONObject read(String key) {
        while(isWriting || isDeleting){

        }
        if(key.length() > 32){
            JSONObject temp = new JSONObject();
            temp.put("error","length of key is exceeding 32 characters");
            return temp;
        }
        JSONParser parser = new JSONParser();
        JSONObject json;
        String data;
        try {
            Scanner myReader = new Scanner(fileObj);
            while (myReader.hasNext()) {
                data = myReader.nextLine();
                if(!data.equals("\n")){
                    json = (JSONObject) parser.parse(data);
                    if(json.get("key").equals(key)){
                        if (checkTTL(json)){
                            return (JSONObject)json.get("value");
                        }
                        else{
                            delete(key);
                        }
                        break;
                    }
                    else{

                    }
                }
            }
            myReader.close();
        } catch (FileNotFoundException | ParseException | java.text.ParseException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        JSONObject temp = new JSONObject();
        temp.put("error","Key doesn't exists");
        return temp;
    }

    @Override
    public String delete(String key) {
        while(isWriting){

        }
        isDeleting = true;
        boolean doesExists=false;
        JSONParser parser = new JSONParser();
        JSONObject json;
        String data;
        String allData = "";
        try {
            Scanner myReader = new Scanner(fileObj);
            while (myReader.hasNext()) {
                data = myReader.nextLine();
                if(!data.equals("\n")){
                    json = (JSONObject) parser.parse(data);
                    if(json.get("key").equals(key)){
                        doesExists = true;
                    }
                    else if (!checkTTL(json)){

                    }
                    else{
                        allData = allData + json.toString() + "\n";
                    }
                }
            }
            myReader.close();
        } catch (FileNotFoundException | ParseException | java.text.ParseException e) {
            e.printStackTrace();
        }

        if(doesExists){
            try{
                FileWriter myWriter = new FileWriter(path);
                myWriter.write(allData);
                myWriter.close();
                isDeleting = false;
                return "deleted";
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        else{
            isDeleting = false;
            return "error : Key doesn't exists";
        }
        isDeleting = false;
        return "null";
    }
}
