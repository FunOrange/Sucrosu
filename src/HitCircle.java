import java.awt.*;
// TODO: make AR table to debug approach circles
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
		CLICKED,
		DELETEME
	}
	// some variables
	private int visibleDuration;
	private int timingWindow_50, timingWindow_100, timingWindow_300;
	private State state;
	private Color color;
	private ParticleHandler particleHandler;
	private int alpha;
	private int eta;
	private CircleHandler master;
	// instance specific properties
	private int targetTime;
	public int x, y;
	
	public int radius;
	private int approachRadius;
	private Color approachColor;
	
	public HitCircle(int x, int y, int targetTime, int label) {
		particleHandler = game.getParticleHandler();
		
		master = game.getCircleHandler();
		
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
		// FIXME: this shit is clearly erroneous
		eta = targetTime - game.localTime;
		approachRadius = (int) (radius + (100 - radius) * ((double) eta / (double) visibleDuration));
		approachRadius = Game.clamp(approachRadius, radius - 1, 10000);
		alpha = 255 - ((targetTime - visibleDuration) - game.localTime);
		alpha = Game.clamp(alpha, 0, 255);
		approachColor = new Color(255, 255, 255, alpha);
		
		// state system
		switch (state) {
			case WAIT:
				color = new Color(0, 0, 100, alpha);
				if (game.localTime > targetTime - visibleDuration) {
					state = State.VISIBLE;
				}
				break;
			case VISIBLE:
				color = new Color(0, 0, 116, alpha);
				if (game.localTime > targetTime - timingWindow_50) {
					state = State.CLICKABLE;
				}
				break;
			case CLICKABLE:
				color = new Color(0, 135, 195);
//				if (Math.abs(eta) < timingWindow_300)
//					color = Color.GREEN;
//				else if (Math.abs(eta) < timingWindow_100)
//					color = Color.YELLOW;
//				else if (Math.abs(eta) < timingWindow_50)
//					color = Color.RED;
				if (game.localTime > targetTime + timingWindow_50) {
					state = State.DELETEME;
				}
				break;
		}
	}
	
	public void onClick(int mx, int my) {
		double distance = Math.hypot(mx - x, my - y);
		if (distance < radius) {
			if (state == State.CLICKABLE) {
				if (Math.abs(eta) < timingWindow_300) {
					game.debugMessage[1] = ("300~! " + eta);
					particleHandler.add(new TimingJudge(x, y, 1));
				} else if (Math.abs(eta) < timingWindow_100) {
					game.debugMessage[1] = ("100~! " + eta);
					particleHandler.add(new TimingJudge(x, y, 2));
				} else if (Math.abs(eta) < timingWindow_50) {
					game.debugMessage[1] = ("50~! " + eta);
					particleHandler.add(new TimingJudge(x, y, 3));
				}
			} else {
				game.debugMessage[1] = ("Timing Miss~!");
				particleHandler.add(new TimingJudge(x, y, 0));
			}
			this.state = State.DELETEME;
		} else {
			game.debugMessage[1] = String.format("Miss~! You clicked: (%d, %d) Circle was at (%d, %d)", mx, my, x, y);
		}
	}
	
	public void draw() {
		if (state != State.DELETEME) {
			// approach circle
			g.setColor(approachColor);
			g.drawOval(x - approachRadius, y - approachRadius, approachRadius * 2, approachRadius * 2);
			
			// circle
			g.setColor(color);
			g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
			g.setColor(new Color(241, 250, 140, alpha));
			g.setStroke(new BasicStroke(5));
			g.drawOval(x - radius, y - radius, radius * 2, radius * 2);
			g.setColor(new Color(20, 20, 20, 200));
			g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
			
			// label
			g.setFont(new Font("Arial", Font.PLAIN, 20));
			g.setColor(new Color(255, 255, 255, alpha));
			g.drawString(String.valueOf(eta), x-14, y+7);
//			g.drawString(String.valueOf(label), x-4, y+5);
			g.setFont(Game.DEFAULT_FONT);
		}
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
