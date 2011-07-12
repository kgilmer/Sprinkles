/*
 * Copyright (c) 2011, Ken Gilmer
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer 
 * in the documentation and/or other materials provided with the distribution. Neither the name of the Ken Gilmer nor the names 
 * of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
