Sprinkles is a Java class file that defines a generic Function interface, and a set of static methods to operate on it.  

By writing implementations (either regular or anonymous), one can write Java code in a functional style, by chaining
functional operations on sets of data.  Currently 4 basic functional operations are supported: map, fold, find, and depth-first map.
Additional classes provide functions for common operations against types such as File and String.

# Examples #

## Walk filesystem tree and print files ##
```java
Applier.map(Applier.map(FileFunctions.GET_FILES_FN, new File(".")), new Applier.Fn<File, File>() {
	
	@Override
	public File apply(File f) {
		System.out.println(f.toString());
		
		return f;
	}
});
```

## Join a List of String as a single String separated by commas ##
```java
List<String> l = new ArrayList<String>();

for (int i = 0 ; i < 100; ++i)
	l.add("i" + i);

System.out.println(
		Applier.fold(l, new StringFunctions.JoinFn(",")));
```

# API #

Refer to [Javadocs](https://leafcutter.ci.cloudbees.com/job/Sprinkles/javadoc/) for current API information.

# Download #

[Binary builds](https://leafcutter.ci.cloudbees.com/job/Sprinkles/) are provided by Cloudbees.

# Documentation #

[Javadoc](https://leafcutter.ci.cloudbees.com/job/Sprinkles/javadoc/) are generated in the Cloudbees build.

# License #

Sprinkles is public domain.
