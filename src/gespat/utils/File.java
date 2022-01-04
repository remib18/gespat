package utils;

import exceptions.ProcessingException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class File<T> {

    /** Séparateur de champ de données */
    public static final String COLUMN_SEPARATOR = ";";

    /**
     * Retourne une liste de données d'un fichier.
     * @param file
     * @return
     * @throws ProcessingException
     */
    public String[][] getData(String file) throws ProcessingException {
        try (
                FileReader fr = new FileReader(file);
                Scanner sc = new Scanner(fr)
        ) {

            ArrayList<String[]> strs = new ArrayList<>();

            while (sc.hasNextLine()) {
                final String[] dataPart = sc.nextLine().split(COLUMN_SEPARATOR);
                strs.add(dataPart);
            }
            if (strs.isEmpty()) {
                return null;
            }
            return strs.toArray(new String[strs.get(0).length][0]);
        } catch (IOException e) {
            try (
                    FileWriter fw = new FileWriter(file)
            ) {
                fw.write("");
            } catch (Exception err) {
                throw new ProcessingException("[FILE – SAVE DATA]: Impossible de créer le fichier.");
            }
            throw new ProcessingException("[FILE – SAVE DATA]: Le fichier n'existe pas, un nouveau va être créer.");
        }
    }

    /**
     * Enregistre une liste de données dans un fichier.
     * @param data
     * @param file
     * @throws ProcessingException
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
