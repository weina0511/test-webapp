package com.weina.test.tianji.api.model.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.weina.test.tianji.api.model.User;

public class ModelUtils {
	// POST request helper  
    public static String executePost(String targetURL, String urlParameters) {  
        URL url;  
        HttpURLConnection connection = null;  
        try {  
            // Create connection  
            url = new URL(targetURL);  
            connection = (HttpURLConnection) url.openConnection();  
            connection.setRequestMethod("POST");  
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");  
            connection.setRequestProperty("Content-Length",  
                        "" + Integer.toString(urlParameters.getBytes().length));  
            connection.setRequestProperty("Content-Language", "en-US");  
  
            connection.setUseCaches(false);  
            connection.setDoInput(true);  
            connection.setDoOutput(true);  
  
            // Send request  
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());  
            wr.writeBytes(urlParameters);  
            wr.flush();  
            wr.close();  
  
            // Get Response  
            InputStream is = connection.getInputStream();  
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));  
            String line;  
            StringBuffer response = new StringBuffer();  
            while ((line = rd.readLine()) != null) {  
                response.append(line);  
                response.append('\r');  
            }  
            rd.close();  
            return response.toString();  
  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        } finally {  
            if (connection != null) {  
                connection.disconnect();  
            }  
        }  
    }  
	public static User getUserByString(String s){
		User user = new User();
		   try {
			JSONObject jsonuser = new JSONObject(s);
			user.setId(jsonuser.getString("id"));
//			user.setCity(jsonuser.getString("location"));
			user.setContact_count(jsonuser.getInt("contact_count"));
			user.setLink(jsonuser.getString("link"));
			user.setName(jsonuser.getString("name"));
			user.setPicture_small(jsonuser.getString("picture_small"));
			user.setPicture_large(jsonuser.getString("picture_large"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		   return user;
	}
}
