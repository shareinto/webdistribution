package com.omco.ci.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.Properties;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class GlobalConfig {
	public static void main(String[] args) {
		MultipartEntity entity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
			CloseableHttpResponse response = null;
			InputStream stream = new FileInputStream(new File(
					"Z:\\workspace\\com.omco.ci\\target\\com.omco.ci.war"));
			entity.addPart("files[]", new InputStreamBody(stream,
					"com.omco.ci.war"));
			entity.addPart("group", new StringBody("group1"));
			HttpPost post = new HttpPost("http://192.168.213.135/file/upload");
			post.setEntity(entity);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			response = httpClient.execute(post);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			String line = null;
			while ((line = reader.readLine()) != null ) {
				System.out.println(line);
			}
		} catch (Exception e) {
		}
	}

	public static String uploadFolder;

	public static String saltFileServerPre;

	static {
		try {
			Resource resource = new ClassPathResource(
					"/config/application.properties");
			Properties prop = PropertiesLoaderUtils.loadProperties(resource);
			uploadFolder = prop.getProperty("global.upload.folder");
			saltFileServerPre = prop.getProperty("global.salt.fileserver.pre");
		} catch (Exception e) {
		}
	}
}
