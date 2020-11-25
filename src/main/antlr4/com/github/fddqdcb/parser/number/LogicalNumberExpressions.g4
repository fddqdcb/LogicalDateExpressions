grammar LogicalNumberExpressions;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Parser
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
stmt
        :   expr EOF
        ;

expr
        :   leftExpr=expr AND rightExpr=expr              # andExpr
        |   leftExpr=expr OR rightExpr=expr               # orExpr
        |   LPAREN expr RPAREN                            # parenExpr
        |   NOT expr                                      # notExpr
        |   (GT | GE | LT | LE) number                    # compareNumberExpr
        |   fromNumber=number (MINUS|TO) toNumber=number  # fromToNumberExpr
        |   number                                        # singleNumberExpr
        ;

number
        :   MINUS? NUMBER
        ;



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Lexer
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
NUMBER  :   DIGIT+ (DOT DIGIT*)?
        |   DOT DIGIT+
        ;

AND     :   [Aa][Nn][Dd] 
        |   '&&'
        |   '&'
        ;

OR      :   [Oo][Rr] 
        |   '||'
        |   '|'
        ;

MINUS   :   '-';
TO      :   [Tt][Oo];
DIGIT   :   [0-9];
GT      :   '>';
GE      :   '>=';
LT      :   '<';
LE      :   '<=';
DOT     :   '.';
LPAREN  :   '(';
RPAREN  :   ')';
NOT     :   '!';


WHITESPACES      
        :  [ \t\r\n\u000C]+ -> skip
        ;
