package com.weina.test.tianji.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.weina.test.tianji.api.model.Card;
import com.weina.test.tianji.api.model.Feed;
import com.weina.test.tianji.api.model.Message;
import com.weina.test.tianji.api.model.User;
import com.weina.test.tianji.api.model.Users;
import com.weina.test.tianji.api.model.util.ModelUtils;

@Controller
public class TestController {
		private static final String clientId = "a2f03487-c2c7-482c-84f1-e568b3c06812";  
	    private static final String clientSecret = "38dafd8c-9a84-4d03-b6be-2b2b85ce47ba";  
	  
	    private static final String oauthBase = "https://login.tianji.com/authz/oauth";  
	    private static final String authorizeURL = oauthBase + "/authorize";  
	    private static final String accessTokenURL = oauthBase + "/token";  
	    private static final String apiBase = "https://api.tianji.com";  
	    private static final String localhost = "http://localhsot";
	    private static String accessToken = null;
	    private static String redirectUri = null;
	    private static RestTemplate restTemplate;
	    static{
	    	restTemplate = new RestTemplate();
//	    	restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
	    }
	@RequestMapping(method= RequestMethod.GET,value="status")
	public String get(@RequestParam(required=false) String code,HttpServletRequest request, HttpServletResponse response,Model model){
	       redirectUri = request.getRequestURL().toString(); 
	 //redirectUri = "http://10.0.2.1/status"; 
	System.out.println("Remote url is " + redirectUri) ;
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
	           ResponseEntity<Feed> ss =  restTemplate.getForEntity(apiBase+"/me/home_newsfeed?user_detail=full&access_token="+accessToken, Feed.class);
//	           List<FeedModel> feedlist = ModelUtils.getListFeedByString(ss.getBody());
	           model.addAttribute("feedlist", ss.getBody());	            
	           return "test";
	        }  
	}
	@RequestMapping(method = RequestMethod.POST,value="status")
	public String postStatus(String message,Model model){
		 Message o = new Message();
	     o.setMessage(message);
	    ResponseEntity<String> sussess =  restTemplate.postForEntity(apiBase+"/status?access_token="+accessToken, o,String.class);
	    ResponseEntity<Feed> ss =  restTemplate.getForEntity(apiBase+"/me/home_newsfeed?user_detail=full&access_token="+accessToken, Feed.class);
//        List<FeedModel> feedlist = ModelUtils.getListFeedByString(ss.getBody());
        model.addAttribute("feedlist", ss.getBody());	
	    return "test";
	}
	@RequestMapping(value="card",method=RequestMethod.GET)
	public String getProfleCard(Model model,@RequestParam String id){
		 ResponseEntity<Card> ss =  restTemplate.getForEntity(apiBase+"/"+id+"/contact_cards?access_token="+accessToken, Card.class);
		//User user =  ModelUtils.getUserCardByString(ss.getBody());
		 model.addAttribute("card", ss.getBody());
		return "card";
	}
	@RequestMapping(value="contacts",method=RequestMethod.GET)
	public String getProfleContacts(Model model,@RequestParam String location){
		 ResponseEntity<Users> ss =  restTemplate.getForEntity(apiBase+"/me/contacts?user_detail=full&access_token="+accessToken,Users.class);
		//User user =  ModelUtils.getUserCardByString(ss.getBody());
		 List<User> list = ss.getBody().getData();
		 for(int i=0;i<list.size();i++){
			 if(!list.get(i).getLocation().getCity().equals(location)){
				 list.remove(list.get(i));
			 }
		 }
		 model.addAttribute("contacts", list);
		return "contacts";
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
//		MappingJacksonHttpMessageConverter mc = new MappingJacksonHttpMessageConverter();
//		rt.getMessageConverters().add(mc);
        String accessToken = "5cd49226-9152-431f-886c-567d9f8666e1";
        ResponseEntity<Card> ss =  rt.getForEntity(apiBase+"/me/contact_cards?access_token="+accessToken, Card.class);
     //   ResponseEntity<User> ss =  rt.getForEntity(apiBase+"/69372bc566bb8f0dc0b88bbe32796e94/contact_cards?user_detail=full&access_token="+accessToken, User.class);
        Message o = new Message();
//        ResponseEntity<Feed> ss =  rt.getForEntity(apiBase+"/me/home_newsfeed?user_detail=full&?user_detail=full&access_token="+accessToken, Feed.class);
        o.setMessage("hello,everyone!");
      // ResponseEntity<String> ss =  rt.postForEntity(apiBase+"/status?access_token="+accessToken,o ,String.class);
      //  System.out.println(ss.getBody().getData().size()+"==="+ss.getBody().getData().get(0));
       System.out.println(ss.getBody());
	}
	 
}
