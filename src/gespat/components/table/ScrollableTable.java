package components.table;

import models.AbstractData;
import utils.Colors;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.TableModel;
import java.awt.*;

public class ScrollableTable<T extends AbstractData> extends JScrollPane {

	private final Table<T> table;

	/**
	 * Gère un tableau et permet de le rendre scrollable s'il prend plus de place que disponible
	 *
	 * @param table tableau
	 */
	public ScrollableTable(Table<T> table) {
		super(table, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);
		this.table = table;

		EventQueue.invokeLater(this::init);
	}

	/**
	 * Initialisation
	 */
	private void init() {

		setViewportBorder(null);

		// Création d'un layout personnalisé pour changer la couleur de fond du tableau et le comportement de la barre de défilement
		setLayout(new ScrollPaneLayout() {
			@Override
			public void layoutContainer(Container parent) {
				JScrollPane scrollPane = (JScrollPane) parent;

				Rectangle availR = scrollPane.getBounds();
				availR.x = availR.y = 0;

				Insets insets = parent.getInsets();
				availR.x = insets.left;
				availR.y = insets.top;
				availR.width -= insets.left + insets.right;
				availR.height -= insets.top + insets.bottom;

				Rectangle vsbR = new Rectangle();
				vsbR.width = 12;
				vsbR.height = availR.height;
				vsbR.x = availR.x + availR.width - vsbR.width;
				vsbR.y = availR.y;

				if (viewport != null) {
					viewport.setBounds(availR);
				}
				if (vsb != null) {
					vsb.setVisible(true);
					vsb.setBounds(vsbR);
				}
			}
		});

		// Définit une nouvelle barre de défilement personnalisée
		getVerticalScrollBar().setUI(new BasicScrollBarUI() {
			private final Dimension d = new Dimension();

			@Override
			protected JButton createDecreaseButton(int orientation) {
				return new JButton() {
					@Override
					public Dimension getPreferredSize() {
						return d;
					}
				};
			}

			@Override
			protected JButton createIncreaseButton(int orientation) {
				return new JButton() {
					@Override
					public Dimension getPreferredSize() {
						return d;
					}
				};
			}

			@Override
			protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				// Changement de la couleur de fond de la barre de défilement
				g2.setPaint(getBackground());
				g2.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);
				g2.dispose();
			}

			@Override
			protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				JScrollBar sb = (JScrollBar) c;
				if (!sb.isEnabled() || r.width > r.height) {
					return;
				}
				// Changement de la couleur de la barre de défilement
				g2.setPaint(Colors.BUTTON_SECONDARY);
				g2.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);
				g2.dispose();
			}

			@Override
			protected void setThumbBounds(int x, int y, int width, int height) {
				super.setThumbBounds(x, y, width, height);
				scrollbar.repaint();
			}
		});

		// Suppression des bordures
		// TODO: il reste une bordure à supprimer
		setBorder(null);
		setViewportBorder(null);
		getViewport().setBorder(null);

		// Définition de la couleur d'arrière plan
		setForeground(Colors.TEXT_ON_PRIMARY);
		getViewport().setBackground(Colors.PRIMARY);
		getVerticalScrollBar().setBackground(Colors.PRIMARY);

		setBackground(Colors.PRIMARY);

		// Permet de prendre le plus de place possible (en largeur)
		setPreferredSize(new Dimension(10000, getPreferredSize().height));

		// Définit les barres de défilement
		setComponentZOrder(getVerticalScrollBar(), 0);
		setComponentZOrder(getVerticalScrollBar(), 1);

	}

	/**
	 * Ajoute un écouteur pour la modification des données
	 *
	 * @param listener écouteur
	 */
	public void addTableListener(TableListener<T> listener) {
		table.addTableListener(listener);
	}

	/**
	 * @return le modèle de donnée du tableau
	 */
	public TableModel getModel() {
		return table.getModel();
	}

	/**
	 * Définit la ligne du tableau qui est sélectionnée
	 *
	 * @param row index de la ligne
	 */
	public void setSelectedRow(int row) {
		table.setSelectedRow(row);
	}
}
