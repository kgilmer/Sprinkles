package org.sprinkles.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.sprinkles.Fn;

import junit.framework.TestCase;

public class ConcurrencyTests extends TestCase {

	
	public void testCompletion() {
		List<String> input = new ArrayList<String>();
		int inputSize = 20;
		
		for (int i = 0; i < inputSize; ++i)
			input.add(new String("Task " + i));
		
		Collection<Object> col = Fn.map(new PrintInputFunction(), 
				Fn.map(new RandomSleepFunction(), input));
		
		assertTrue(col.size() == inputSize);
	}
	
	private class PrintInputFunction implements Fn.Function<Object, Object> {

		@Override
		public Object apply(Object element) {
			System.out.println(element.toString());
			return element;
		}
		
	}
	
	
	private class RandomSleepFunction implements Fn.Function<Object, Object> {

		@Override
		public Object apply(Object element) {
			Random r = new Random();
			
			try {
				Thread.sleep((r.nextInt(5) + 1) * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
			return element;
		}
		
	}
}
