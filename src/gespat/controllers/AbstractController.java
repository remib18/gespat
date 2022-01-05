package controllers;

import components.table.TableListener;
import exceptions.ConflictingDataException;
import exceptions.NotFoundException;
import exceptions.ProcessingException;
import models.AbstractData;
import utils.File;
import utils.StateManager;
import views.popups.UserMessage;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractController<T extends AbstractData> {

    protected static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /** Liste des données */
    protected final List<T> data = new ArrayList<>();

    /** Fichier de stockage */
    protected String storeFile;

    /** Clé utilisée pour utiliser la state */
    protected StateManager.DataType stateDataType;

    /** Listeners sur la mise à jour des données */
    protected final List<TableListener<T>> tableUpdateListeners = new ArrayList<>();


    /**
     * Ajoute la donnée.
     * @param object donnée à ajouter.
     *
     * @throws ConflictingDataException si une donnée avec le même identifiant existe déjà.
     * @throws ProcessingException en cas de problème lors de la sauvegarde ou du chargement des données
     */
    protected T add(T object) throws ConflictingDataException, ProcessingException {
        try {
            // Vérification de l'existence de la donnée
            get(object.getId());
            StateManager.getState().narrowNextInsertionIndex(stateDataType);
            throw new ConflictingDataException("Une donnée avec le même identifiant existe déjà.");
        } catch (NotFoundException e) {
            data.add(object);
        }
        save();
        publishTableUpdate();
        return object;
    }

    /**
     * Ajoute un évènement permettant d'écouter les mises à jour de données
     * Utile dans le cas de plusieurs instances
     * @param listener l'évènement
     */
    public void addTableUpdateListener(TableListener<T> listener) {
        tableUpdateListeners.add(listener);
    }

    /**
     * Supprime l'ensemble des données
     * @throws ProcessingException en cas de problème lors de la sauvegarde ou du chargement des données
     */
    public void clear() throws ProcessingException {
        data.clear();
        StateManager.getState().clearData(stateDataType);
        save();
        publishTableUpdate();
    }

    /**
     * Retourne la donnée
     * @param id identifiant
     * @return   donnée correspondente
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

    /**
     * Charge le jeu de données à partir du fichier.
     * @throws ProcessingException si les données sont impossibles à charger.
     */
    protected void load() throws ProcessingException {
        data.clear();
        final String[][] newData = (new File<T>()).getData(storeFile);
        if (newData == null) {
            return;
        }
        for (String[] object : newData) {
            try {
                add(makeObjectFromString(object));
            }
            catch (ConflictingDataException | NullPointerException | ArrayIndexOutOfBoundsException e) { /**/ }
            catch (NotFoundException | DateTimeParseException e) {
                new UserMessage("Une donnée corrompue à été interceptée et supprimée.", UserMessage.LEVEL.Warning);
                logger.log(Level.WARNING, "Donnée corrompue interceptée et supprimée.");
            }
        }
    }

    /**
     * Converti un tableau de chaine de caractère en donnée.
     * @param object la donnée.
     * @return l'objet correspondant.
     */
    protected abstract T makeObjectFromString(String[] object)
            throws NotFoundException, NumberFormatException, ProcessingException;

    /**
     * Publie l'évènement TableUpdate
     */
    protected void publishTableUpdate() {
        tableUpdateListeners.forEach(TableListener::onTableUpdate);
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
     * @throws NotFoundException si la donnée à supprimer n'existe pas.
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
     * @throws ProcessingException si les données sont impossibles à sauvegarder.
     */
    protected void save() throws ProcessingException {
        (new File<T>()).saveData(data, storeFile);
    }

    /**
     * Met à jour les informations d'une donnée
     * @param data la donnée modifiée
     * @throws ProcessingException
     * @throws NotFoundException
     */
    public void update(T data) throws NotFoundException, ProcessingException {
        remove(data.getId(), true);
        try {
            add(data);
        } catch (ConflictingDataException e) {/* La donnée ne peut être en conflit, car elle a été supprimée préalablement */}
    }

}
