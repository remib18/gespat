package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe utilitaire pour les expressions régulières
 */
public class Regex {

	/**
	 * Exécute une expression régulière
	 *
	 * @param regex expression régulière
	 * @param text  text où appliquer la RegEx
	 * @return la liste des résultats
	 */
	public static List<String> getMatches(String regex, String text) {
		final List<String> results = new ArrayList<>();

		// Calculs des résultats
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(text);

		// Attribution des résultats à la liste des résultats
		while (matcher.find()) {
			results.add(matcher.group(0));
		}

		return results;
	}
}
