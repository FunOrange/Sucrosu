import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
	public Window(int width, int height, String title, Game game) {
		super(title);
		setPreferredSize(new Dimension(width, height));
		setMinimumSize(new Dimension(width, height));
		setMaximumSize(new Dimension(width, height));
		setResizable(false);
//		setLocationRelativeTo();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		add(game);
		game.start();
	}
}
