package org.sprinkles.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.sprinkles.Fn.Function;

/**
 * A collection implementation designed for concurrent execution of the results of a map operation.
 * Passing this adapter into map() will result in each non-null response from the initial function to be 
 * spawned in a Function as part of a ExecutorService.
 * 
 * @author kgilmer
 *
 */
public class ExecutorCollection implements Collection<Object>, ExecutorService {

	private ExecutorService executor;
	private final Function handler;
	private final Collection taskMap = new ArrayList();
	
	/**
	 * Create adapter with default Executor.
	 * 
	 * @param handler
	 */
	public ExecutorCollection(Function handler) {
		executor = Executors.newFixedThreadPool(1);
		this.handler = handler;
	}
	
	/**
	 * Create adapter with client-device Executor.
	 * 
	 * @param executor
	 * @param handler
	 */
	public ExecutorCollection(ExecutorService executor, Function handler) {
		this.executor = executor;
		this.handler = handler;
	}
	
	/**
	 * @param runnable
	 * @see java.util.concurrent.Executor#execute(java.lang.Runnable)
	 */
	public void execute(Runnable runnable) {
		executor.execute(runnable);
	}

	/**
	 * @param l
	 * @param timeunit
	 * @return
	 * @throws InterruptedException
	 * @see java.util.concurrent.ExecutorService#awaitTermination(long, java.util.concurrent.TimeUnit)
	 */
	public boolean awaitTermination(long l, TimeUnit timeunit) throws InterruptedException {
		return executor.awaitTermination(l, timeunit);
	}

	/**
	 * @param <T>
	 * @param tasks
	 * @param timeout
	 * @param unit
	 * @return
	 * @throws InterruptedException
	 * @see java.util.concurrent.ExecutorService#invokeAll(java.util.Collection, long, java.util.concurrent.TimeUnit)
	 */
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
		return executor.invokeAll(tasks, timeout, unit);
	}

	/**
	 * @param <T>
	 * @param tasks
	 * @return
	 * @throws InterruptedException
	 * @see java.util.concurrent.ExecutorService#invokeAll(java.util.Collection)
	 */
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
		return executor.invokeAll(tasks);
	}

	/**
	 * @param <T>
	 * @param tasks
	 * @param timeout
	 * @param unit
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 * @see java.util.concurrent.ExecutorService#invokeAny(java.util.Collection, long, java.util.concurrent.TimeUnit)
	 */
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return executor.invokeAny(tasks, timeout, unit);
	}

	/**
	 * @param <T>
	 * @param tasks
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @see java.util.concurrent.ExecutorService#invokeAny(java.util.Collection)
	 */
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
		return executor.invokeAny(tasks);
	}

	/**
	 * 
	 * @see java.util.concurrent.ExecutorService#shutdown()
	 */
	public void shutdown() {
		executor.shutdown();
	}

	/**
	 * @return
	 * @see java.util.concurrent.ExecutorService#shutdownNow()
	 */
	public List<Runnable> shutdownNow() {
		return executor.shutdownNow();
	}

	/**
	 * @return
	 * @see java.util.concurrent.ExecutorService#isShutdown()
	 */
	public boolean isShutdown() {
		return executor.isShutdown();
	}

	/**
	 * @return
	 * @see java.util.concurrent.ExecutorService#isTerminated()
	 */
	public boolean isTerminated() {
		return executor.isTerminated();
	}

	/**
	 * @param <T>
	 * @param task
	 * @return
	 * @see java.util.concurrent.ExecutorService#submit(java.util.concurrent.Callable)
	 */
	public <T> Future<T> submit(Callable<T> task) {
		return executor.submit(task);
	}

	/**
	 * @param <T>
	 * @param task
	 * @param result
	 * @return
	 * @see java.util.concurrent.ExecutorService#submit(java.lang.Runnable, java.lang.Object)
	 */
	public <T> Future<T> submit(Runnable task, T result) {
		return executor.submit(task, result);
	}

	/**
	 * @param runnable
	 * @return
	 * @see java.util.concurrent.ExecutorService#submit(java.lang.Runnable)
	 */
	public Future<?> submit(Runnable runnable) {
		return executor.submit(runnable);
	}
	
	@Override
	public boolean add(Object e) {
		executor.submit(new FunctionRunnableAdapter(handler, e));
		
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends Object> c) {
		for (Object o: c)
			add(o);
		
		return true;
	}

	@Override
	public void clear() {
		taskMap.clear();
		executor.shutdown();
	}

	@Override
	public boolean contains(Object o) {
		return taskMap.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new RuntimeException("Unimplemented");
	}

	@Override
	public boolean isEmpty() {		
		return taskMap.isEmpty();
	}

	@Override
	public Iterator iterator() {
		return taskMap.iterator();
	}

	@Override
	public boolean remove(Object o) {
		throw new RuntimeException("Unimplemented");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		for (Object o: c)
			remove(o);
		
		return true;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int size() {		
		return taskMap.size();
	}

	@Override
	public Object[] toArray() {	
		return taskMap.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {		
		return (T[]) taskMap.toArray(a);
	}

	
	/**
	 * Adapts a Function as a Runnable.
	 * @author kgilmer
	 *
	 */
	class FunctionRunnableAdapter implements Runnable {

		private final Function f;
		private final Object input;

		public FunctionRunnableAdapter(Function f, Object input) {
			this.f = f;
			this.input = input;
		}
		
		@Override
		public void run() {
			taskMap.add(f.apply(input));			
		}

	}
}
