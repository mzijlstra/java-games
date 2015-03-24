package snake.multi.net;

import java.io.BufferedReader;
import java.io.IOException;

public class SynchronizedReader {
	private BufferedReader reader;
	private String lastLine;
	
	public SynchronizedReader(BufferedReader reader) {
		this.reader = reader;
	}
	
	public synchronized String readLine() throws IOException {
		lastLine = reader.readLine();
		System.out.println("Read: " + lastLine);
		return lastLine;
	}
	
	public synchronized String[] readTokens() throws IOException {
		lastLine = reader.readLine();
		System.out.println("Read: " + lastLine);
		return lastLine.split("\\s+");
	}
	
	public String getLastLine() {
		return lastLine;
	}
}
