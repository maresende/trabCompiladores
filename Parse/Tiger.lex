package Parse;
import ErrorMsg.ErrorMsg;
import java_cup.runtime.Symbol;

%%
%implements Lexer
%type java_cup.runtime.Symbol
%char


%{



	private ErrorMsg error;
	Yylex (java.io.InputStream instream, ErrorMsg error) {
		this(instream);
		this.error = error;
	}


	private java_cup.runtime.Symbol tok(int kind, Object value) {
		Symbol scanned = new java_cup.runtime.Symbol(kind, yychar, yychar+yylength(),value);
		return scanned;
	}
%}
DIGIT=		[0-9]
LETTER=		[a-zA-Z]
WHITESPACE2=	[ \t] 
WHITESPACE=	[ \t\n] 
QUOTATION=	[\"]
BACKSLASH=	[_]
START=	"/*"
END=	"*/"
SIMPLE=	[^*]
COMPLEX=	"*"/[^/]
NOQUOTE=	[^\"]
%%
{START}({SIMPLE}|{COMPLEX})*{END} { return tok(sym.COMMENT, this.yytext()); }
{WHITESPACE}* { }
{QUOTATION}{NOQUOTE}*{QUOTATION} { return tok(sym.STRING, this.yytext()); }
"," { return tok(sym.VIRG, this.yytext()); }
"-" { return tok(sym.MINUS, this.yytext()); }
"+" { return tok(sym.PLUS, this.yytext()); }
"(" { return tok(sym.LPAR, this.yytext()); }
")" { return tok(sym.RPAR, this.yytext()); }
"*" { return tok(sym.TIMES, this.yytext()); }
"/" { return tok(sym.DIVIDE, this.yytext()); }
":=" { return tok(sym.ASSIGN, this.yytext()); }
">" { return tok(sym.BIGGER, this.yytext()); }
"<" { return tok(sym.SMALLER, this.yytext()); }
">=" { return tok(sym.BIGGER_EQUAL, this.yytext()); }
"<=" { return tok(sym.SMALLER_EQUAL, this.yytext()); }
"=" { return tok(sym.EQUAL, this.yytext()); }
"<>" { return tok(sym.DIFF, this.yytext()); }
"&" { return tok(sym.AND, this.yytext()); }
"|" { return tok(sym.OR, this.yytext()); }
"[" { return tok(sym.LBRA, this.yytext()); }
"]" { return tok(sym.RBRA, this.yytext()); }
"{" { return tok(sym.LKEY, this.yytext()); }
"}" { return tok(sym.RKEY, this.yytext()); }
":" { return tok(sym.COLON, this.yytext()); }
"." { return tok(sym.DOT, this.yytext()); }
"NIL" { return tok(sym.NIL, this.yytext()); }
"of" { return tok(sym.OF, this.yytext()); }
"if" { return tok(sym.IF, this.yytext()); }
"then" { return tok(sym.THEN, this.yytext()); }
"else" { return tok(sym.ELSE, this.yytext()); }
"while" { return tok(sym.WHILE, this.yytext()); }
"do" { return tok(sym.DO, this.yytext()); }
"for" { return tok(sym.FOR, this.yytext()); }
"to" { return tok(sym.TO, this.yytext()); }
";" { return tok(sym.SEMI, this.yytext()); }
"break" { return tok(sym.BREAK, this.yytext()); }
"let" { return tok(sym.LET, this.yytext()); }
"in" { return tok(sym.IN, this.yytext()); }
"end" { return tok(sym.END, this.yytext()); }
"type" { return tok(sym.TYPE, this.yytext()); }
"array of" { return tok(sym.ARRAY_OF, this.yytext()); }
"var" { return tok(sym.VAR, this.yytext()); }
"function" { return tok(sym.FUNCTION, this.yytext()); }
({LETTER}|{BACKSLASH})({BACKSLASH}|{LETTER}|{DIGIT})* { return tok(sym.ID, this.yytext()); }
({DIGIT}*) { return tok(sym.NUM, this.yytext()); }

