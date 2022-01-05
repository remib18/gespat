package components;

import utils.Colors;
import utils.threads.ThreadUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Button extends JButton {

	private static final long serialVersionUID = 7968751094886354977L;
	private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static final Size defaultSize = Size.LARGE;
	private static final Style defaultStyle = Style.FILLED;
	private static final Color defaultColor = Color.SECONDARY;

	private boolean styleOutline = false;
	private int padding;
	private double outlineStrokeWidth;
	private Position position = Position.CENTER;

	public enum Color {PRIMARY, SECONDARY, SUCCESS, INFO, WARNING, DANGER}

	public enum Position {START, CENTER, END}

	public enum Size {SMALL, LARGE}

	public enum Style {FILLED, OUTLINED}

	/**
	 * Créer un nouveau bouton
	 *
	 * @param text titre du bouton
	 */
	public Button(String text) {
		super(text);
		EventQueue.invokeLater(() -> ThreadUtils.run(() -> init(defaultSize, defaultStyle, defaultColor), "Button UI Load"));
	}

	/**
	 * Crée un nouveau bouton
	 *
	 * @param text titre du bouton
	 * @param size taille du bouton parmis <code>ButtonSizes</code>
	 */
	public Button(String text, Size size) {
		super(text);
		EventQueue.invokeLater(() -> ThreadUtils.run(() -> init(size, defaultStyle, defaultColor), "Button UI Load"));
	}

	/**
	 * Crée un nouveau bouton
	 *
	 * @param text  titre du bouton
	 * @param size  taille du bouton
	 * @param style style du bouton
	 */
	public Button(String text, Size size, Style style) {
		super(text);
		EventQueue.invokeLater(() -> ThreadUtils.run(() -> init(size, style, defaultColor), "Button UI Load"));
	}

	/**
	 * Crée un nouveau bouton
	 *
	 * @param text  titre du bouton
	 * @param size  taille du bouton
	 * @param style style du bouton
	 * @param color couleur du bouton
	 */
	public Button(String text, Size size, Style style, Color color) {
		super(text);
		EventQueue.invokeLater(() -> ThreadUtils.run(() -> init(size, style, color), "Button UI Load"));
	}

	/**
	 * @return la couleur de fond du parent
	 */
	private java.awt.Color getParentBackground() {
		return this.getParent().getBackground();
	}

	/**
	 * @return la taille réelle du composant
	 */
	public int getTrueWidth() {
		Graphics2D g2d = (Graphics2D) getGraphics();
		return g2d.getFontMetrics().stringWidth(this.getText()) + 2 * padding;
	}

	/**
	 * @return la position du bouton pour qu'il soit centré
	 */
	private int getXPos() {
		switch (position) {
			case CENTER:
				return (this.getWidth() - this.getTrueWidth()) / 2;
			case END:
				return this.getWidth() - this.getTrueWidth();
			case START:
			default:
				return 0;
		}
	}

	/**
	 * Création du bouton avec les paramètres
	 *
	 * @param size
	 * @param style
	 * @param color
	 */
	private void init(Size size, Style style, Color color) {
		// Annulation des styles par défaut du bouton
		// Pour permettre les coins arrondis
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		this.setOpaque(false);

		// Choix de la taille du bouton
		// Définit aussi la taille de la police
		switch (size) {
			case SMALL:
				this.padding = 10;
				this.outlineStrokeWidth = 1.5;
				this.setFont(new Font("Arial", Font.PLAIN, 14));
				this.setPreferredSize(new Dimension(0, 21));
				break;

			case LARGE:
				this.padding = 15;
				this.outlineStrokeWidth = 2;
				this.setFont(new Font("Arial", Font.PLAIN, 16));
				this.setPreferredSize(new Dimension(0, 41));
				break;
		}

		// Choix du style du boutton
		switch (style) {
			case FILLED:
				break;

			case OUTLINED:
				this.styleOutline = true;
				break;
		}

		switch (color) {
			case PRIMARY:
				this.setBackground(Colors.BUTTON_PRIMARY);
				this.setForeground(this.styleOutline ? Colors.TEXT_ON_SECONDARY : Colors.TEXT_ON_PRIMARY);
				break;

			case SECONDARY:
				this.setBackground(Colors.BUTTON_SECONDARY);
				this.setForeground(this.styleOutline ? Colors.TEXT_ON_PRIMARY : Colors.TEXT_ON_SECONDARY);
				break;

			case SUCCESS:
			case INFO:
			case WARNING:
				logger.log(Level.SEVERE, "Couleur non implémentée.");
				break;

			case DANGER:
				this.setBackground(Colors.BUTTON_DANGER);
				this.setForeground(Colors.BUTTON_DANGER);
				break;
		}
	}

	/**
	 * Dessine le bouton
	 */
	@Override
	protected void paintComponent(Graphics g) {
		// Préparation
		Graphics2D g2d = (Graphics2D) g.create();
		RenderingHints hints = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
		);
		g2d.setRenderingHints(hints);

		// Change la couleur de dessin par celle du fond du bouton
		g2d.setColor(getBackground());

		// Calcul de la largeur du bouton
		double w = this.getTrueWidth();

		// Dessine un rectangle arrondi
		g2d.fill(new RoundRectangle2D.Double(
				this.getXPos(), 0,
				w, (double) getHeight() - 1,
				15, 15
		));

		// Si le bouton est de style outlined,
		// Dessine un autre rectangle arrondi au milieu du précédent avec la couleur du parent
		if (this.styleOutline) {
			double stroke = this.outlineStrokeWidth * 2;
			double radius = 15 - this.outlineStrokeWidth;

			g2d.setColor(getParentBackground());

			//noinspection SuspiciousNameCombination
			g2d.fill(new RoundRectangle2D.Double(
					this.getXPos() + this.outlineStrokeWidth,
					this.outlineStrokeWidth,
					w - stroke,
					getHeight() - 1 - stroke,
					radius, radius
			));
		}

		// Paramétrage de la couleur du texte
		g2d.setColor(getForeground());

		// Calcul du placement du texte en horizontal
		int y = this.getHeight() / 2 + this.getFont().getSize() / 2 - 2;

		// Dessin du texte
		g2d.drawString(this.getText(), this.getXPos() + padding, y);

		// On a terminé
		g2d.dispose();
	}

	/**
	 * Retourne le bouton avec une position définie
	 *
	 * @param pos position du bouton
	 */
	public Button setPosition(Position pos) {
		this.position = pos;
		this.repaint();
		return this;
	}

	public int getTrueX() {
		return getX() + getXPos();
	}
}
