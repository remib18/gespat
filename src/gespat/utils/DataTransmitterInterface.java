package utils;

/**
 * Permet la transmission d'une donnée
 *
 * @param <T>
 */
public interface DataTransmitterInterface<T> {

	/**
	 * Gère comment la donnée est exécutée
	 *
	 * @param data Donnée transmise
	 */
	void transmit(T data);
}
