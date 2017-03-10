import java.awt.*;

/**
 * Created by JR on 2017-03-10.
 */
abstract class GameObject {
	// just some things to make drawing game objects easier
	Graphics2D g;
	Game game;
	
	GameObject() {
		game = Game.getInstance();
	}
	
	public void render() {
		g = Game.getInstance().getGraphics();
		setStyle(Style.DEFAULT);
		draw();
		setStyle(Style.DEFAULT);
	}
	
	protected abstract void draw();
	
	// helper functions
	void setAlpha(float alpha) {
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	}
	
	private void setStyle(Style style) {
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
