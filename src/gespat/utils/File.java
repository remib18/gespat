package utils;

import exceptions.ProcessingException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class File<T> {

	/**
	 * Séparateur de champ de données
	 */
	public static final String COLUMN_SEPARATOR = ";";

	/**
	 * Retourne une liste de données d'un fichier.
	 *
	 * @param file fichier
	 * @return les données sous la forme <code>[ligne][index de la donnée]</code>
	 * @throws ProcessingException en cas d'erreur de chargement des données
	 */
	public String[][] getData(String file) throws ProcessingException {
		try (
				final FileReader fr = new FileReader(file);
				final Scanner sc = new Scanner(fr)
		) {
			// Tableau de retours
			final ArrayList<String[]> strs = new ArrayList<>();

			while (sc.hasNextLine()) {
				// Séparation des données de la ligne
				final String[] dataPart = sc.nextLine().split(COLUMN_SEPARATOR);

				strs.add(dataPart);
			}

			if (strs.isEmpty()) {
				return null;
			}

			// On retourne la liste des résultats transformée en Array
			return strs.toArray(new String[strs.get(0).length][0]);
		} catch (IOException e) {
			// Si on n'a pas réussi à accéder au fichier, on le crée.
			try (
					final FileWriter fw = new FileWriter(file)
			) {
				fw.write("");
			} catch (Exception err) {
				throw new ProcessingException("Impossible de créer le fichier.");
			}
			// Si aucune erreur ne se produit, on ré-execute la méthode
			return getData(file);
			// throw new ProcessingException("Le fichier n'existe pas, un nouveau va être créer.");
		}
	}

	/**
	 * Enregistre une liste de données dans un fichier.
	 *
	 * @param data données à enregistrés
	 * @param file fichier
	 * @throws ProcessingException en cas d'erreur de chargement des données
	 */
	public void saveData(List<T> data, String file) throws ProcessingException {
		try (
				FileWriter fw = new FileWriter(file)
		) {
			for (T entry : data) {
				fw.write(entry.toString());
				fw.write('\n');
			}
		} catch (IOException e) {
			throw new ProcessingException("[FILE – SAVE DATA]: Impossible d'écrire dans le fichier.");
		}
	}
}
