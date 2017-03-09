import javax.swing.*;
import java.awt.*;

class Window extends JFrame {
	public Window(Game game) {
		super("Sucrosu");
		int corrected_width = Game.WIDTH + 6;
		int corrected_height = Game.HEIGHT + 29;
		setPreferredSize(new Dimension(corrected_width, corrected_height));
		setMinimumSize(new Dimension(corrected_width, corrected_height));
		setMaximumSize(new Dimension(corrected_width, corrected_height));
		setResizable(false);
//		setLocationRelativeTo();
		setVisible(true);
//		setState()
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		add(game);
	}
}
