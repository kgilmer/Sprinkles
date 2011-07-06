package org.sprinkles.functions;

import java.util.Collection;

import org.sprinkles.Fn;
import org.sprinkles.Fn.FoldFunction;

/**
 * @author kgilmer
 *
 */
public class StringFunctions {

	/**
	 * Convenience method to call fold on the JoinFn.
	 * @param in
	 * @param delimiter
	 * @return
	 */
	public static String join(Collection<String> in, String delimiter) {
		return Fn.fold(new JoinFn(delimiter), in).toString();
	}
	
	/**
	 * Create a string of the elements of a String collection with a delimiter between each entry.
	 */
	public static final class JoinFn implements FoldFunction<String, StringBuilder> {

		private final String delimiter;

		public JoinFn(String delimiter) {
			this.delimiter = delimiter;
		}
		
		@Override
		public StringBuilder apply(String element, StringBuilder result) {
			if (result == null)
				result = new StringBuilder();
			
			result.append(element);
			result.append(delimiter);
			
			return result;
		}
		
	}
}
