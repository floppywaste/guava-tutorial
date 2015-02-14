#Google Guava Introduction

A collection of unit tests demonstrating some of the API features of the google guava library (Version 18.0).

Some code examples make heavy use of functional programming idioms that are supported by guava (targeted at Java 5 to 7), though [the guava team discourages from using such techniques excessively](https://code.google.com/p/guava-libraries/wiki/FunctionalExplained). The examples should not be considered as best practice for production code. 

A few examples include Java 8 language features (labda expressions) to show what a migration to Java 8 could look like when the code base uses guava. 

## Topics
* [Preconditions](src/test/java/com/github/floppywaste/Guava01_Preconditions.java)
* [Strings](src/test/java/com/github/floppywaste/Guava02_Strings.java)
* [Functional Idioms](src/test/java/com/github/floppywaste/Guava03_Functional.java)
* [Collections](src/test/java/com/github/floppywaste/Guava04_Collections.java)
* [Composing different API features](src/test/java/com/github/floppywaste/Guava06_Compositions.java)


## Links to guava resources
 * [guava on github](https://github.com/google/guava)
 * [API docs (18.0)](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/index.html)
 * [guava explained](https://code.google.com/p/guava-libraries/wiki/GuavaExplained)