# JavaDoc Coverage Maven Plugin
A Maven plugin to generate JavaDoc coverage reports. It parses the java source files generating an Abstract Syntax Tree using the [javaparser](http://javaparser.org) and checks the percentage of the Java code which is covered by JavaDoc documentation, including packages, class, interfaces, attributes, methods, parameters and method's return.

The plugin should:

- be based on a javadoc doclet plugin such as the [exportdoclet](https://github.com/manoelcampos/exportdoclet);
- it should implement a doclet to read the javadocs from the source files and the javaparser to check which elements (such as classes, interfaces, methods, etc) in the entire project source code aren't inside the elements listed by the doclet (meaning they don't have documentation);
- generate reports in different formats such as HTML, XML, JSON, etc. Each available format should be a different maven dependency.
