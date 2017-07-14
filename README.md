# JavaDoc Coverage Maven Plugin [![Build Status](https://img.shields.io/travis/manoelcampos/javadoc-coverage/master.svg)](https://travis-ci.org/manoelcampos/javadoc-coverage) [![GPL licensed](https://img.shields.io/badge/license-GPL-blue.svg)](http://www.gnu.org/licenses/gpl-3.0)

A Maven plugin to generate JavaDoc coverage reports. It parses the java source files and checks the percentage of the Java code which is covered by JavaDoc documentation, including:
- modules, packages
- classes, inner classes, interfaces and enums
- class attributes
- methods, parameters, exceptions and return value.

The project provides a [Doclet](http://docs.oracle.com/javase/7/docs/technotes/guides/javadoc/doclet/overview.html) to be used with the JavaDoc Tool
and the maven-javadoc-plugin to automate the generation of JavaDoc documentation coverage at your project.

**This is a pre-release which currently only shows results on the terminal and doesn't compute documentation coverage percentage yet.**

# Building the Plugin

The plugin is a Java Maven project which can be built directly from any IDE or using the following maven command:

```bash
mvn clean install
```

That will build the tool and install it at your local maven repository.

# How to use the Plugin

The generate the regular JavaDoc HTML files and the coverage reports, you have to include two configurations for the `maven-javadoc-plugin` inside your project's `pom.xml` file, as exemplified below. 

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-javadoc-plugin</artifactId>
    <version>2.10.4</version>
    <executions>
        <!-- Exports JavaDocs to regular HTML files -->
        <execution>
            <id>javadoc-html</id>
            <phase>package</phase>
            <goals>
                <goal>javadoc</goal>
            </goals>
        </execution>

        <!-- Generates the JavaDoc coverage report -->
        <execution>
            <id>javadoc-coverage</id>
            <phase>package</phase>
            <goals>
                <goal>javadoc</goal>
            </goals>
            <configuration>
                <doclet>com.manoelcampos.javadoc.coverage.CoverageDoclet</doclet>
                <docletArtifact>
                    <groupId>com.manoelcampos</groupId>
                    <artifactId>javadoc-coverage</artifactId>
                    <version>1.0.0-SNAPSHOT</version>
                </docletArtifact>
            </configuration>
        </execution>
    </executions>
</plugin>
```

Now, to generate the regular JavaDocs in HTML and the documentation coverage report, you can execute the `package` goal in Maven, using your IDE or the command line inside your project root directory:

```bash
mvn clean package
```

There is a maven [sample project](sample-project) where you can test the plugin. Just execute the command above inside the project's directory to see the results.