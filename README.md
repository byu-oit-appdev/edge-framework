# Edge Framework
This is a compilation of various libraries for use in our Java projects. The goal with many of these libraries is to solve a specific problem or concern in an elegant/reusable way. Typically, [Spring](http://projects.spring.io/spring-framework/) and [Google Guava](https://code.google.com/p/guava-libraries/) are used as dependencies, but this varies based on the problem to solve.

## Modules

### byu-auth-filters

### dummy-rest

### dummy-web

### edge-pom

### edge-util

### framework-swing

### jdbc-da
This library contains some utility classes for use with Spring JDBC. Extract a String or Integer, translate boolean values, etc.

### jdbc-gen
Generates domain objects, data access interfaces, and stubs out data access implementations based on Spring-JDBC by analyzing an Oracle or MySQL schema. The ability to specify a schema and limit to specific tables or views is supported. Also generates the required Spring application context (xml).

### personMerge

### query-tools
DBMSs may (and most do) have a limit on the number of list elements passed as parameters. For example, Oracle has a limit of 1000 - where MY_COL in (val1, val2, val3, etc) - such that you can only pass in up to 1000 values for that in clause. Typically, we don't worry about that because most of our queries deal with individuals or we use a subselect for the in clause. When this is not the case, we must limit the size of the parameter list.

### soap-old-security

### swing-components

### ws-auth-filters

### ws-simple-auth-filter
The network directors can do the authentication and pass on the identity to the service. This library contains a Spring Security filter/auth handler that loads identity details based on the headers from the network directors. There is also a filter that will only accept connections from specified IP addresses/ranges. That way the headers are only trusted if the source is our network directors.
