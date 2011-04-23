package org.sprinkles.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.sprinkles.Fn.Function;

public class FunctionalCollectionDecrator implements FunctionalCollection {
	public static FunctionalCollection newFunctionalCollection(List l) {
		return new FunctionalCollectionDecrator(l);
	}
	
	private static List list;

	/**
	 * @param index
	 * @param element
	 * @see java.util.List#add(int, java.lang.Object)
	 */
	public void add(int index, Object element) {
		list.add(index, element);
	}


	/**
	 * @param e
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean add(Object e) {
		return list.add(e);
	}


	/**
	 * @param c
	 * @return
	 * @see java.util.List#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection c) {
		return list.addAll(c);
	}


	/**
	 * @param index
	 * @param c
	 * @return
	 * @see java.util.List#addAll(int, java.util.Collection)
	 */
	public boolean addAll(int index, Collection c) {
		return list.addAll(index, c);
	}


	/**
	 * @param obj
	 * @return
	 * @see java.util.List#contains(java.lang.Object)
	 */
	public boolean contains(Object obj) {
		return list.contains(obj);
	}


	/**
	 * @return
	 * @see java.util.List#iterator()
	 */
	public Iterator iterator() {
		return list.iterator();
	}


	/**
	 * @param obj
	 * @return
	 * @see java.util.List#remove(java.lang.Object)
	 */
	public boolean remove(Object obj) {
		return list.remove(obj);
	}


	/**
	 * 
	 * @see java.util.List#clear()
	 */
	public void clear() {
		list.clear();
	}


	/**
	 * @param c
	 * @return
	 * @see java.util.List#containsAll(java.util.Collection)
	 */
	public boolean containsAll(Collection c) {
		return list.containsAll(c);
	}


	/**
	 * @return
	 * @see java.util.List#isEmpty()
	 */
	public boolean isEmpty() {
		return list.isEmpty();
	}


	/**
	 * @param obj
	 * @return
	 * @see java.util.List#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return list.equals(obj);
	}


	/**
	 * @return
	 * @see java.util.List#hashCode()
	 */
	public int hashCode() {
		return list.hashCode();
	}


	/**
	 * @param i
	 * @return
	 * @see java.util.List#get(int)
	 */
	public Object get(int i) {
		return list.get(i);
	}


	/**
	 * @param i
	 * @return
	 * @see java.util.List#remove(int)
	 */
	public Object remove(int i) {
		return list.remove(i);
	}


	/**
	 * @param obj
	 * @return
	 * @see java.util.List#indexOf(java.lang.Object)
	 */
	public int indexOf(Object obj) {
		return list.indexOf(obj);
	}


	/**
	 * @param obj
	 * @return
	 * @see java.util.List#lastIndexOf(java.lang.Object)
	 */
	public int lastIndexOf(Object obj) {
		return list.lastIndexOf(obj);
	}


	/**
	 * @return
	 * @see java.util.List#listIterator()
	 */
	public ListIterator listIterator() {
		return list.listIterator();
	}


	/**
	 * @param i
	 * @return
	 * @see java.util.List#listIterator(int)
	 */
	public ListIterator listIterator(int i) {
		return list.listIterator(i);
	}


	/**
	 * @param c
	 * @return
	 * @see java.util.List#removeAll(java.util.Collection)
	 */
	public boolean removeAll(Collection c) {
		return list.removeAll(c);
	}


	/**
	 * @param c
	 * @return
	 * @see java.util.List#retainAll(java.util.Collection)
	 */
	public boolean retainAll(Collection c) {
		return list.retainAll(c);
	}


	/**
	 * @param index
	 * @param element
	 * @return
	 * @see java.util.List#set(int, java.lang.Object)
	 */
	public Object set(int index, Object element) {
		return list.set(index, element);
	}


	/**
	 * @return
	 * @see java.util.List#size()
	 */
	public int size() {
		return list.size();
	}


	/**
	 * @return
	 * @see java.util.List#toArray()
	 */
	public Object[] toArray() {
		return list.toArray();
	}


	/**
	 * @param i
	 * @param j
	 * @return
	 * @see java.util.List#subList(int, int)
	 */
	public List subList(int i, int j) {
		return list.subList(i, j);
	}


	/**
	 * @param a
	 * @return
	 * @see java.util.List#toArray(T[])
	 */
	public Object[] toArray(Object[] a) {
		return list.toArray(a);
	}


	public FunctionalCollectionDecrator(List l) {
		list = l;
	}
	

	@Override
	public Collection map(org.sprinkles.Fn.Function function) {
		Collection out = new ArrayList();
		Iterable in;

		applyMap(function, this, out, false, true, true);

		return Collections.unmodifiableCollection(out);
	}
	
	/**
	 * The map function.
	 * 
	 * @param function
	 *            Function to be applied
	 * @param input
	 *            Collection to apply the function to.
	 * @param collection
	 *            Stores the result of function application.
	 * @param stopFirstMatch
	 *            Return (stop recursing) after first time function returns
	 *            non-null value.
	 * @param adaptMap
	 *            Inspect input types, if is a Map, iterate over values of map.
	 * @param recurse
	 *            Call apply on any elements of collection that are iterable.
	 */
	private static <I, O> void applyMap(Function<I, O> function, Iterable input, Collection<O> collection, boolean stopFirstMatch, boolean adaptMap, boolean recurse) {
		for (Object child : input) {
			boolean isIterable = child instanceof Collection;

			/*if (adaptMap)
				throw new RuntimeException("Adapt Map is broken!");*/
			if (!isIterable && adaptMap) {
				if (child instanceof Map) {
					child = (I) ((Map) child).values();
					isIterable = true;
				}
			}

			if (isIterable && recurse) {
				
				applyMap(function, (Iterable) child, collection, stopFirstMatch, adaptMap, recurse);
			} else {
				O result = function.apply((I) child);

				if (result != null) {
					collection.add(result);

					if (stopFirstMatch) {
						return;
					}
				}
			}
		}
	}
}
