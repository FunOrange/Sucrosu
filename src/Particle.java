import java.awt.*;

public abstract class Particle {
	public boolean deleteme = false;
	protected int life;
	protected double x;
	protected double y;
	protected double vx = 0;
	protected double vy = 0;
	protected double ax = 0;
	protected double ay = 0;
	protected float alpha;
	
	public abstract void tick();
	
	protected void motion() {
		x += vx;
		vx += ax;
		y += vy;
		vy += ay;
	}
	
	public abstract void render(Graphics g);
	
	protected AlphaComposite getAlphaComposite(float alpha) {
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type, alpha));
	}
}
