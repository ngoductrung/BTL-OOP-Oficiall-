package helloworld;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;



public class Test {
    public static void main (String[] args) throws MalformedURLException, ProtocolException,IOException {
        URL url = new URL(Constant.DEL_SAVED_SEARCH); /* URL to an API */
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        Map<String, String> params = new HashMap<>();
        params.put("search_id", "123456789");
        params.put("all", "1");
        /*tạo param*/

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (postData.length() != 0) {
                postData.append('&');
            }
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }

        byte[] postDataBytes = postData.toString().getBytes("UTF-8");
        connection.setDoOutput(true);
        try (DataOutputStream writer = new DataOutputStream(connection.getOutputStream())) {
            writer.write(postDataBytes);
            writer.flush();
            writer.close();

            StringBuilder content;

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                String line;
                content = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            } /* Program already receive data */
            System.out.println(content.toString());

            Gson g = new Gson();
            ResponseSignUp rp = g.fromJson(content.toString(), ResponseSignUp.class);

//	        try {
//	        	rp.code.equals("9992");
//			}catch (AssertionError e) {
//	        	e.printStackTrace();
//			}
            Map<String, String> table = new HashMap<>();
            table.put("1000","OK");
            table.put("9992", "Post is not existed");
            table.put("9993", "Code verify is incorrect");
            table.put("9994", "No data or end of list data");
            table.put("1004", "Parameter type is invalid");
            table.put("1009", "Not access");
            table.put("1005", "Unknow error");
            table.put("9998","Token is invalid");
            if(table.containsKey(rp.code)==true){
                System.out.println(rp.message);
            }else{
                System.out.println("Error");
            }

        } finally {
            connection.disconnect();
        }
    }

}
class ResponseSignUp {
    public String code;
    public String message;
}


