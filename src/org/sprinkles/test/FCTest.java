package org.sprinkles.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.sprinkles.Fn;
import org.sprinkles.Fn.Function;
import org.sprinkles.collection.FunctionalCollection;
import org.sprinkles.collection.FunctionalCollectionDecrator;

public class FCTest {

	public void testBasicUsage() {
		FunctionalCollection<String> l = new FunctionalCollectionDecrator(new ArrayList<String>());
		
		l.add("one");
		l.add("two");
		l.add("three");
		
		Function<String, String> rev = new Function<String, String>() {

			@Override
			public String apply(String element) {
				StringBuilder sb = new StringBuilder(element);
				return sb.reverse().toString();
			}
		};
		
		Collection res = l.map(rev);
		
		for (Object o: res)
			System.out.println(o.toString());
	}
	
	public void testOldWay() {
		List<String> l = new ArrayList<String>();
		
		l.add("one");
		l.add("two");
		l.add("three");
		
		Function<String, String> rev = new Function<String, String>() {

			@Override
			public String apply(String element) {
				StringBuilder sb = new StringBuilder(element);
				return sb.reverse().toString();
			}
		};
		
		Collection res = Fn.map(rev, l);
		
		for (Object o: res)
			System.out.println(o.toString());
	}
}
