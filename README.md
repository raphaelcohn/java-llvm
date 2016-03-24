[](This file is part of java-llvm. It is subject to the licence terms in the COPYRIGHT file found in the top-level directory of this distribution and at https://raw.githubusercontent.com/raphaelcohn/java-llvm/master/COPYRIGHT. No part of java-llvm, including this file, may be copied, modified, propagated, or distributed except according to the terms contained in the COPYRIGHT file.)
[](Copyright © 2016 The developers of java-llvm. See the COPYRIGHT file in the top-level directory of this distribution and at https://raw.githubusercontent.com/raphaelcohn/java-llvm/master/COPYRIGHT.)

# java-llvm

[java-llvm] provides an in-development framework for converting to LLVM bytecode.


## Licensing

The license for this project is MIT.


## Known Issues

### Invalid Files

On Mac OS X, using all the jar files from JDK 1.8.0 update 74 (`rm -rf ./out/jar; mkdir -p ./out/jar; find /Library/Java/JavaVirtualMachines/jdk1.8.0_74.jdk/Contents/Home/ -type f -name '*.jar' | xargs gcp -t ./out/jar`), the following failures are present:-

* RuntimeInvisibleParameterAnnotations length must match method descriptor parameter count
    * `rt.jar`
        * `com/sun/xml/internal/ws/binding/WebServiceFeatureList$MergedFeatures.class`
* bootstrap method argument reference is not a bootstrap method argument
    * `rt.jar`
        * `java/time/chrono/AbstractChronology.class`
        * and others; this looks like a real bug

[java-llvm]: https://github.com/raphaelcohn/java-llvm "java-llvm GitHub page"
