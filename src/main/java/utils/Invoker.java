package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Invoker {
	private String[] cmds;

	public Invoker(String[] cmds) {
		this.cmds = cmds;
	}

	public String invoke() throws IOException, InterruptedException {
		StringBuilder sb = new StringBuilder();
		ProcessBuilder pb = new ProcessBuilder(cmds);
		Process p = pb.start();
		if (p.waitFor() == 0) {
			sb.append(readProcessOutput(p.getInputStream()));
		} else {
			sb.append(readProcessOutput(p.getErrorStream()));
		}
		return sb.toString();
	}

	private String readProcessOutput(InputStream inputStream)
			throws IOException {
		StringBuilder out = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				inputStream));
		String line = null, previous = null;
		while ((line = br.readLine()) != null)
			if (!line.equals(previous)) {
				previous = line;
				out.append(line).append('\n');
			}
		return out.toString();
	}
}
