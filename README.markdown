Sprinkles is a Java class file that defines a generic Function interface, and a set of static methods to operate on it.  

By writing implementations (either regular or anonymous), one can write Java code in a functional style, by chaining
functional operations on sets of data.  Currently 4 basic functional operations are supported: map, fold, find, and depth-first map.
Additional classes provide functions for common operations against types such as File and String.

# Examples #

## Walk filesystem tree and print files ##
```java
Fn.map(new Fn.Function<File, File>() {
	
	@Override
	public File apply(File f) {
		System.out.println(f.toString());
		
		return f;
	}
}, Fn.map(ReturnFilesFunction.GET_FILES_FN, new File(".")));
```

## Join a List of String as a single String separated by commas ##
```java
List<String> l = new ArrayList<String>();

for (int i = 0 ; i < 100; ++i)
	l.add("i" + i);

System.out.println(
		Fn.fold(
			new StringFunctions.JoinFn(","), l));
```

# API #

## Function Interface ##
```java
public interface Function<I, O> {
	public O apply(I element);
}
```

### The interface for Fold operations ###

```java
public interface FoldFunction<I, O> {	
	public O apply(I element, O result);
}
```

## Static Operations (methods) ##

In the simplest case, just apply a Function to some kind of input.  The static method will determine if the input can be iterated over, 
and if not the function will be applied to the single input.

```java
public static <I, O> Collection<O> map(Function<I, O> function, Object input);
```

Here we apply a set of functions to an input.  The evaluation occurs depth-first.

```java
public static Collection map(Collection<Function> functions, Object input);
```

Perform a fold operation, meaning for each element in `input`, pass the value in and the result of the last call to the `FoldFunction`.  The 
result of the final call is returned.

```java
public static <I, O> O fold(FoldFunction<I, O> function, Object input);
```

Perform a find operation.  Apply `Function` to the `input` until a non-null result is returned.

```java
public static <I, O> O find(Function<I, O> function, Object input);
```

# Download #

[Binary builds](https://leafcutter.ci.cloudbees.com/job/Sprinkles/) are provided by Cloudbees.

# Documentation #

[Javadoc](https://leafcutter.ci.cloudbees.com/job/Sprinkles/javadoc/) are generated in the Cloudbees build.

# License #

Sprinkles uses the BSD-style license.
