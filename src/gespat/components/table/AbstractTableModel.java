package components.table;

import models.AbstractData;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTableModel<T extends AbstractData> extends javax.swing.table.AbstractTableModel {

	private static final long serialVersionUID = 8145852153886651759L;

	/**
	 * Bouton supprimer en fin de ligne
	 */
	protected final JButton delete = new JButton("Supprimer");

	/**
	 * Liste de données
	 */
	protected List<T> data;

	/**
	 * Liste des noms de colonne
	 */
	protected final List<String> headers = new ArrayList<>();

	/**
	 * @return le nombre de colones
	 */
	@Override
	public int getColumnCount() {
		return headers.size();
	}

	/**
	 * @param columnIndex l'index de la colonne
	 * @return le nom de la colonne liée
	 */
	@Override
	public String getColumnName(int columnIndex) {
		return headers.get(columnIndex);
	}

	/**
	 * @return l'ensemble des données du tableau
	 */
	public List<T> getData() {
		return data;
	}

	/**
	 * @return le nombre de lignes
	 */
	@Override
	public int getRowCount() {
		return data.size() + 1;
	}

	/**
	 * Définit un nouveau jeu de données pour le tableau
	 *
	 * @param data la liste des données contenues dans le tableau
	 * @apiNote cette méthode supprime les anciennes données
	 */
	public void setDataSet(List<T> data) {
		this.data = data;
	}

}
