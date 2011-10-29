package com.weina.test.tianji.api.model.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.weina.test.tianji.api.model.FeedModel;
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
			connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
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

	public static User getUserByString(String s) {
		User user = new User();
		try {
			JSONObject jsonuser = new JSONObject(s);
			user.setId(jsonuser.getString("id"));
			// user.setCity(jsonuser.getString("location"));
			user.setContact_count(Integer.parseInt(getStrByKey("contact_count", s, Integer.class)));
			user.setLink(getStrByKey("link", s));
			user.setName(getStrByKey("name", s));
			user.setPicture_small(getStrByKey("picture_small", s));
			// user.setPicture_large(jsonuser.getString("picture_large"));
			// user.setHeadline(jsonuser.getString("headline"));
			System.out.println(user);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}
	public static List<FeedModel> getListFeedByString(String s){
		List<FeedModel> list = new ArrayList<FeedModel>();
		JSONObject jsonfeed;
		try {
			jsonfeed = new JSONObject(s);
			JSONArray dataArray = jsonfeed.getJSONArray("data");
			for(int i=0;i<dataArray.length();i++){
				String oo = dataArray.getJSONObject(i).toString();
				FeedModel model = new FeedModel();
				model.setUserName(jsonfeed.getString("name"));
				model.setSenderName(getStrByKey("name",oo));
				model.setSenderLink(getStrByKey("link",oo));
				model.setUpdated_time(getStrByKey("updated_time",oo));
				String message = model.getSenderName()+getStrByKey("label",oo)+getStrByKey("message",oo);
				model.setMessage(message);
				list.add(model);			
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
		
	}
	public static User getUserCardByString(String s) {
		User user = new User();
		user.setEmail(getStrByKey("emails", s));
		String mobile = getStrByKey("mobile", s);
		String phone = getStrByKey("number", s);
		if (mobile != null) {
			user.setMobile(mobile);
		}
		if (phone != null) {
			user.setPhone(phone);
		}
		System.out.println(user);
		// user.setPhone(jsonuser.getJSONArray("phones").getString(0))
		return user;
	}
	private static String getStrByKey(String key, String str) {
		return getStrByKey(key, str, String.class);
	}
	private static String getStrByKey(String key, String str, Class type) {
		Pattern p = getPatternByKey(key, type);
		Matcher m = p.matcher(str);
		if (m.find()) {
			//System.out.println(m.groupCount() + "matched---:" + m.group(1) + "===key==" + m.group(2) +"----" + m.group());
			return m.group(2);
		}
		return null;
	}

	private static Map<String, Object> getStrByKey(String str, Map<String, Class> keys) {
		Map<String, Object> values = new HashMap<String, Object>();
		// keys.get("1").getClass().equals(Integer.class);
		StringBuilder sb = new StringBuilder();
		List<Pattern> ps = new ArrayList<Pattern>(values.size());
		for (Entry<String, Class> entry : keys.entrySet()) {
			ps.add(getPatternByKey(entry.getKey(), entry.getValue()));
		}
		// Pattern pkey = Pattern.compile("\"" + key + "\":[^\"]*\"([^\"]+)");
		for (Pattern p : ps) {
			Matcher m = p.matcher(str);
			while (m.find()) {
				System.out.println("matched---:" + m.group(1) + "===key222==" + m.group(2) + "============" + m.group());
				values.put(m.group(1), m.group(2));
			}
		}
		// Pattern.c
		
		return values;
	}

	private static Pattern getPatternByKey(String key, Class clazz) {
		StringBuilder sb = new StringBuilder();
		sb.append("\"(" + key + ")\":");
		sb.append("[^\"]*?");
		if (clazz.equals(String.class)) {
			sb.append("\"([^\"]+)");
		} else if (clazz.equals(Integer.class)) {
			sb.append("(\\d+)");
		}
		//System.out.println("" + sb);
		return Pattern.compile(sb.toString());
	}

	// {"id":"","type":"CONTACT CARDS","name":"","link":"","updated_time":"2011-10-28T21:57:54+08:00","data":[{"id":"","type":"CONTACT CARD","name":"","link":"","updated_time":"2011-10-28T21:57:54+08:00","kind":"BUSINESS","shared":"EVERYONE","company_address":"北京市朝阳区万达广场１０号楼","city":"北京","country":"中国大陆","region":"北京","emails":["juanjuansui@126.com"],"phones":[{"type":"LANDLINE","number":"010-59798008"}],"ims":[{"type":"MSN","value":"syyywhhlz@hotmail.com"}]}]}
	public static void main(String[] args) {
		String apiBase = "https://api.tianji.com";  
		String str = "afas:\"sdfsdf\", \"emails\":[\"leoner@163.com\"],\"number\":\"010-59798008\", \"number\":\"010-59798\",\"count\": 123456,\"name\":22";
		RestTemplate rt = new RestTemplate();
        String accessToken = "23bd3d22-3659-4ea2-a6b8-eeca1a3eb204";
//        ResponseEntity<String> ss =  rt.getForEntity(apiBase+"/me/contact_cards?access_token="+accessToken, String.class);
        ResponseEntity<String> ss =  rt.getForEntity(apiBase+"/me/home_newsfeed?access_token="+accessToken, String.class);
      //  ResponseEntity<String> ss =  rt.postForEntity(apiBase+"/status?access_token="+accessToken,"ss" ,String.class);
        System.out.println(ss.getBody());
		Map<String, Class> keys = new HashMap<String, Class>();
//		keys.put("emails", String.class);
//		keys.put("number", String.class);
//		keys.put("count", Integer.class);
		//System.out.println(getStrByKey(str, keys));
		List<FeedModel> feedlist = getListFeedByString(ss.getBody());
		System.out.println("1111="+feedlist.get(0).toString());
//		System.out.println(getStrByKey("emails", str, String.class));
//		System.out.println(getStrByKey("number", str, String.class));
//		System.out.println(getStrByKey("count", str, Integer.class));
	}
	
}