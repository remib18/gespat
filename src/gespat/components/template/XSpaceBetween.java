package components.template;

import javax.swing.*;
import java.awt.*;

public class XSpaceBetween extends JPanel {

    public static final String WEST = BorderLayout.WEST;
    public static final String EAST = BorderLayout.EAST;

    public XSpaceBetween() {
        init();
    }

    public XSpaceBetween(Component a, Component b) {
        init();

        setLayout(new BorderLayout());
        add(a, BorderLayout.WEST);
        add(b, BorderLayout.EAST);
    }

    private void init() {
        setLayout(new BorderLayout());
        setOpaque(true);
        EventQueue.invokeLater(() -> {
            try {
                setMinimumSize(new Dimension(getParent().getSize().width - 32, getPreferredSize().height));
                setBackground(getParent().getBackground());
            } catch (NullPointerException err) { /**/ }
        });
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
