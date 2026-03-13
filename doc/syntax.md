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
           | 'proc' routine
           | 'func' routine
routine    = id args [ vars ] statements 'end'
args       = '(' [ id { ',' id } ] ')'
```