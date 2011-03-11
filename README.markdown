Sprinkles is a set of static methods to allow for functional composition to be sprinkled into Java programs.  
It includes 4 basic functional operations: map, fold, find, and depth-first map.  There are some very simple examples
in the test package but more interesting examples will be provided shortly.

# API #

## Function Interface ##

	public interface Function<I, O> {
		public O apply(I element);
	}
	
## Functional Operations ##

	public static <I, O> Collection<O> map(Function<I, O> function, Object input);
	
	public static Collection map(Collection<Function> functions, Object input);
	
	public static <I, O> O fold(FoldFunction<I, O> function, Object input);
	
	public static <I, O> O find(Function<I, O> function, Object input);
