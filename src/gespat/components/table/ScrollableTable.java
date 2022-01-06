package components.table;

import models.AbstractData;
import utils.Colors;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.TableModel;
import java.awt.*;

public class ScrollableTable<T extends AbstractData> extends JScrollPane {

	private final Table<T> table;

	public ScrollableTable(Table<T> table) {
		super(table, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);
		this.table = table;

		EventQueue.invokeLater(this::init);
	}

	private void init() {

		setViewportBorder(null);

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
		setForeground(Colors.TEXT_ON_PRIMARY);
		setBackground(Colors.PRIMARY);
		setBorder(null);
		setViewportBorder(null);

		getViewport().setBackground(Colors.PRIMARY);
		getViewport().setBorder(null);

		setPreferredSize(new Dimension(10000, getPreferredSize().height));

		getVerticalScrollBar().setBackground(Colors.PRIMARY);

		setComponentZOrder(getVerticalScrollBar(), 0);
		setComponentZOrder(getVerticalScrollBar(), 1);

	}

	public void addTableListener(TableListener<T> listener) {
		table.addTableListener(listener);
	}

	public TableModel getModel() {
		return table.getModel();
	}

	public void setSelectedRow(int row) {
		table.setSelectedRow(row);
	}
}
