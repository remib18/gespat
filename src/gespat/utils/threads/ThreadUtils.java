package utils.threads;

/**
 * Classe utilitaire simplifiant l'utilisation de threads
 */
public class ThreadUtils {

	private ThreadUtils() {
		throw new IllegalStateException("Classe utilitaire.");
	}

	/**
	 * Création et execution d'un thread.
	 *
	 * @param runnable le code à exécuter
	 * @param name     le nom du thread
	 */
	public static void run(Runnable runnable, String name) {
		Thread thread = new Thread(runnable, name);
		thread.start();
	}
}
