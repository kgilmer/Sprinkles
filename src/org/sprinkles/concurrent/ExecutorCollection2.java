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
package org.sprinkles.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.sprinkles.Fn.Function;

/**
 * A collection implementation designed for concurrent execution of the results of a map operation.
 * Passing this adapter into map() will result in each non-null response from the initial function to be 
 * spawned in a Function as part of a ExecutorService.
 * 
 * The first client to call iterator() on the collection will prevent further tasks from being executed.  
 * The iterator can then be used to return the results of all map operations performed.
 * 
 * The class uses a counter to compare tasks that have been added to tasks that have been completed, when
 * iterator() has been called and the counter is 0 the executor is considered finished.
 * 
 * @author kgilmer
 *
 */
public class ExecutorCollection2 implements Collection {

	private ExecutorService executor;
	private final Function handler;
	private ArrayList<Object> completedTasks;
	private ArrayList<BlockingQueue> queues;
	private boolean finalized = false;
	private int pending = 0;

	
	/**
	 * Create adapter with default Executor.
	 * 
	 * @param handler
	 */
	public ExecutorCollection2(Function handler) {
		executor = Executors.newFixedThreadPool(1);
		this.handler = handler;
		completedTasks = new ArrayList<Object>();
		queues = new ArrayList<BlockingQueue>();
	}
	
	/**
	 * Create adapter with client-device Executor.
	 * 
	 * @param executor
	 * @param handler
	 */
	public ExecutorCollection2(ExecutorService executor, Function handler) {
		this.executor = executor;
		this.handler = handler;
		completedTasks = new ArrayList<Object>();
		queues = new ArrayList<BlockingQueue>();
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

	@Override
	public boolean add(Object e) {
		if (finalized)
			return false;
		
		executor.submit(new FunctionRunnableAdapter(handler, e));
		pending++;
		
		return true;
	}

	@Override
	public boolean addAll(Collection c) {

		for (Object o: c) 
			if (!add(o))
				return false;
		
		return true;
	}

	@Override
	public boolean contains(Object obj) {
		return completedTasks.contains(obj);
	}

	@Override
	public void clear() {
		throw new RuntimeException("Unimplemented");
	}

	@Override
	public boolean containsAll(Collection c) {
		for (Object o: c) 
			if (!contains(o))
				return false;
		
		return true;
	}

	@Override
	public boolean isEmpty() {
		return completedTasks.isEmpty();
	}

	@Override
	public Iterator iterator() {
		finalized  = true;
		BlockingQueue<Object> bq = new LinkedBlockingQueue<Object>();
		bq.addAll(completedTasks);
		
		queues.add(bq);
		
		return new QueueIterator(bq);			
	}

	@Override
	public boolean remove(Object obj) {
		throw new RuntimeException("Unimplemented");
	}

	@Override
	public boolean removeAll(Collection c) {
		throw new RuntimeException("Unimplemented");
	}

	@Override
	public boolean retainAll(Collection c) {
		throw new RuntimeException("Unimplemented");
	}

	@Override
	public int size() {
		throw new RuntimeException("Unimplemented");
	}

	@Override
	public Object[] toArray() {
		return completedTasks.toArray();
	}

	@Override
	public Object[] toArray(Object[] a) {
		return completedTasks.toArray(a);
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
			try {
				Object result = f.apply(input);	
				
				if (result != null)
					synchronized (result) {
						completedTasks.add(result);
						for (BlockingQueue<Object> bq: queues)
							bq.add(result);
					}
			} catch (RuntimeException e) {
				e.printStackTrace();
			} finally {
				pending--;
			}
		}
	}
	
	/**
	 * An interator that will block until all tasks are completed.
	 * 
	 * @author kgilmer
	 *
	 */
	private class QueueIterator implements Iterator {
		private final BlockingQueue<Object> bq;

		public QueueIterator(BlockingQueue<Object> bq) {
			this.bq = bq;						
		}
		
		@Override
		public boolean hasNext() {		
			if (!finalized)
				throw new RuntimeException("Invalid state, " + ExecutorCollection2.class.getSimpleName() + " should be finalized.");
			
			if (!bq.isEmpty())
				return true;
			
			return pending > 0;
		}

		@Override
		public Object next() {
			try {
				return bq.take();
			} catch (InterruptedException e) {
				return null;
			}
		}

		@Override
		public void remove() {
			throw new RuntimeException("Unimplemented"); 
		}
	}
}
