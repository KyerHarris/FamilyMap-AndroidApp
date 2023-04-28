package kyer.harris.familymap.backend;

import java.io.*;
import java.net.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Requests.*;
import Results.*;

public class ServerProxy {
    private static String serverHost = "10.0.2.2";
    private static String serverPort = "1521";

    public static void main(String[] args){
        serverHost = args[0];
        serverPort = args[1];
    }
    public void setServerHost(String newHost){
        serverHost = newHost;
    }

    public LoginResult login(LoginRequest request){
        LoginResult result = null;
        HttpURLConnection http = null;
        try{
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");
            http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.connect();
            OutputStream reqBody = http.getOutputStream();
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            String reqData = gson.toJson(request);
            writeString(reqData, reqBody);
            reqBody.close();
            if(http.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream resBody = http.getInputStream();
                String resData = readString(resBody);
                result = gson.fromJson(resData, LoginResult.class);
            }
            else{
                InputStream resBody = http.getErrorStream();
                String resData = readString(resBody);
                result = gson.fromJson(resData, LoginResult.class);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            if(http != null){
                http.disconnect();
            }
        }
        return result;
    }
    public RegisterResult register(RegisterRequest request){
        RegisterResult result = null;
        HttpURLConnection http = null;
        try{
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
            http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.connect();
            OutputStream reqBody = http.getOutputStream();
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            String reqData = gson.toJson(request);
            writeString(reqData, reqBody);
            reqBody.close();
            if(http.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream resBody = http.getInputStream();
                String resData = readString(resBody);
                result = gson.fromJson(resData, RegisterResult.class);
                System.out.println("Successfully Logged In");
            }
            else{
                InputStream resBody = http.getErrorStream();
                String resData = readString(resBody);
                result = gson.fromJson(resData, RegisterResult.class);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            if(http != null) {
                http.disconnect();
            }
        }
        return result;
    }
    public EventResult getEvents(){
        EventResult result = null;
        try{
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", DataCache.getInstance().getAuthtoken());
            http.connect();
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            if(http.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream resBody = http.getInputStream();
                String resData = readString(resBody);
                result = gson.fromJson(resData, EventResult.class);
            }
            else{
                System.out.println("Error: " + http.getResponseMessage());
                InputStream resBody = http.getErrorStream();
                String resData = readString(resBody);

                result = gson.fromJson(resData, EventResult.class);
                System.out.println(resData);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
    public PersonResult getPersons(){
        PersonResult result = null;
        try{
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", DataCache.getInstance().getAuthtoken());
            http.connect();
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            if(http.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream resBody = http.getInputStream();
                String resData = readString(resBody);
                result = gson.fromJson(resData, PersonResult.class);
                System.out.println(resData);
            }
            else{
                InputStream resBody = http.getErrorStream();
                String resData = readString(resBody);
                result = gson.fromJson(resData, PersonResult.class);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }
    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
