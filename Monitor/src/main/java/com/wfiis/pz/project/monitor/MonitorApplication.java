package com.wfiis.pz.project.monitor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

/**
 * 
 * @author Mateusz Papież
 *
 */
@SpringBootApplication(scanBasePackages="com.wfiis")
public class MonitorApplication {
	
	@Autowired
	private Environment env;
	
	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		
		JSONObject reply = new JSONObject();
		String httpurl = env.getProperty("AUTH_SERVICE_URL")+"login";
		try {

			

			URL url = new URL(httpurl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			
			JSONObject body = new JSONObject();
			body.put("username",env.getProperty("auth_username"));
			body.put("password",env.getProperty("pass"));
			
			con.setDoOutput(true);

			OutputStream os = con.getOutputStream();
			BufferedWriter writer = new BufferedWriter(
			        new OutputStreamWriter(os, "UTF-8"));
			writer.write(body.toString());
			writer.flush();
			writer.close();
			os.close();
			
			int responseCode = con.getResponseCode();
			// System.out.println(responseCode);
			if (responseCode == 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
				StringBuilder sb = new StringBuilder();
				String output;
				while ((output = br.readLine()) != null) {
				  sb.append(output);
				}
				
				
				reply = new JSONObject(sb.toString());
				// System.out.println("Logged in");
				// System.out.println(reply.toString());
				//return sb.toString();
			}else {
				System.out.println("Brak odpowiedzi na url /login");
			}

		} catch (Exception e) {
			System.out.println(httpurl);
			System.out.println("Błąd wysyłu danych na url /login");
			e.printStackTrace();
		}
		
		// System.out.println("Json");
		// System.out.println(reply.toString());
		JSONObject json = new JSONObject();
		try {
			json.put("monitor-id", env.getProperty("MONITORID"));
			json.put("api-endpoint", env.getProperty("API_ENDPOINT")+env.getProperty("API_VERSION"));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}    
		// System.out.println("Register http client");
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		try {
			// System.out.println("Before Registering");
			HttpPost request = new HttpPost(env.getProperty("API_GATEWAY_URL"));
			// System.out.println(env.getProperty("API_GATEWAY_URL"));
		    StringEntity params = new StringEntity(json.toString());
			request.addHeader("content-type", "application/json");
			request.addHeader("access-token", reply.getString("access_token"));
			request.setEntity(params);
			// System.out.println("Registering");
			httpClient.execute(request);
			// System.out.println("Registered");
		// handle response here...
		} catch (Exception ex) {
			ex.printStackTrace();
		    // handle exception here
		} finally {
		    try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(MonitorApplication.class);
		application.run( args);
	}

}
