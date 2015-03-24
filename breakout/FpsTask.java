package breakout;
import java.util.TimerTask;



public class FpsTask extends TimerTask {
	private Breakout breakout;
	
	public FpsTask(Breakout breakout) {
		this.breakout = breakout;
	}

	@Override
	public void run() {
		breakout.setFPS();
	}

}
