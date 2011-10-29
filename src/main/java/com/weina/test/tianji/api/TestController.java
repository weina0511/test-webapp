package com.weina.test.tianji.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.weina.test.tianji.api.model.FeedModel;
import com.weina.test.tianji.api.model.Message;
import com.weina.test.tianji.api.model.User;
import com.weina.test.tianji.api.model.util.ModelUtils;

@Controller
@RequestMapping("app")
public class TestController {
		private static final String clientId = "a2f03487-c2c7-482c-84f1-e568b3c06812";  
	    private static final String clientSecret = "38dafd8c-9a84-4d03-b6be-2b2b85ce47ba";  
	  
	    private static final String oauthBase = "https://login.tianji.com/authz/oauth";  
	    private static final String authorizeURL = oauthBase + "/authorize";  
	    private static final String accessTokenURL = oauthBase + "/token";  
	    private static final String apiBase = "https://api.tianji.com";  
	    private static String accessToken = null;
	    private static String redirectUri = null;
	    private static RestTemplate restTemplate;
	    static{
	    	restTemplate = new RestTemplate();
	    	restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
	    }
	@RequestMapping(method= RequestMethod.GET,value="status")
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
	          // ResponseEntity<String> ss =  restTemplate.getForEntity(apiBase+"/me?access_token="+accessToken, String.class);
	           ResponseEntity<String> ss =  restTemplate.getForEntity(apiBase+"/me/home_newsfeed?access_token="+accessToken, String.class);
	           List<FeedModel> feedlist = ModelUtils.getListFeedByString(ss.getBody());
	          //  return "redirect:"+apiBase + "/me.xml?access_token=" + accessToken;
	           // response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);  
	            //response.setHeader("Location", apiBase + "/me.xml?access_token=" + accessToken);  
	           model.addAttribute("feedlist", feedlist);	            
	           return "test";
	        }  
	}
	@RequestMapping(method = RequestMethod.POST,value="status")
	public @ResponseBody String postStatus(String message){
		 Message o = new Message();
	     o.setMessage(message);
	     try {
	    	 ResponseEntity<String> ss =  restTemplate.postForEntity(apiBase+"/status?access_token="+accessToken, o,String.class);
	    	 return "success";
		} catch (Exception e) {
			// TODO: handle exception
			return "false";
		}
	}
	@RequestMapping(value="card",method=RequestMethod.GET)
	public String getProfleCard(Model model){
		 ResponseEntity<String> ss =  restTemplate.getForEntity(apiBase+"/me/contact_cards?access_token="+accessToken, String.class);
		User user =  ModelUtils.getUserCardByString(ss.getBody());
		 model.addAttribute("card", user);
		return "card";
	}
	@RequestMapping(value="contacts",method=RequestMethod.GET)
	public String getProfleContacts(Model model){
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
		rt.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
        String accessToken = "23bd3d22-3659-4ea2-a6b8-eeca1a3eb204";
//        ResponseEntity<String> ss =  rt.getForEntity(apiBase+"/me/contact_cards?access_token="+accessToken, String.class);
     //   ResponseEntity<String> ss =  rt.getForEntity(apiBase+"/me/home_newsfeed?access_token="+accessToken, String.class);
        Message o = new Message();
        o.setMessage("hello,everyone!");
       ResponseEntity<String> ss =  rt.postForEntity(apiBase+"/status?access_token="+accessToken,o ,String.class);
        System.out.println(ss.getBody());
	}
	 
}
