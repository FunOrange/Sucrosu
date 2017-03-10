import java.awt.*;

/**
 * Created by JR on 2017-03-10.
 */
public abstract class GameObject {
	// just somde drawing methods and properties
	protected Graphics2D g;
	protected Game game;
	
	public GameObject() {
		game = Game.getInstance();
		g = game.getGraphics();
	}
	
	public void render() {
		g = Game.getInstance().getGraphics();
		setStyle(Style.DEFAULT);
		draw();
	}
	public abstract void draw();
	
	// helper functions
	public void setAlpha(float alpha) {
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	}
	
	// draw style presets:
	public enum Style {
		DEFAULT,
	}
	public void setStyle(Style style) {
		switch (style) {
			case DEFAULT:
				g.setColor(Color.white);
				g.setStroke(new BasicStroke(1));
				g.setFont(new Font("monospace", Font.PLAIN, 12));
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
				break;
		}
	}
}
