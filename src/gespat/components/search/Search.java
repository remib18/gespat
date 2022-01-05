package components.search;

import components.table.AbstractTableModel;
import components.table.Table;
import components.table.TableListener;
import components.table.TableRowsFunctionsInterface;
import models.AbstractData;
import utils.threads.ThreadUtils;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Search<T extends AbstractData> implements DocumentListener {

	private final SearchBar sBar;
	private final Table<T> tableResult;
	private final List<FilterInterface<T>> filters = new ArrayList<>();
	private final List<SearchSelectedListener<T>> subscribedSelectionListeners = new ArrayList<>();
	private List<T> data;
	private List<T> lastSearchResult;
	private T selectedResult = null;

	/**
	 * Permet d'effectuer une recherche dans un tableau.
	 * Le tableau sera automatiquement créé.
	 *
	 * @param sBar        la barre de recherche liée, doit être positionnée dans un entête du template
	 * @param data        le lot de données à chercher
	 * @param tableResult un tableau pour afficher le résultat
	 */
	public Search(SearchBar sBar, List<T> data, Table<T> tableResult) {
		this.data = data;
		Collections.sort(this.data);
		this.lastSearchResult = this.data;
		this.sBar = sBar;
		this.tableResult = tableResult;
		this.tableResult.addTableListener(new TableListener<T>() {

			@Override
			public void getDataOnRowSelected(T patient) {
				selectedResult = patient;
				publishSelectionListener();
			}

			@Override
			public void onTableUpdate() { /* Unused */ }

		});

		EventQueue.invokeLater(() -> ThreadUtils.run(this::displayResult, "Search UI Load"));
	}

	/**
	 * @param listener
	 */
	public void addSelectionListener(SearchSelectedListener<T> listener) {
		subscribedSelectionListeners.add(listener);
	}

	/**
	 * Écoute chaque changement de la barre de recherche
	 * Note : je n'ai jamais vu ce cas s'exécuter, je suppose qu'il s'agit des changements effectuer par le code.
	 */
	@Override
	public void changedUpdate(DocumentEvent e) {
		removeUpdate(e);
	}

	/**
	 * Gère l'affichage du résultat
	 */
	@SuppressWarnings("unchecked")
	private void displayResult() {
		// On établit le conteneur, possible, car la barre de recherche doit toujours être dans un header de template
		final Container cont = (Container) sBar.getParent().getParent().getComponent(1);

		// On supprime l'ancien tableau
		// Le try catch gère le cas initial où il n'y a pas de tableau
		try {
			cont.remove(1);
		} catch (IndexOutOfBoundsException e) { /**/ }

		// On définit dans le tableau le nouveau jeu de donnés
		((AbstractTableModel<T>) tableResult.getModel()).setDataSet(lastSearchResult);

		// On ajoute le tableau et rafraichit le conteneur pour qu'il s'affiche
		cont.add(tableResult);
		cont.repaint();
		cont.revalidate();
	}

	/**
	 * @return le résultat de la recherche
	 */
	public List<T> getResult() {
		return this.lastSearchResult;
	}

	public T getSelected() {
		if (lastSearchResult == null) return null;
		if (!lastSearchResult.contains(selectedResult)) return null;
		return selectedResult;
	}

	/**
	 * Sélectionne la ligne
	 *
	 * @param data index de la ligne à selectionner
	 */
	public void setSelected(T data) {
		int row = this.data.indexOf(data) + 1;
		tableResult.setSelectedRow(row);
	}

	/**
	 * Sélectionne la ligne
	 *
	 * @param row index de la ligne à selectionner
	 */
	public void setSelected(int row) {
		tableResult.setSelectedRow(row + 1);
	}

	/**
	 * Écoute chaque insertion de caractère dans la barre de recherche
	 */
	@Override
	public void insertUpdate(DocumentEvent e) {
		update();

		// vérifie que la donnée sélectionnée existe toujours
		// force à récupérer à nouveau la donnée sélectionner
		publishSelectionListener();
	}

	public void update() {
		if (lastSearchResult.equals(data)) {
			lastSearchResult = search(sBar.getText(), data);
		} else {
			lastSearchResult = search(sBar.getText(), lastSearchResult);
		}

		// On trie les données
		lastSearchResult = applyFilters(lastSearchResult);
		Collections.sort(lastSearchResult);

		// On affiche
		displayResult();
	}

	public void publishSelectionListener() {
		subscribedSelectionListeners.forEach(
				listener -> listener.onSelectionChange(getSelected(), new TableRowsFunctionsInterface<T>() {
					@Override
					public int getRowIndex(T _data) {
						return data.indexOf(_data);
					}

					@Override
					public T getData(int index) {
						return data.get(index);
					}
				})
		);
	}

	/**
	 * Écoute chaque suppression de caractère dans la barre de recherche
	 */
	@Override
	public void removeUpdate(DocumentEvent e) {
		lastSearchResult = data;
		displayResult();
	}

	/**
	 * Effectue la recherche à proprement parler
	 *
	 * @param searchString la recherche de l'utilisateur
	 * @param in           les données à chercher
	 * @return les résultats de la recherche
	 */
	private List<T> search(String searchString, List<T> in) {

		List<T> result = new ArrayList<>();

		for (T object : in) {
			if (object.getSearchableFields().toLowerCase().contains(searchString.toLowerCase())) {
				result.add(object);
			}
		}
		Collections.sort(result);
		return result;
	}

	public void addFilter(FilterInterface<T> filter) {
		filters.add(filter);
		data = applyFilters(data);
	}

	private List<T> applyFilters(List<T> data) {
		for (FilterInterface<T> filter : filters) {
			data = filter.newFilter(data);
		}
		return data;
	}

}
