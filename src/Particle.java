abstract class Particle extends GameObject{
	public boolean deleteme = false;
	int life;
	double x;
	double y;
	private double vx = 0;
	double vy = 0;
	private double ax = 0;
	private double ay = 0;
	float alpha;
	
	public abstract void tick();
	
	void motion() {
		x += vx;
		vx += ax;
		y += vy;
		vy += ay;
	}
}
