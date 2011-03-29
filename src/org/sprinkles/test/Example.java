package org.sprinkles.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.sprinkles.Fn;
import org.sprinkles.Fn.FoldFunction;
import org.sprinkles.Fn.Function;
import org.sprinkles.functions.ReturnFilesFunction;

public class Example {

	public void r() {

		Collection<Fn.Function> functions = new ArrayList<Fn.Function>();
		functions.add(ReturnFilesFunction.GET_FILES_FN);
		functions.add(new FileToSize());
		
		Object result = Fn.fold(new AddLongElements(), Fn.map(functions, new File("/tmp")));
		
		System.out.println("Total size: " + result);
	}
	
	private void r2() {
		List<Integer> c = Arrays.asList(new Integer[] { 32, 38, 19});
		
		Collection<String> c2 = Fn.map(new PrintFunction(), c);
		
		for (String s: c2) {
			System.out.println(s.length());
		}
	}
	
	private class PrintFunction implements Fn.Function<Integer, String> {

		@Override
		public String apply(Integer element) {			
			return element.toString();
		}
		
	}
	
	public static void main(String[] args) {
		Example e = new Example();
		e.r2();
		
	}
	
	private class FileToSize implements Function {

		public Object apply(Object element) {
			return ((File) element).length();
		}

	}

	private class AddLongElements implements FoldFunction {
		public Object apply(Object element, Object result) {
			Long ival = (Long) element;
			Long rval = (Long) result;
			
			if (rval == null)
				return ival;
				
			return ival + rval;
		}
	}
}
