import java.awt.*;
import java.util.LinkedList;

public class HitCircle {
	enum State {
		WAIT,
		VISIBLE,
		CLICKABLE,
		CLICKED,
		DELETEME
	}
	// BEGIN STATIC
	private static LinkedList<HitCircle> circles = new LinkedList<HitCircle>();
	public static void add(int x, int y, long time) {
		circles.add(new HitCircle(x, y, time, circles.size()));
	}
	public static void tickAll() {
		for (HitCircle circle : circles)
			circle.tick();
	}
	public static void renderAll(Graphics g) {
		for (HitCircle circle : circles)
			circle.render(g);
	}
	public static void setGameInstance(Game gameInstance) {
		game = gameInstance;
	}
	
	private long visibleTime, clickableTime, deleteTime;
	private State state;
	private Color color;
	private static Game game;
	private int alpha;
	// instance specific properties
	private long targetTime;
	private int x, y;
	private int label;
	// map dependent properties
	private double AR, OD, CS;
	private int radius;
	private int approachRadius;
	private Color approachColor;
	
	public HitCircle(int x, int y, long targetTime, int label) {
		this.targetTime = targetTime;
		this.x = x;
		this.y = y;
		this.label = label;
		// map dependent properties
		AR = 6;
		OD = 2;
		CS = 6;
		int MIN_RADIUS = 15;
		int MAX_RADIUS = 45;
		radius = (int) (MIN_RADIUS + ((10-CS)/10)*(MAX_RADIUS - MIN_RADIUS));
		// other variables
		visibleTime = this.targetTime - 1000;
		clickableTime = this.targetTime - 100;
		deleteTime = this.targetTime + 100;
		state = State.WAIT;
	}
	
	// TODO: make circles clickable
	// TODO: make circles deletable
	public void tick() {
		// calculate approach circle movement
		int delta        = (int) (targetTime - game.localTime);
		approachRadius   = (int) (radius + (AR/100)*delta);
		approachRadius   = Game.clamp(approachRadius, radius-2, 10000);
		alpha            = 255 - (delta-1000);
		alpha            = Game.clamp(alpha, 0, 255);
		approachColor    = new Color(241, 250, 140, alpha);
		
		// state system
		// TODO: state "VISIBLE" redundant?
		switch (state) {
			case WAIT:
				color = new Color(0, 0, 0, 0);
				if (game.localTime > visibleTime) {
					state = State.VISIBLE;
				}
				break;
			case VISIBLE:
				alpha = (int) (game.localTime - visibleTime);
				alpha = Game.clamp(alpha, 0, 255);
				color = new Color(0, 0, 200, alpha);
				if (game.localTime > clickableTime) {
					state = State.CLICKABLE;
				}
				break;
			case CLICKABLE:
				if (Math.hypot(game.mx - x, game.my - y) < radius)
					color = Color.WHITE;
				else 
					color = Color.CYAN;
				if (game.localTime > deleteTime) {
					state = State.DELETEME;
				}
				break;
			case CLICKED:
				// animate hit explosion
				// delete self
				break;
			case DELETEME:
				color = new Color(0, 0, 0, 0);
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
			g.drawString(String.valueOf(label), x, y);
		}
	}
}
