import java.awt.*;

/*  OD TABLE
	* 300 WINDOW
	* Base: 78 ms
	* Decreases by 6 ms with every point to OD
	* 
	* 100 WINDOW
	* Base: 138 ms
	* Decreases by 8 with every point to OD
	* 
	* 50 WINDOW
	* Base: 198 ms
	* Decreases by 10 with every point to OD
*/

/*  AR TABLE
	* AR 0-5:
	* Base: 1800 ms
	* Decreases by 120 ms with every point to AR, up to AR 5
	* 
	* AR 5-10
	* Base: 1200 ms at AR 5
	* Decreases by 150 ms with every additional point to AR (AR - 5)
*/

public class HitCircle {
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
	private static Game game;
	private int alpha;
	private int eta;
	private CircleHandler master;
	
	// instance specific properties
	private int targetTime;
	public int x, y;
	private int label;
	
	private int radius;
	private int approachRadius;
	private Color approachColor;
	
	public HitCircle(int x, int y, int targetTime, int label) {
		if (game == null) {
			game = Game.getInstance();
		}
		master = game.getCircleHandler();
		
		this.targetTime = targetTime;
		this.x = x;
		this.y = y;
		this.label = label;
		// map dependent properties
		int MIN_RADIUS = 15;
		int MAX_RADIUS = 45;
		radius = (int) (MIN_RADIUS + ((10-master.CS)/10)*(MAX_RADIUS - MIN_RADIUS));
		
		// other variables
		if (master.AR <= 5)
			visibleDuration = (int) (1800 - (120 * master.AR));
		else
			visibleDuration = (int) (1200 - (150 * (master.AR - 5)));
		timingWindow_300 = (int) (78 - (6 * master.OD));
		timingWindow_100 = (int) (138 - (8 * master.OD));
		timingWindow_50  = (int) (198 - (10 * master.OD));
		
		state = State.WAIT;
	}
	
	public void tick() {
		// calculate approach circle movement
		eta              = targetTime - game.localTime;
		approachRadius   = (int) (radius + (100-radius)*((double) eta/(double) visibleDuration));
		approachRadius   = Game.clamp(approachRadius, radius-1, 10000);
		alpha            = 255 - ((targetTime - visibleDuration) - game.localTime);
		alpha            = Game.clamp(alpha, 0, 255);
//		approachColor    = new Color(241, 250, 140, alpha);
		approachColor    = new Color(255, 255, 255, alpha);
		
		// state system
		switch (state) {
			case WAIT:
				color = new Color(0, 0, 100, alpha);
				if (game.localTime > targetTime - visibleDuration) {
					state = State.VISIBLE;
				}
				break;
			case VISIBLE:
				color = new Color(0, 0, 100);
				if (game.localTime > targetTime - timingWindow_50) {
					state = State.CLICKABLE;
				}
				break;
			case CLICKABLE:
				color = new Color(0, 0, 150);
				if (game.localTime > targetTime + timingWindow_50) {
					state = State.DELETEME;
				}
				break;
			case CLICKED:
				// TODO: animate hit explosion
				// TODO: create particle effect
				// switch state to DELETEME after a period of time
				break;
		}
	}
	
	public void render(Graphics g) {
		if (state != State.DELETEME) {
			g.setColor(approachColor);
			g.drawOval(x-approachRadius, y-approachRadius, approachRadius*2, approachRadius*2);
			
			g.setColor(color);
			g.fillOval(x-radius, y-radius, radius*2, radius*2);
			
			g.setColor(new Color(255, 255, 255, alpha));
//			g.drawString(String.valueOf(eta), x, y);
			g.drawString(String.valueOf(label), x-4, y+5);
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
