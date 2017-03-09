import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
	public Window(int width, int height, String title, Game game) {
		super(title);
		int corrected_width = width + 6;
		int corrected_height = height + 29;
		setPreferredSize(new Dimension(corrected_width, corrected_height));
		setMinimumSize(new Dimension(corrected_width, corrected_height));
		setMaximumSize(new Dimension(corrected_width, corrected_height));
		setResizable(false);
//		setLocationRelativeTo();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(game);
	}
}
