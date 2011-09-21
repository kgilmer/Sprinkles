package org.sprinkles.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.sprinkles.Applier;
import org.sprinkles.Applier.Fn;
import org.sprinkles.Mapper;

public class Examples {
	
	public static final String HEADER_DELEMITER = "\r\n";
	public static final String HEADER_TERMINATOR = HEADER_DELEMITER + HEADER_DELEMITER;
	
	public static void main(String[] args) throws IOException {
		
		
		
		String request = "http rquest";
		
		Examples e = new Examples();
	
		e.echoServer(5555);
		
		//e.parseHttpMessage(request);
	}
	
	private void echoServer(int port) throws IOException {
		
		/*Mapper.map(new FunctionIteratorFunction(new Applier.Fn<Integer, Socket>() {

			ServerSocket ss = null;
			
			@Override
			public Socket apply(Integer input) {
				
				try {
					if (ss == null)
						ss = new ServerSocket(input.intValue());
					
					return ss.accept();
				} catch (IOException e) {
					e.printStackTrace();
				}			
				
				return null;
			}
		}), new Integer(5555));*/
		
		FunctionIterator fi = new FunctionIterator(new Applier.Fn<Integer, Socket>() {

			ServerSocket ss = null;
			
			@Override
			public Socket apply(Integer input) {
				
				try {
					if (ss == null)
						ss = new ServerSocket(input.intValue());
					
					return ss.accept();
				} catch (IOException e) {
					e.printStackTrace();
				}			
				
				return null;
			}
		}, new Integer(5555));
		
		Mapper.map(new Applier.Fn<Socket, Socket>() {

			@Override
			public Socket apply(Socket input) {
				BufferedReader isr = null;
				OutputStreamWriter osw = null;
				
				try {
					isr = new BufferedReader(new InputStreamReader(input.getInputStream()));
					osw = new OutputStreamWriter(input.getOutputStream());
					
					String line = null;
				
					while ((line = isr.readLine()) != null) {
						osw.write(line);
						osw.write('\n');
						osw.flush();
						
						if (line.equals("quit"))
							return null;
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						isr.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						osw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				return null;
			}
		}, fi);		
	}
	
	private void httpServer(int port) {
		
		/*Mapper.map(
				new HandleHttpMessageFunction(), new ExecutorCollection(new AcceptConnectionFunction(port)));*/
	}
	
	private void parseHttpMessage(String message) {
		//Parse an HTTP request
		Iterator<String> sectionIterator = Mapper.map(
				new ParseHttpMessageFunction(HEADER_TERMINATOR), message).iterator();
				
		List<String> headers = Mapper.map(
				new ParseHttpMessageFunction(HEADER_DELEMITER), sectionIterator.next());
		
		List<String> body = Mapper.map(
				new ParseHttpMessageFunction(HEADER_DELEMITER), sectionIterator.next());		
	}
	/*
	private class AcceptConnectionFunction implements Applier.Fn<I, O> {

		private final int port;

		public AcceptConnectionFunction(int port) {
			this.port = port;			
		}

		@Override
		public O apply(I input) {
			// TODO Auto-generated method stub
			return null;
		}
		
		
		
	}*/
	/*
	private class HandleHttpMessageFunction implements Applier.Fn<I, O> {

		@Override
		public O apply(I input) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	*/
	private class ParseHttpMessageFunction implements Applier.Fn<String, List<String>> {

		private final String delimiter;

		public ParseHttpMessageFunction(String delimiter) {
			this.delimiter = delimiter;			
		}
		
		@Override
		public List<String> apply(String input) {
			
			return Arrays.asList(input.split(delimiter));
		}
		
	}
	
	private class FunctionIteratorFunction implements Applier.Fn<Object, Iterator<?>> {

		private final Fn<Object, ?> function;

		public FunctionIteratorFunction(Applier.Fn<Object, ?> function) {
			this.function = function;			
		}
		
		@Override
		public Iterator<?> apply(final Object input) {
			return new Iterator() {

				private Object lastVal = "adsf";

				@Override
				public boolean hasNext() {					
					return lastVal != null;
				}

				@Override
				public Object next() {	
					lastVal  = function.apply(input);
					return lastVal;
				}

				@Override
				public void remove() {
					
				}
			};
		}
		
	}
	
	private class FunctionIterator implements Iterable {

		private final Fn function;
		private final Object input;
		
		public FunctionIterator(Applier.Fn function, Object input) {
			this.function = function;
			this.input = input;			
		}
		@Override
		public Iterator iterator() {
			
			return new Iterator() {

				private Object lastVal = "adsf";

				@Override
				public boolean hasNext() {					
					return lastVal != null;
				}

				@Override
				public Object next() {	
					lastVal  = function.apply(input);
					return lastVal;
				}

				@Override
				public void remove() {
					
				}
			};
		}		
	}
}
