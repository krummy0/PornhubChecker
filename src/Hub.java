import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.*;

//TODO Make sure checking for premium account is working

public class Hub extends Scrapper {
    private final String email;
    private final String pass;
    private String token;
    private boolean valid;
    private boolean premium;
    private String username;
    

    public Hub(String email, String pass) throws Exception {
    	super();
    	setURI(URI.create("https://www.pornhub.com"));
        this.email = email;
        this.pass = pass;
        getCookie();
        checkLog();
    }
 
    //get Functions
    
    public String getEmail() {
    	return email;
    }
    public String getPass() {
    	return pass;
    }
    public String getUsername() {
    	return username;
    }
    public boolean isValid() {
    	return valid;
    }
    public boolean isPremium() {
    	return premium;
    }
    
    //getAs Functions
    
    public String getAsLog() {
    	return email + '|' + pass + '|' + premium;
    }
    
    //core
     private void checkLog() throws Exception{
        String url = "https://www.pornhub.com/front/authenticate";
        String method = "POST";
        String log = URLEncoder.encode(email, "UTF-8");
        String password = URLEncoder.encode(pass, "UTF-8");

        String body = "redirect=" + ""
            + "&user_id=&intended_action=&token="  + token
            + "&from=pc_login_modal_%3Aindex&email="
            + log + "&password=" + password + "&remember_me=on";

        //headers
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json, text/javascript, */*; q=0.01");
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headers.put("Origin", "https://www.pornhub.com");
        headers.put("Referer", "https://www.pornhub.com/");
        headers.put("Sec-Fetch-Dest", "empty");
        headers.put("Sec-Fetch-Mode", "cors");
        headers.put("X-Requested-With", "XMLHttpRequest");
        
        headers = addLessCommonHeaders(headers);

        Response response = makeCurlRequest(url, method, body, headers);
        //set new cookie
        setNewCookie(response.getHeaders());
        
        // Get values
        JSONObject json = (JSONObject) JSONValue.parse(response.getBody());
        valid = "1".equals(json.get("success"));
        premium = ! ("0".equals(json.get("premium_redirect_cookie")));
        username = (String)json.get("username");
    }

    private void getCookie() throws Exception {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8");
        headers.put("Sec-Fetch-Dest", "document");
        headers.put("Sec-Fetch-Mode", "navigate");
        headers.put("Sec-Fetch-Site", "none");
        headers.put("Sec-Fetch-User", "?1");
        headers.put("Upgrade-Insecure-Requests", "1");
        
        headers = addCommonHeaders(headers);
        Response res = makeCurlRequest("https://www.pornhub.com/", "GET", null, headers, 45000);

        //set cookie to array of set-cookie
        setNewCookie(res.getHeaders());

        //get token from html
        token = res.getBody().split("name=\"token\"")[1];
        token = token.split("\"")[1];
    }

    private Map<String, String> addLessCommonHeaders(Map<String, String> headers) {
        headers.put("Cookie", getCookieHeader());
        headers.put("Sec-Fetch-Site", "same-origin");
        
    	headers = addCommonHeaders(headers);
    	return headers;
    }
    
    private Map<String, String> addCommonHeaders(Map<String, String> headers) {
    	headers.put("Accept-Encoding", "gzip, deflate, br, zstd");
    	headers.put("Accept-Language", "en-US,en;q=0.9");
        headers.put("Sec-GPC", "1");
        headers.put("User-Agent", getUA());
        return headers;
    }
}
