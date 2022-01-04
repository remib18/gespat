package components.template;

import components.inputs.Text;
import components.Label;
import exceptions.FormatException;
import utils.Colors;

import javax.swing.JPanel;
import java.awt.Component;
import java.awt.BorderLayout;
import javax.swing.text.JTextComponent;
import java.time.LocalDate;

public class SidebarRow extends JPanel {

    private static final long serialVersionUID = 2835082161395088285L;

    /**
     * Crée une ligne de donnée date non modifiable pour la sidebar
     *
     * @param label
     * @param date  date par défaut
     */
    public SidebarRow(String label, LocalDate date) {
        initDate(label, date, false);
    }

    /**
     * Crée une ligne de donnée date pour la sidebar
     *
     * @param label
     * @param date    date par défaut
     * @param canEdit définit si la donnée est modifiable
     */
    public SidebarRow(String label, LocalDate date, boolean canEdit) {
        initDate(label, date, canEdit);
    }

    /**
     * Crée une ligne de donnée texte non modifiable pour la sidebar
     *
     * @param label
     * @param text  texte par défaut
     */
    public SidebarRow(String label, String text) {
        init(label, text, false);
    }

    /**
     * Crée une ligne de donnée texte pour la sidebar
     *
     * @param label
     * @param text    texte par défaut
     * @param canEdit définit si la donnée est modifiable
     */
    public SidebarRow(String label, String text, boolean canEdit) {
        init(label, text, canEdit);
    }

    /**
     * @apiNote ne fonctionne que dans le cas d'une donnée date
     * @return la date
     * @throws FormatException
     */
    public LocalDate getDate() throws FormatException {
        JPanel panel = ((JPanel) getComponent(1));
        try {
            int day = Integer.parseInt(((Text) panel.getComponent(0)).getText());
            int month = Integer.parseInt(((Text) panel.getComponent(1)).getText());
            int year = Integer.parseInt(((Text) panel.getComponent(2)).getText());
            return LocalDate.of(year, month, day);
        } catch (java.lang.NumberFormatException | java.time.DateTimeException e) {
            /* Throw the exception */ }
        throw new FormatException("[SIDEBAR ROW — GET DATE]: You must provide a valid date.");
    }

    /**
     * @param data
     * @param canEdit
     * @return la donnée en fonction de son éditabilité
     */
    private Component getDateField(int data, boolean canEdit) {
        return getDateField("" + data, canEdit);
    }

    private Component getDateField(String data, boolean canEdit) {
        Component field;
        if (canEdit) {
            field = new Text(40, 20);
            field.setForeground(Colors.TEXT_ON_PRIMARY);
            ((Text) field).setText(data);
            return field;
        }
        return new Label(data);
    }

    /**
     * @apiNote ne fonctionne que dans le cas d'une donnée texte ou numérique
     * @return le texte
     */
    public String getText() {
        Component cp = getComponent(1);
        try {
            return ((Text) cp).getText();
        } catch (ClassCastException err) {
            return ((Label) cp).getText();
        }
    }

    /**
     * Initialise le composant (texte)
     *
     * @param label
     * @param text
     * @param canEdit
     */
    private void init(String label, String text, boolean canEdit) {
        setOpaque(false);
        Component data;

        if (canEdit) {
            data = new Text(50, 20);
            data.setForeground(Colors.TEXT_ON_PRIMARY);
            ((Text) data).setText(text);
        } else {
            data = new Label(text);
        }

        setLayout(new BorderLayout());
        add(new Label(label), BorderLayout.WEST);
        add(data, BorderLayout.EAST);
    }

    /**
     * Initialise le composant (date)
     *
     * @param label
     * @param date
     * @param canEdit
     */
    private void initDate(String label, LocalDate date, boolean canEdit) {
        setOpaque(false);

        JPanel inputs = new JPanel();
        inputs.setOpaque(false);
        try {
            inputs.add(getDateField(date.getDayOfMonth(), canEdit));
            inputs.add(getDateField(date.getMonthValue(), canEdit));
            inputs.add(getDateField(date.getYear(), canEdit));
        } catch (NullPointerException err) {
            inputs.add(getDateField("Jour", true));
            inputs.add(getDateField("Mois", true));
            inputs.add(getDateField("Année", true));
        }

        setLayout(new BorderLayout());
        add(new Label(label), BorderLayout.WEST);
        add(inputs, BorderLayout.EAST);
    }

}
