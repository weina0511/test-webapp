package com.weina.test.tianji.api;


	public class Oauth2SampleCodeServlet extends HttpServlet {  
	    private static final long serialVersionUID = 1L;  
	  
	    private static final String clientId = "[your client id]";  
	    private static final String clientSecret = "[your client secret]";  
	  
	    private static final String oauthBase = "https://secure.tianji.com/oauth-provider";  
	    private static final String authorizeURL = oauthBase + "/authorize2";  
	    private static final String accessTokenURL = oauthBase + "/access_token2";  
	    private static final String apiBase = "https://api.tianji.com";  
	  
	    @Override  
	    protected void doGet(HttpServletRequest request, HttpServletResponse response)  
	        throws ServletException, IOException {  
	  
	        String code = request.getParameter("code");  
	        String redirectUri = request.getRequestURL().toString();  
	  
	        if (code == null || "".equals(code)) {  
	  
	            // Step 1 - Redirect user to provider for authorization  
	            String url = authorizeURL + "?response_type=code&display=popup&lang=en&client_id="  
	                                       + clientId + "&redirect_uri=" + redirectUri;  
	            response.sendRedirect(url);  
	  
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
	            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);  
	            response.setHeader("Location", apiBase + "/me.xml?access_token=" + accessToken);  
	        }  
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
