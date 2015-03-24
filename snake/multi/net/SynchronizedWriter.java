package snake.multi.net;

import java.io.PrintWriter;

public class SynchronizedWriter {
	private PrintWriter writer;
	
	public SynchronizedWriter(PrintWriter writer) {
		this.writer = writer;
	}
	
	public synchronized void println(String line) {
		writer.println(line);
		writer.flush();
		System.out.println("Wrote: " + line);
	}
}
