package utils;

import exceptions.ProcessingException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Date {

	private Date() {
		// Retourner une exception en cas de tentative d'instantiation de la classe.
		throw new IllegalStateException("[DATE MANAGER]: Classe utilitaire.");
	}

	/**
	 * Convertis une date en chaine de caractère.
	 *
	 */
	public static String convert(LocalDate date) {
		return date.format(DateTimeFormatter.ISO_DATE);
	}

	/**
	 * Converti une chaine de caractère en LocalDate
	 *
	 * @param str du format y-M-d ou yyyy-MM-d
	 */
	public static LocalDate convert(String str)
			throws ProcessingException {
		try {
			// Utilisation de la méthode parse pour effectuer la conversion.
			return LocalDate.parse(str, DateTimeFormatter.ISO_DATE);
		}
		// Si le format n'est pas acceptable.
		// Si la chaine de caractère ne peut être parsé.
		catch (IllegalArgumentException | DateTimeParseException e) {
			throw new ProcessingException(
					"[DATE MANAGER – CONVERT]: Un problème est survenu lors de la conversion de la date. La donnée est corrompue.");
		}
	}

	public static String getDisplayString(LocalDate date) {
		String month = "" + date.getMonthValue();
		String day = "" + date.getDayOfMonth();

		// Rajout du 0 devant si on a un mois/jour < 10
		day = day.length() > 1 ? day : "0" + day;
		month = month.length() > 1 ? month : "0" + month;

		// On retourne la date formatée
		return day + "/" + month + "/" + date.getYear();
	}
}
