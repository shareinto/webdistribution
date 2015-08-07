package com.omco.ci.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import utils.Invoker;

import com.omco.ci.config.GlobalConfig;

@RestController
public class ManageController {
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test()
	{
		return "hello docker!this is a test";
	}
	
	// µ¥ÎÄ¼þÉÏ´«
	@RequestMapping(value = "/file/upload", method = RequestMethod.POST)
	public String queryFileData(
			@RequestParam("files[]") CommonsMultipartFile file, String group,
			HttpServletRequest request) throws IOException, Exception {
		String lastFileName = File.separator + UUID.randomUUID().toString()
				+ File.separator + file.getOriginalFilename();
		String saltFileserver = GlobalConfig.saltFileServerPre + lastFileName;
		File tempFile = new File(GlobalConfig.uploadFolder + lastFileName);
		FileUtils.copyInputStreamToFile(file.getInputStream(), tempFile);
		StringBuilder sb = new StringBuilder();
		String[] cmds = new String[] { "salt", "-N", group, "cp.get_file",
				saltFileserver, "/data/web/" + file.getOriginalFilename() };
		Invoker invoker = new Invoker(cmds);
		sb.append(invoker.invoke() + "\n");
		cmds = new String[] { "salt", "-N", group, "cmd.run",
				"/usr/local/tomcat/bin/shutdown.sh" };
		invoker = new Invoker(cmds);
		sb.append(invoker.invoke() + "\n");

		cmds = new String[] { "salt", "-N", group, "cmd.run",
				"rm -rf /usr/local/tomcat/webapps/*" };
		invoker = new Invoker(cmds);
		sb.append(invoker.invoke() + "\n");

		cmds = new String[] { "salt", "-N", group, "cmd.run",
				"/usr/local/tomcat/bin/startup.sh" };
		invoker = new Invoker(cmds);
		sb.append(invoker.invoke() + "\n");
		return sb.toString();
	}
}
