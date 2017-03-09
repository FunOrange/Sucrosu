import java.awt.event.*;
import java.util.Random;

class Input implements MouseMotionListener, MouseListener, KeyListener {
	private Game game;
	private Window window;
	private int mx;
	private int my;
	private boolean k1_down;
	private boolean k2_down;
	public Input(Game game, Window window) {
		this.game = game;
		this.window = window;
		k1_down = false;
		k2_down = false;
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
		// game.getCircleHandler().handleClick(e.getX(), e.getY());
//		Random rand = new Random();
//		game.getParticleHandler().add(new TimingJudge(e.getX(), e.getY(), rand.nextInt(4)));
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
		if (e.getKeyCode() == KeyEvent.VK_Z && !k1_down) {
			k1_down = true;
			game.getCircleHandler().handleClick(mx, my);
		}
		if (e.getKeyCode() == KeyEvent.VK_X && !k2_down) {
			k2_down = true;
			game.getCircleHandler().handleClick(mx, my);
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
		}
	}
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_Z)
			k1_down = false;
		if (e.getKeyCode() == KeyEvent.VK_X)
			k2_down = false;
	}
}
