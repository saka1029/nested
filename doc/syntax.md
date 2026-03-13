# Syntax

```
program    = 'program' [ vars ] statements 'end'
vars       = 'var' var { ',' var } ';'
var        = id [ '=' expression ]
statements = { statement }
statement  = id '=' expression ';'
           | 'if' expression 'then' statements [ 'else' statements ] 'end'
           | 'while' expression 'do' statements 'end'
           | 'display' expression ';'
```