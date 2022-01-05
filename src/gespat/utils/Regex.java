package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {

	public static List<String> getMatches(String regex, String text) {
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(text);
		final List<String> results = new ArrayList<>();

		while (matcher.find()) {
			results.add(matcher.group(0));
		}
		return results;
	}
}
