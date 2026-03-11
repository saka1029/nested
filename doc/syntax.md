# Syntax

```
program    = 'program' [ vars ] { routines } statements 'end'
vars       = 'var' id { ',' id } ';'
routines   = ( 'procedure' | 'function' ) id '(' [ id { ',' id } ] ')' [ vars ] statements 'end'
statements = statement { ';' statement } [ ';' ]
statement  = id '=' expression
           | id '(' [ expresion { ',' expresion } ] ')'
           | 'if' expression 'then' statements [ 'else' statements ] 'end'
           | 'while' expression 'do' statements 'end'
```