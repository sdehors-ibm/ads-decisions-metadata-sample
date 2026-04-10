ADS metadata sample demo

This sample shows how management metadata can be accessed wihtin the rules to enrich the decision output. 

The scenario is the following: with the result of each invocation the user wants to know the auhtor and date of the rule that fired. This is modeled in the output of the decision.

To realize this, each rule that needs to be tracked must call a specific method from an external library that will retrieve the author and date of the rule. The returned value can be used to enrich the decision result.

Using this technique the rule itself  on its business logic, the management information will be retrieved dynamically at runtime.

Technical implementation:
An external library provides a method getMetadata() that returns the metadata about the current rule calling it. To access the current rule context this metod is implemented using b2x mapping. 
The metadata returned is precomputed at build time and packaged with the decision archive file. At runtime the getMetadata() method is called, looks up the value in the precomputed file and returs the information  used to enrich the decision result.

This sample contains:
- a java library that provides utility classes to access the precomputed metadata 
- an ADS external library that provides the getMetadata() method
- a decision service that uses the library to enrich the decision result, and capture management metadata at build time.