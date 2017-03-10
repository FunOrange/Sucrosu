import java.awt.*;

public abstract class Particle extends GameObject{
	public boolean deleteme = false;
	protected int life;
	protected double x, y;
	protected double vx = 0, vy = 0;
	protected double ax = 0, ay = 0;
	protected float alpha;
	
	public abstract void tick();
	
	protected void motion() {
		x += vx;
		vx += ax;
		y += vy;
		vy += ay;
	}
}
