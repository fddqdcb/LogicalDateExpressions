grammar LogicalDateExpressions;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Parser
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
stmt
        :   expr EOF
        ;

expr
        :   leftExpr=expr AND rightExpr=expr            # andExpr
        |   leftExpr=expr OR rightExpr=expr             # orExpr
        |   LPAREN expr RPAREN                          # parenExpr
        |   NOT expr                                    # notExpr
        |   (GT | GE | LT | LE) regularDate             # compareDateExpr
        |   fromDate=regularDate TO toDate=regularDate  # fromToDateExpr
        |   regularDate                                 # singleDateExpr
        ;

regularDate
        :   year
        |   month DOT year
        |   day DOT month DOT year
        ;

day
        :   N_01_12
        |   N_13_31
        ;

month
        :   N_01_12
        ;

year
        :   N_01_12
        |   N_13_31
        |   N_32_99
        |   N_1000_2999
        ;



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Lexer
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
N_01_12
        :   '0'? DIGIT    // 01-09
        |   '1' [0-2]     // 10-12
        ;

N_13_31
        :   '1' [3-9]     // 13-19
        |   '2' DIGIT     // 20-29
        |   '3' [01]      // 30-31
        ;

N_32_99
        :   '3' [2-9]     // 32-39
        |   [4-9] DIGIT   // 40-99
        ;

N_1000_2999
        :   [12] DIGIT DIGIT DIGIT // 1000-2999
        ;

AND     :   [Aa][Nn][Dd] 
        |   '&&'
        |   '&'
        ;

OR      :   [Oo][Rr] 
        |   '||'
        |   '|'
        ;

TO      :   '-'
        |   [Tt][Oo]
        ;

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
