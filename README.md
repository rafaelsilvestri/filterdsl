# Overview
Filter DSL is a basic implementation of [Microsoft REST API Guidelines](https://github.com/microsoft/api-guidelines/blob/vNext/Guidelines.md#97-filtering)
filter operations.
A grammar was build using [ANTLR](https://www.antlr.org/) and a visitor class fulfills the tree that can be used to build other fragments of specific syntax like SQL or ElasticSearch. 

#### Filter operations
Operator             | Description           | Example
-------------------- | --------------------- | -----------------------------------------------------
Comparison Operators |                       |
eq                   | Equal                 | city eq 'Redmond'
ne                   | Not equal             | city ne 'London'
gt                   | Greater than          | price gt 20
ge                   | Greater than or equal | price ge 10
lt                   | Less than             | price lt 20
le                   | Less than or equal    | price le 100
Logical Operators    |                       |
and                  | Logical and           | price le 200 and price gt 3.5
or                   | Logical or            | price le 3.5 or price gt 200
Grouping Operators   |                       |
( )                  | Precedence grouping   | (priority eq 1 or city eq 'Redmond') and price gt 100

#### Examples
The following examples illustrate the use and semantics of each of the logical operators.

Example: all products with a name equal to 'Milk'

```http
GET https://api.contoso.com/v1.0/products?$filter=name eq 'Milk'
```

Example: all products with the name 'Milk' that also have a price less than 2.55:

```http
GET https://api.contoso.com/v1.0/products?$filter=name eq 'Milk' and price lt 2.55
```

Example: all products that either have the name 'Milk' or have a price less than 2.55:

```http
GET https://api.contoso.com/v1.0/products?$filter=name eq 'Milk' or price lt 2.55
```

Example: all products that have the name 'Milk' or 'Eggs' and have a price less than 2.55:

```http
GET https://api.contoso.com/v1.0/products?$filter=(name eq 'Milk' or name eq 'Eggs') and price lt 2.55
```

### Builders
We can use [FilterBuilder](src/main/java/com/github/rafaelsilvestri/filterdsl/FilterBuilder.java) interface to create our own
 implementation to build fragments of a particular syntax like SQL or ElasticSearch.

Take a look at [SqlBuilderTest](src/test/java/com/github/rafaelsilvestri/filterdsl/SqlSyntaxBuilderTest.java) for more examples of producing SQL fragments.

## TODO
* [ ] Improve String concatenation on the builders
* [ ] Support ElasticSearch syntax
* [ ] Improve the treatment for specific values like numbers 
* [ ] Add Querydsl support

## References
* https://www.antlr.org/
* https://www.thinkprogramming.co.uk/writing-predicate-parser-for-search-in-csharp-with-antlr4/
* https://www.thinkprogramming.co.uk/writing-predicate-parser-for-search-in-csharp-with-antlr-4-part-two/
* https://github.com/andy-williams/PredicateParser