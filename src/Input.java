import java.awt.event.*;

public class Input implements MouseMotionListener, MouseListener, KeyListener {
	private Game game;
	int mx, my;
	boolean k1_down, k2_down;
	public Input(Game game) {
		this.game = game;
		k1_down = false;
		k2_down = false;
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
		// game.getCircleHandler().handleClick(e.getX(), e.getY());
	}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
	}
	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_C && k1_down == false) {
			k1_down = true;
			game.getCircleHandler().handleClick(mx, my);
		}
		if (e.getKeyCode() == KeyEvent.VK_V && k2_down == false) {
			k2_down = true;
			game.getCircleHandler().handleClick(mx, my);
		}
	}
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_C)
			k1_down = false;
		if (e.getKeyCode() == KeyEvent.VK_V)
			k2_down = false;
	}
}
