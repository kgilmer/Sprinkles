package org.sprinkles.collection;

import java.util.Collection;
import java.util.List;

import org.sprinkles.Fn.Function;

public interface FunctionalCollection<E> extends List<E> {

	/**
	 * Apply a function to an object and return an object.
	 * 
	 * @author kgilmer
	 * 
	 */
	/*public interface Function<I, O> {
		*//**
		 * @param <T>
		 * @param element
		 * @return result of function. If null is returned nothing is added to
		 *         result.
		 *//*
		public O apply(I element);
	}*/
	
	/**
	 * A function in a fold operation.
	 * 
	 * @author kgilmer
	 * 
	 */
	/*public interface FoldFunction<I, O> {
		*//**
		 * @param element
		 * @param result
		 *            - result from previous application of function.
		 * @return
		 *//*
		public O apply(I element, O result);
	}*/
	
	public <I, O> Collection<O> map(Function<I, O> function);
	
	//public <I, O> O find(Function<I, O> function, Object input);
}
