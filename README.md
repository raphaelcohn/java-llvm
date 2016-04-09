[](This file is part of java-llvm. It is subject to the licence terms in the COPYRIGHT file found in the top-level directory of this distribution and at https://raw.githubusercontent.com/raphaelcohn/java-llvm/master/COPYRIGHT. No part of java-llvm, including this file, may be copied, modified, propagated, or distributed except according to the terms contained in the COPYRIGHT file.)
[](Copyright © 2016 The developers of java-llvm. See the COPYRIGHT file in the top-level directory of this distribution and at https://raw.githubusercontent.com/raphaelcohn/java-llvm/master/COPYRIGHT.)

# java-llvm

[java-llvm] provides an in-development framework for converting to LLVM bytecode.


## Licensing

The license for this project is MIT.


## Valid Files

On Mac OS X, using all the jar files from JDK 1.8.0 update 74 (`rm -rf ./out/jar; mkdir -p ./out/jar; find /Library/Java/JavaVirtualMachines/jdk1.8.0_74.jdk/Contents/Home/ -type f -name '*.jar' | xargs gcp -t ./out/jar`), all 73,669 files pass (although about 10 class files make use of String constants that have invalid UTF-16 surrogate pairs in them).


### Ideas

* Type Annotations for new X() operations
	* Use stack space to allocate
		* Not a pointer to a struct, just as a struct
	* Use stack space to allocate, freed on end of method
	* Use stack space to allocate, freed on end of block
		* Could work with AutoCloseable
	* Never garbage collected (ie instantiated for all time)
	* Thread-wide objects can choose a number of heaps to allocate in
		* Per-Thread
		* Per-Core
		* Per-Group-of-Cores (NUMA)
		* Global
	* Pre-defined object pools
	* Per socket connection memory pool (a sub-memory pool of a per-core / per-group-of-cores)

* Type Annotations on cast operations (@Unchecked Type)
	* Type casts are not checked (ie the set of pointers for all implemented types is not consulted)

* Type Annotations on array index operations
	* OutOfBounds checks are not made
	* Not sure this is possible (could be done as an annotation on the array on declaration)
	* Alternatively, create a subclass

* Specialized array and vector subclasses (converted into a primitive type)

* Specialized floating point maths classes (converted into a primitive type)

* A disposable pattern using a collection field of an object
	* The collection contains dependent children
	* Makes it trivial to clean up
	* Use AutoCloseable; calling close() calls close() on all children
	* close() is called also by any GC approach

* A wrapper pattern for things like file descriptors
	* Translates a class into a reference / int with static methods

* Special syntax to not inherit from Object

* Special syntax to be a flat struct

* The set of classes a class implements is composed of a set of pointers to those classes; look up then becomes checking for presence (contains()) in an unsigned 64-bit integer set

* Other
	* Project Lombok
	* IntelliJ plugin for operator overloading

[java-llvm]: https://github.com/raphaelcohn/java-llvm "java-llvm GitHub page"
