package it.unibz.lib.bob.chatterbot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @version $Id$
 */
public class BobString {


	/**
	 * Post-process system answers before they are returned to the user. Add 3
	 * types of HTML links to the system answers, for 1. terms highlighted with
	 * <option> tags, 2. URLs that link to some url from PHRASES. in the
	 * topictree, 3. normal URLs
	 */

	// what to use as a target for HTML a elements
	private static String htmlLinkTarget = "_blank";

	private static String optionTags = "<option>([^<]*)</option>";

	// group 1: phrases; group 2: description
	private static String linkToPhrasesTags = "<linkToPhrases>(.*?)\\|(.*?)</linkToPhrases>";

	// must not match background URLs, but only URLs that appear with no
	// surrounding tags, nor surrounding " or ', nor leading = (because of
	// proxyfied URLs with &url=http:// parameter)
	private static String normalUrl = "(?<!['\">=])(http://\\S+)(?!['\"<])";

	/**
	 * The "$1" refers to matching group 1
	 */
	private static String javascriptEnabledLink = "<a href=\"javascript:sendChatTextFromClickedLink('$1')\" class=\"inline\">$1</a>";

	/**
	 * The "$1" refers to matching group 1
	 */
	private static String humanReadableLink = "<a href=\"$1\" target=\""
			+ htmlLinkTarget + "\">$2</a>";

	/**
	 * The "$1" refers to matching group 1
	 */
	private static String normalLink = "<a href=\"$1\" class=\"normal\" target=\""
			+ htmlLinkTarget + "\">$1</a>";

	/**
	 * Replace the <option>-tagged words in a string with corresponding HTML
	 * links that trigger a javascript function which supplies the tagged words
	 * to BoB. Replace URLs with regular HTML links.
	 */
	public static String makeHtmlLinks(String str) {
		Pattern optionPattern = Pattern.compile(optionTags);
		Matcher optionMatcher = optionPattern.matcher(str);
		String optionResult = optionMatcher.replaceAll(javascriptEnabledLink);

		Pattern linkToPhrasesPattern = Pattern.compile(linkToPhrasesTags);
		Matcher linkToPhrasesMatcher = linkToPhrasesPattern
				.matcher(optionResult);
		String linkToPhrasesResult = linkToPhrasesMatcher
				.replaceAll(humanReadableLink);

		Pattern urlPattern = Pattern.compile(normalUrl);
		Matcher urlPatternMatcher = urlPattern.matcher(linkToPhrasesResult);
		String urlPatternResult = urlPatternMatcher.replaceAll(normalLink);

		return urlPatternResult;
	}

	public static void main(String[] aArguments) {
		String htmlText = "hallo http://www.ling.uni-bz.it/lala/lala.html <option>ich bin</option> BoB. Ich leite dich weiter. <linkToPhrases>http://libproxy.unibz.it/login?url=http://www.gbv.de/gsomenu/opendb.php?db=2.88&amp;amp;ln=du|google</linkToPhrases> ";
		System.out.println("Old  text : " + htmlText);
		System.out.println("New  text : " + makeHtmlLinks(htmlText));
	}

}
