import java.awt.*;
import java.awt.FontMetrics;

/*  AR TABLE
	* AR 0-5:
	* Base: 1800 ms
	* Decreases by 120 ms with every point to AR, up to AR 5
	* 
	* AR 5-10
	* Base: 1200 ms at AR 5
	* Decreases by 150 ms with every additional point to AR (AR - 5)
*/
public class HitCircle extends GameObject {
	enum State {
		WAIT,
		VISIBLE,
		CLICKABLE,
		DISAPPEAR,
		DELETEME
	}
	
	// some variables
	private static final float INITIAL_APPROACH_RADIUS_FACTOR = 4.25f;
	private static final float FADE_RATE = 1.5f;
	private static final float FADE_TIME = 110;
	private int visibleDuration;
	private int timingWindow_50, timingWindow_100, timingWindow_300;
	private ParticleHandler particleHandler;
	private CircleHandler master;
	private State state;
	private Color color;
	private String label;
	private float alpha;
	private int eta;
	private int deleteTime;
	// instance specific properties
	private int targetTime;
	public int x, y;
	
	public int radius;
	private int approachRadius;
	
	public HitCircle(int x, int y, int targetTime, String label, Color color) {
		particleHandler = game.getParticleHandler();
		master = game.getCircleHandler();
		this.label = label;
		this.color = color;
		this.targetTime = targetTime;
		this.x = x;
		this.y = y;
		// map dependent properties
		int MIN_RADIUS = 15;
		int MAX_RADIUS = 45;
		radius = (int) (MIN_RADIUS + ((10 - master.CS) / 10) * (MAX_RADIUS - MIN_RADIUS));
		
		// other variables
		if (master.AR <= 5)
			visibleDuration = (int) (1800 - (120 * master.AR));
		else
			visibleDuration = (int) (1200 - (150 * (master.AR - 5)));
		// TODO: test for cases OD:[1-10]
		timingWindow_300 = getTimingWindow_300(master.OD);
		timingWindow_100 = getTimingWindow_100(master.OD);
		timingWindow_50 = getTimingWindow_50(master.OD);
		
		state = State.WAIT;
	}
	
	private int getTimingWindow_50(double OD) {
		return (int) (198 - (10 * OD)) / 2;
	}
	
	private int getTimingWindow_100(double OD) {
		return (int) (138 - (8 * OD)) / 2;
	}
	
	private int getTimingWindow_300(double OD) {
		return (int) (78 - (6 * OD)) / 2;
	}
	
	public void tick() {
		// calculate approach circle movement
		eta = targetTime - game.getLocalTime();
		approachRadius = Game.lerp((int) (radius * INITIAL_APPROACH_RADIUS_FACTOR), radius, 1.0f - (float) eta / (float) visibleDuration);
		alpha = -FADE_RATE * (targetTime - visibleDuration - game.getLocalTime());
		alpha = Game.clamp(alpha, 0f, 1f);
		// TODO: finish writing this
		alpha = Game.lerp(0.0f, 1.0f, ((float) visibleDuration - (float) eta)/FADE_TIME);
		
		// state system
		switch (state) {
			case WAIT:
				if (game.getLocalTime() > targetTime - visibleDuration) {
					state = State.VISIBLE;
				}
				break;
			case VISIBLE:
				if (game.getLocalTime() > (targetTime - timingWindow_50))
					state = State.CLICKABLE;
				break;
			case CLICKABLE:
				color = Color.WHITE;
				if (game.getLocalTime() > targetTime /*+ timingWindow_50*/) {
					deleteTime = game.getLocalTime();
					state = State.DISAPPEAR;
				}
				break;
			case DISAPPEAR:
				System.out.println("I'm disappearing!");
				alpha = Game.lerp(1.0f, 0.0f, (game.getLocalTime() - deleteTime)/FADE_TIME);
				if (alpha <= 0) {
					System.out.println("circle deleted.");
					state = State.DELETEME;
				}
				break;
		}
	}
	
	public void onClick(int mx, int my) {
		float distance = (float) Math.hypot(mx - x, my - y);
		if (distance < radius) {
			if (state == State.CLICKABLE) {
				if (Math.abs(eta) < timingWindow_300) {
					particleHandler.add(new TimingJudge(x, y, 1));
				} else if (Math.abs(eta) < timingWindow_100) {
					particleHandler.add(new TimingJudge(x, y, 2));
				} else if (Math.abs(eta) < timingWindow_50) {
					particleHandler.add(new TimingJudge(x, y, 3));
				}
			} else {
				particleHandler.add(new TimingJudge(x, y, 0));
			}
			this.state = State.DELETEME;
		} else {
		}
	}
	
	public void draw() {
		// set transparency
		setAlpha(alpha);
		
		// approach circle
		g.setStroke(new BasicStroke(1.5f));
		g.setColor(color);
		g.drawOval(x - approachRadius, y - approachRadius, approachRadius * 2, approachRadius * 2);
		
		// hit circle
		g.setColor(Color.BLACK);
		g.fillOval(x - (radius + 1), y - (radius + 1), (radius + 1) * 2, (radius + 1) * 2);
		g.setColor(Color.WHITE);
		g.setStroke(new BasicStroke(3));
		g.drawOval(x - (radius - 2), y - (radius - 2), (radius - 2) * 2, (radius - 2) * 2);
		g.setColor(color);
		g.setStroke(new BasicStroke(3));
		g.drawOval(x - (radius - 4), y - (radius - 4), (radius - 4) * 2, (radius - 4) * 2);
		
		
		// label
		g.setColor(Color.WHITE);
		g.setFont(new Font("serif", Font.PLAIN, 36));
		FontMetrics fontMetrics = g.getFontMetrics();
		g.drawString(label, x+1 - fontMetrics.stringWidth(label) / 2, y + 12);
		
	}
	
	public State getState() {
		return state;
	}
	
	public String toString() {
		return "HitCircle{" +
			"x=" + x +
			", y=" + y +
			", targetTime=" + targetTime +
			'}';
	}
	
}
