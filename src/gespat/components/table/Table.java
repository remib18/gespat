package components.table;

import models.AbstractData;
import utils.Colors;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Table<T extends AbstractData> extends JTable {

	private static final long serialVersionUID = -4513960817544981475L;

	/**
	 * Liste des écouteurs
	 */
	protected final List<TableListener<T>> tableListeners = new ArrayList<>();

	private final int colNumb;

	/**
	 * Création d'un tableau selon le model fournit
	 *
	 * @param model
	 * @see AbstractTableModel
	 */
	public Table(AbstractTableModel<T> model) {
		// On définit le modèle
		this.setModel(model);
		this.colNumb = this.getColumnModel().getColumnCount();

		init();

		// Handling user selection
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getSelectionModel().addListSelectionListener(e -> publishSelectedData());
	}


	/**
	 * Ajoute un écouteur
	 *
	 * @param listener
	 */
	public void addTableListener(TableListener<T> listener) {
		tableListeners.add(listener);
	}

	/**
	 * Création du tableau
	 */
	private void init() {
		// Définition des couleurs
		this.setBackground(Colors.PRIMARY);
		this.setForeground(Colors.TEXT_ON_PRIMARY);
		setSelectionBackground(Colors.SECONDARY);
		setSelectionForeground(Colors.TEXT_ON_SECONDARY);

		// Paramétrage de la colonne Action pour afficher des boutons

		// Choix des tailles de colonnes
		// Remarque : les tailles sont volontairement élevées pour que le tableau remplisse la colonne
		for (int i = 0; i < colNumb; i++) {
			if (i == colNumb - 3) {
				this.getColumnModel().getColumn(i).setPreferredWidth(500);
			} else {
				this.getColumnModel().getColumn(i).setPreferredWidth(200);
				this.getColumnModel().getColumn(i).setMinWidth(this.getColumnModel().getColumn(i).getWidth());
			}
		}

		// Choix de la taille des lignes
		this.setRowHeight(35);

		// Dessin de la grille du tableau
		this.setGridColor(Colors.SECONDARY);
		this.setShowVerticalLines(false);
		this.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
	}

	/**
	 * Publie l'évènement SelectedData
	 */
	@SuppressWarnings("unchecked")
	private void publishSelectedData() {
		try {
			int rowIndex = getSelectedRow();
			T data = ((AbstractTableModel<T>) getModel()).getData().get(rowIndex - 1);
			tableListeners.forEach(action -> action.getDataOnRowSelected(data));
		} catch (IndexOutOfBoundsException e) {
			/* Protège d'une exception si la barre de titre est selectionnée */
		}
	}

	/**
	 * Sélectionne la ligne
	 * @param row index de la ligne à selectionner
	 */
	public void setSelectedRow(int row) {
		row++;
		try {
			addRowSelectionInterval(row, row);
		} catch (IllegalArgumentException err) {
						Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.WARNING, "Impossible de sélectionner la donnée inexistante dans la table (id: " + row + ").");
		}
	}
}
