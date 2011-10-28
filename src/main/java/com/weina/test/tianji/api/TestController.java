package com.weina.test.tianji.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.weina.test.tianji.api.model.User;
import com.weina.test.tianji.api.model.util.ModelUtils;

@Controller
@RequestMapping("test")
public class TestController {
		private static final String clientId = "a2f03487-c2c7-482c-84f1-e568b3c06812";  
	    private static final String clientSecret = "38dafd8c-9a84-4d03-b6be-2b2b85ce47ba";  
	  
	    private static final String oauthBase = "https://login.tianji.com/authz/oauth";  
	    private static final String authorizeURL = oauthBase + "/authorize";  
	    private static final String accessTokenURL = oauthBase + "/token";  
	    private static final String apiBase = "https://api.tianji.com";  
	    private static String accessToken = null;
	    private static String redirectUri = null;
	    @Autowired
	    @Qualifier("restTemplate")
	    private RestTemplate restTemplate;
	@RequestMapping(method= RequestMethod.GET)
	public String get(@RequestParam(required=false) String code,HttpServletRequest request, HttpServletResponse response,Model model){
	        redirectUri = request.getRequestURL().toString();  
	  
	        if (code == null || "".equals(code)) {  
	            // Step 1 - Redirect user to provider for authorization  
	            String url = authorizeURL + "?response_type=code&display=popup&lang=en&client_id="  
	                                       + clientId + "&redirect_uri=" + redirectUri;  
	            return "redirect:"+url;
	        } else { 
	        	if(accessToken==null){
	        		getaccessToken(code);
	            // Step 2 - Exchange for access grant  		           
	        	}
	            // Step 3 - Create connection 
	           ResponseEntity<String> ss =  restTemplate.getForEntity(apiBase+"/me?access_token="+accessToken, String.class);
	           User user = ModelUtils.getUserByString(ss.getBody());
	          //  return "redirect:"+apiBase + "/me.xml?access_token=" + accessToken;
	           // response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);  
	            //response.setHeader("Location", apiBase + "/me.xml?access_token=" + accessToken);  
	           model.addAttribute("user", user);	            
	           return "test";
	        }  
	}
	@RequestMapping(value="card",method=RequestMethod.GET)
	public String getProfleCard(Model model){
		 ResponseEntity<String> ss =  restTemplate.getForEntity(apiBase+"/me/contact_cards?access_token="+accessToken, String.class);
		User user =  ModelUtils.getUserCardByString(ss.getBody());
		 model.addAttribute("card", user);
		return "card";
	}
	private void getaccessToken(String code){
				 String urlParameters = "grant_type=authorization_code&client_id=" + clientId  
		         + "&client_secret=" + clientSecret  
		         + "&redirect_uri=" + redirectUri + "&code=" + code;  
		
		 String resp = ModelUtils.executePost(accessTokenURL, urlParameters);  
		 try {  
		     JSONObject json = new JSONObject(resp);  
		     accessToken = json.getString("access_token");  
		     System.out.println("accessToken==="+accessToken);
		 } catch (JSONException e) { }  
	}
	public static void main(String[] args) {
		RestTemplate rt = new RestTemplate();
        String accessToken = "23bd3d22-3659-4ea2-a6b8-eeca1a3eb204";
        ResponseEntity<String> ss =  rt.getForEntity(apiBase+"/me/contact_cards?access_token="+accessToken, String.class);
        System.out.println(ss.getBody());
	}
	 
}
