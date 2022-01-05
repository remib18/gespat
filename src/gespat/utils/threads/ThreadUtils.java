package utils.threads;

public class ThreadUtils {

	private ThreadUtils() {}

	public static void run(Runnable runnable, String name) {
		Thread thread = new Thread(runnable, name);
		thread.start();
	}
}
