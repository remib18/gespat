package components;

import utils.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Separator extends JComponent {

	/**
	 * Composant servant à séparer deux autres composants
	 */
	public Separator() {
		EventQueue.invokeLater(this::init);
	}

	/**
	 * Initialisation de la taille et de la couleur du séparateur
	 */
	private void init() {
		try {
			setPreferredSize(new Dimension(10000, 9));
		} catch (NullPointerException err) { /**/ }
		setBackground(Colors.BUTTON_SECONDARY);
		repaint();
	}

	/**
	 * Dessine le séparateur
	 */
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
