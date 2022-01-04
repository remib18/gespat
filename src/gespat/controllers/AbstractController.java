package controllers;

import components.table.TableListener;
import exceptions.ConflictingDataException;
import exceptions.NotFoundException;
import exceptions.ProcessingException;
import models.Data;
import utils.File;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractController<T extends Data> {

    /** Liste des données */
    protected List<T> data = new ArrayList<>();

    /** Fichier de stockage */
    protected String storeFile;

    /** Listeners sur la mise à jour des données */
    protected List<TableListener> tableUpdateListeners = new ArrayList<>();


    /**
     * Ajoute la donnée.
     * @param object donnée à ajouter.
     * @throws ConflictingDataException si une donnée avec le même identifiant existe déjà.
     * @throws ProcessingException
     */
    public void add(T object) throws ConflictingDataException, ProcessingException {
        try {
            get(object.getId());
            throw new ConflictingDataException("[ABS CTRL — ADD]: Une donnée avec le même identifiant existe déjà.");
        } catch (NotFoundException e) {
            data.add(object);
        }
        save();
        publishTableUpdate();
    }

    /**
     * Ajoute un évènement permetant d'écouter les mises à jours de données
     * Utile dans le cas de plusieurs instances
     * @param listener
     */
    public void addTableUpdateListener(TableListener listener) {
        tableUpdateListeners.add(listener);
    }

    /**
     * Supprime l'ensemble des données
     * @throws ProcessingException
     * @hidden
     */
    public void clear() throws ProcessingException {
        data.clear();
        save();
        publishTableUpdate();
    }

    /**
     * Retourne la donnée
     * @param id identifiant
     * @return donnée corespondante
     * @throws NotFoundException si la donnée n'a pas été trouvée.
     */
    public T get(int id) throws NotFoundException {
        for (T object : data) {
            if (object.getId() == id) {
                return object;
            }
        }
        throw new NotFoundException("[ABS CTRL — GET]: Impossible de trouver la donnée rechercher.");
    }

    /**
     * @return l'ensemble du jeu de données
     */
    public List<T> getAll() {
        return data;
    }

    protected int getLastInsertedIndex() {
        return data.size() - 1;
    }

    /**
     * Charge le jeu de données à partir du fichier.
     * @throws ProcessingException si les données sont impossible à charger.
     */
    protected void load() throws ProcessingException {
        clear();
        final String[][] newData = (new File<T>()).getData(storeFile);
        if (newData == null) {
            return;
        }
        for (String[] object : newData) {
            try {
                add(makeObjectFromString(object));
            }
            catch (ConflictingDataException | NullPointerException | ArrayIndexOutOfBoundsException e) { /**/ }
            catch (NotFoundException e) {
                System.err.println("[ABS CTRL — LOAD]: Donnée corrompu interceptée et supprimée.");
            }
        }
    }

    /**
     * Converti un tableau de chaine de caractère en donnée.
     * @param object la donnée.
     * @return l'objet correpondant.
     */
    protected abstract T makeObjectFromString(String[] object)
            throws NotFoundException, NumberFormatException, ProcessingException;

    /**
     * Publie l'évènement TableUpdate
     */
    protected void publishTableUpdate() {
        tableUpdateListeners.forEach(item -> item.onTableUpdate());
    }

    /**
     * Supprime la donnée.
     * @param id identifiant de la donnée à supprimer.
     * @param confirmation validation de l'utilisateur via l'interface.
     * @throws NotFoundException si la donnée à supprimer n'existe pas.
     * @throws ProcessingException
     */
    public void remove(int id, boolean confirmation) throws NotFoundException, ProcessingException {
        if (confirmation) {
            data.remove(get(id));
        }
        save();
        publishTableUpdate();
    }

    /**
     * Supprime la donnée.
     * @param object donnée à supprimer.
     * @param confirmation validation de l'utilisateur via l'interface.
     * @throws NotFoundException si l donnée à supprimer n'existe pas.
     * @throws ProcessingException
     */
    public void remove(T object, boolean confirmation) throws NotFoundException, ProcessingException {
        if (confirmation) {
            boolean operationResult = data.remove(object);
            if (!operationResult) throw new NotFoundException("[ABS CTRL — REMOVE]: Impossible de trouver la donnée rechercher.");
        }
        save();
        publishTableUpdate();
    }

    /**
     * Enregistre le jeu de données dans le fichier.
     * @throws ProcessingException si les données sont impossible à sauvegarder.
     */
    protected void save() throws ProcessingException {
        (new File<T>()).saveData(data, storeFile);
    }

    /**
     * Met à jour les informations d'une donnée
     * @param data la donnée mofifiée
     * @throws ProcessingException
     * @throws NotFoundException
     */
    public void update(T data) throws NotFoundException, ProcessingException {
        remove(data.getId(), true);
        try {
            add(data);
        } catch (ConflictingDataException e) {/* La donnée ne peut être en conflit car elle a été supprimé préalablement */}
    }

}