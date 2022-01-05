package components.template;

import utils.threads.ThreadUtils;

import javax.swing.*;
import java.awt.*;

public class XSpaceBetween extends JPanel {

	// Définition des positionnements
	public static final String WEST = BorderLayout.WEST;
	public static final String EAST = BorderLayout.EAST;

	public XSpaceBetween(Component[] cps) {
		init();

		setLayout(new BorderLayout());
		add(cps[0], BorderLayout.WEST);
		add(cps[1], BorderLayout.EAST);
	}

	public XSpaceBetween(Component a, Component b) {
		init();

		setLayout(new BorderLayout());
		add(a, BorderLayout.WEST);
		add(b, BorderLayout.EAST);
	}

	/**
	 * Initialisation du composant
	 */
	private void init() {
		setLayout(new BorderLayout());
		setOpaque(true);
		EventQueue.invokeLater(() -> ThreadUtils.run(() -> {
			try {
				setPreferredSize(new Dimension(10000, getPreferredSize().height));
				setMinimumSize(new Dimension(getParent().getSize().width - 32, getPreferredSize().height));
				setBackground(getParent().getBackground());
			} catch (NullPointerException err) { /**/ }
		}, "XSpaceBetween UI Load"));
	}

	@Override
	public void add(Component comp, Object constraints) {
		switch ((String) constraints) {
			case WEST:
			case EAST:
				super.add(comp, constraints);
				break;
		}
	}
}
