grammar Filter;

// Reference: https://github.com/microsoft/api-guidelines/blob/vNext/Guidelines.md#97-filtering

@header {
    package com.github.rafaelsilvestri.filterdsl;
}

// PARSER RULES
expr: attribute EOF;

attribute
    : attribute logicalOperator attribute
    | OpenParen attribute CloseParen
    | operand operator operand
    ;

logicalOperator
    : And
    | Or
    ;

operator
    : GreaterThan
    | LessThan
    | GreaterThanEqual
    | LessThanEqual
    | Equal
    | NotEqual
    | Contains
    ;

operand
    : Property
    | String
    | Number
    ;


// LEXER RULES

// Logical Operators
And: 'and';
Or: 'or';

// operators
// minimal set of operators
GreaterThan: 'gt';
GreaterThanEqual: 'ge';
LessThan: 'lt';
LessThanEqual: 'le';
Equal: 'eq';
NotEqual: 'ne';
// adicional operators
Contains: 'contains';

// grouping Operators
OpenParen: '(';
CloseParen: ')';

// operands
Number: [0-9]+;
String: '\'' ( '\\"' | ~["\r\n] )*? '\'';
Property: PROPERTY_NAME;

WhiteSpace: [ \t\f\r\n]+ -> channel(HIDDEN); // skip whitespaces
Discardable: . -> channel(HIDDEN); // keeping whitespace tokenised makes it easier for syntax highlighting

fragment PROPERTY_NAME: [a-z_]+;
