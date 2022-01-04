package components;

import utils.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Separator extends JComponent {

	public Separator() {
		EventQueue.invokeLater(this::init);
	}

	private void init() {
		setPreferredSize(new Dimension(getParent().getSize().width, 9));
		setBackground(Colors.BUTTON_SECONDARY);
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		RenderingHints hints = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
		);
		g2d.setRenderingHints(hints);

		g2d.setColor(getBackground());
		g2d.fill(new Rectangle2D.Double(0, 4, getWidth(), 1) {
		});

		g2d.dispose();
	}
}
