import java.awt.*;

class TimingJudge extends Particle {
	private int type;
	private Color color;
	
	public TimingJudge(int x, int y, int type) {
		this.type = type;
		this.x = (double) x;
		this.y = (double) y;
		life = 255;
		switch (type) {
			case 0:     color = new Color(255, 0, 0);       break;
			case 1:     color = new Color(255, 166, 55);    break;
			case 2:     color = new Color(20, 255, 22);     break;
			case 3:     color = new Color(15, 0, 255);      break;
		}
	}
	
	@Override
	public void tick() {
		motion();
		vy = 0.15;
		life--;
		alpha = (float) Game.clamp(life, 0, 255) / 255.0f;
		if (life < 0)
			deleteme = true;
	}
	
	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		int radius = 4;
		g2d.setComposite(getAlphaComposite(alpha));
		g2d.setColor(color);
		switch (type) {
			case 0:
				g2d.drawString("MISS", (int) x, (int) y);
				break;
			case 1:
				g2d.drawString("300!", (int) x, (int) y);
				break;
			case 2:
				g2d.drawString("100", (int) x, (int) y);
				break;
			case 3:
				g2d.drawString("50", (int) x, (int) y);
				break;
		}
		g2d.setComposite(getAlphaComposite(1.0f));
	}
	
}
