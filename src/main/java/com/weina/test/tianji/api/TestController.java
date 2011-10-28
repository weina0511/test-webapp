package com.weina.test.tianji.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("test")
public class TestController {
		private static final String clientId = "a2f03487-c2c7-482c-84f1-e568b3c06812";  
	    private static final String clientSecret = "38dafd8c-9a84-4d03-b6be-2b2b85ce47ba";  
	  
	    private static final String oauthBase = "https://login.tianji.com/authz/oauth";  
	    private static final String authorizeURL = oauthBase + "/authorize";  
	    private static final String accessTokenURL = oauthBase + "/token";  
	      private static final String apiBase = "https://api.tianji.com";  
	@RequestMapping(method= RequestMethod.GET)
	public String get(@RequestParam(required=false) String code,HttpServletRequest request, HttpServletResponse response){
	        String redirectUri = request.getRequestURL().toString();  
	  
	        if (code == null || "".equals(code)) {  
	            // Step 1 - Redirect user to provider for authorization  
	            String url = authorizeURL + "?response_type=code&display=popup&lang=en&client_id="  
	                                       + clientId + "&redirect_uri=" + redirectUri;  
	            return "redirect:"+url;
	           // response.sendRedirect(url);  
	  
	        } else {  
	            // Step 2 - Exchange for access grant  
	            String urlParameters = "grant_type=authorization_code&client_id=" + clientId  
	                    + "&client_secret=" + clientSecret  
	                    + "&redirect_uri=" + redirectUri + "&code=" + code;  
	  
	            String resp = executePost(accessTokenURL, urlParameters);  
	  
	            String accessToken = "";  
	            try {  
	                JSONObject json = new JSONObject(resp);  
	                accessToken = json.getString("access_token");  
	            } catch (JSONException e) { }  
	  
	            // Step 3 - Create connection 
	            return "redirect:"+apiBase + "/me.xml?access_token=" + accessToken;
	           // response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);  
	            //response.setHeader("Location", apiBase + "/me.xml?access_token=" + accessToken);  
	        }  
		//return "test";
	}
	
	 // POST request helper  
    private String executePost(String targetURL, String urlParameters) {  
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
}
