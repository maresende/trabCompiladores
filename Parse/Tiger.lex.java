package Parse;
import ErrorMsg.ErrorMsg;
import java_cup.runtime.Symbol;


class Yylex implements Lexer {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

	private ErrorMsg error;
	Yylex (java.io.InputStream instream, ErrorMsg error) {
		this(instream);
		this.error = error;
	}
	private java_cup.runtime.Symbol tok(int kind, Object value) {
		Symbol scanned = new java_cup.runtime.Symbol(kind, yychar, yychar+yylength(),value);
		return scanned;
	}
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yychar;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	Yylex (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	Yylex (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private Yylex () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yychar = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yychar = yychar
			+ yy_buffer_index - yy_buffer_start;
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NO_ANCHOR,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NOT_ACCEPT,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NOT_ACCEPT,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NOT_ACCEPT,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NOT_ACCEPT,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NOT_ACCEPT,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"3:9,4:2,3:21,43,3,5,3:3,15,3,9,10,2,8,6,7,21,1,49:10,11,37,14,12,13,3:2,47:" +
"8,23,47:2,24,47,22,47:12,17,3,18,3,48,3,39,38,46,35,30,26,47,29,27,47,40,32" +
",47,31,25,42,47,36,33,28,45,44,34,47,41,47,19,16,20,3:2,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,92,
"0,1,2,1,3,1:5,4,1,5,6,1:7,7,1,8,1:5,9:10,10,9:6,1,11,12,13,14,15,16,17,18,1" +
"9,20,9,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43" +
",44,9,45,46,47,48,49,50,51,52,53")[0];

	private int yy_nxt[][] = unpackFromString(54,50,
"1,2,3,-1,47,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,82:2,49,85,51,5" +
"3,82,86,82,87,82,88,55,82,22,89,90,82:3,47,91,82:4,23,-1:52,48,-1:48,4:4,24" +
",4:44,-1:12,25,-1:49,26,-1:49,27,28,-1:58,82,56,82:13,-1,82:5,-1,82:4,57:2," +
"-1:49,23,-1:22,82:15,-1,82:5,-1,82:4,57:2,-1:2,48:48,-1:4,47,-1:38,47,-1:7," +
"48,50,48:47,-1:22,82:4,29,82:10,-1,82:5,-1,82:4,57:2,-1,39,-1:70,82:4,30,82" +
":4,31,82:5,-1,82:5,-1,82:4,57:2,-1:25,54,-1:46,82:3,32,82:3,60,82:7,-1,82:3" +
",61,82,-1,82:4,57:2,-1:26,46,-1:45,82:3,33,82:11,-1,82:5,-1,82:4,57:2,-1:22" +
",82:2,34,82:12,-1,82:5,-1,82:4,57:2,-1:22,82:14,35,-1,82:5,-1,82:4,57:2,-1:" +
"22,82:9,68,82:5,-1,82:5,-1,82:4,57:2,-1:22,82:8,69,82:6,-1,82:5,-1,82:4,57:" +
"2,-1:22,82:15,-1,82:4,70,-1,82:4,57:2,-1:22,82:13,36,82,-1,82:5,-1,82:4,57:" +
"2,-1:22,82:11,71,82:3,-1,82:5,-1,82:4,57:2,-1:22,82:6,37,82:8,-1,82:5,-1,82" +
":4,57:2,-1:22,82:5,72,82:9,-1,82:5,-1,82:4,57:2,-1:22,82:14,84,-1,82:5,-1,8" +
"2:4,57:2,-1:22,82:14,38,-1,82:5,-1,82:4,57:2,-1:22,82:15,-1,82:5,-1,82:2,74" +
",82,57:2,-1:22,82:9,40,82:5,-1,82:5,-1,82:4,57:2,-1:22,82:8,41,82:6,-1,82:5" +
",-1,82:4,57:2,-1:22,82:8,42,82:6,-1,82:5,-1,82:4,57:2,-1:22,82:10,75,82:4,-" +
"1,82:5,-1,82:4,57:2,-1:22,82:15,-1,82,76,82:3,-1,82:4,57:2,-1:22,82:6,78,82" +
":8,-1,82:5,-1,82:4,57:2,-1:22,82:8,43,82:6,-1,82:5,-1,82:4,57:2,-1:22,82:15" +
",-1,82:2,44,82:2,-1,82:4,57:2,-1:22,82:15,-1,82:3,79,82,-1,82:4,57:2,-1:22," +
"82:5,80,82:9,-1,82:5,-1,82:4,57:2,-1:22,82:15,-1,82:5,52,82:4,57:2,-1:22,82" +
":3,81,82:11,-1,82:5,-1,82:4,57:2,-1:22,82:9,45,82:5,-1,82:5,-1,82:4,57:2,-1" +
":22,82:8,73,82:6,-1,82:5,-1,82:4,57:2,-1:22,82:15,-1,82,77,82:3,-1,82:4,57:" +
"2,-1:22,82:3,58,82:11,-1,82:5,-1,82,59,82:2,57:2,-1:22,82:9,62,63,82:4,-1,8" +
"2:5,-1,82:4,57:2,-1:22,82:8,64,82:6,-1,82:5,-1,82:4,57:2,-1:22,82:7,65,82:7" +
",-1,82:5,-1,82:4,57:2,-1:22,82:14,83,-1,82:5,-1,82:4,57:2,-1:22,82:14,66,-1" +
",82:5,-1,82:4,57:2,-1:22,82:15,-1,82,67,82:3,-1,82:4,57:2");

	public java_cup.runtime.Symbol yylex ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {
				return null;
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 0:
						{ }
					case -2:
						break;
					case 1:
						
					case -3:
						break;
					case 2:
						{ return tok(sym.DIVIDE, this.yytext()); }
					case -4:
						break;
					case 3:
						{ return tok(sym.TIMES, this.yytext()); }
					case -5:
						break;
					case 5:
						{ return tok(sym.VIRG, this.yytext()); }
					case -6:
						break;
					case 6:
						{ return tok(sym.MINUS, this.yytext()); }
					case -7:
						break;
					case 7:
						{ return tok(sym.PLUS, this.yytext()); }
					case -8:
						break;
					case 8:
						{ return tok(sym.LPAR, this.yytext()); }
					case -9:
						break;
					case 9:
						{ return tok(sym.RPAR, this.yytext()); }
					case -10:
						break;
					case 10:
						{ return tok(sym.COLON, this.yytext()); }
					case -11:
						break;
					case 11:
						{ return tok(sym.EQUAL, this.yytext()); }
					case -12:
						break;
					case 12:
						{ return tok(sym.BIGGER, this.yytext()); }
					case -13:
						break;
					case 13:
						{ return tok(sym.SMALLER, this.yytext()); }
					case -14:
						break;
					case 14:
						{ return tok(sym.AND, this.yytext()); }
					case -15:
						break;
					case 15:
						{ return tok(sym.OR, this.yytext()); }
					case -16:
						break;
					case 16:
						{ return tok(sym.LBRA, this.yytext()); }
					case -17:
						break;
					case 17:
						{ return tok(sym.RBRA, this.yytext()); }
					case -18:
						break;
					case 18:
						{ return tok(sym.LKEY, this.yytext()); }
					case -19:
						break;
					case 19:
						{ return tok(sym.RKEY, this.yytext()); }
					case -20:
						break;
					case 20:
						{ return tok(sym.DOT, this.yytext()); }
					case -21:
						break;
					case 21:
						{ return tok(sym.ID, this.yytext()); }
					case -22:
						break;
					case 22:
						{ return tok(sym.SEMI, this.yytext()); }
					case -23:
						break;
					case 23:
						{ return tok(sym.NUM, this.yytext()); }
					case -24:
						break;
					case 24:
						{ return tok(sym.STRING, this.yytext()); }
					case -25:
						break;
					case 25:
						{ return tok(sym.ASSIGN, this.yytext()); }
					case -26:
						break;
					case 26:
						{ return tok(sym.BIGGER_EQUAL, this.yytext()); }
					case -27:
						break;
					case 27:
						{ return tok(sym.SMALLER_EQUAL, this.yytext()); }
					case -28:
						break;
					case 28:
						{ return tok(sym.DIFF, this.yytext()); }
					case -29:
						break;
					case 29:
						{ return tok(sym.OF, this.yytext()); }
					case -30:
						break;
					case 30:
						{ return tok(sym.IF, this.yytext()); }
					case -31:
						break;
					case 31:
						{ return tok(sym.IN, this.yytext()); }
					case -32:
						break;
					case 32:
						{ return tok(sym.TO, this.yytext()); }
					case -33:
						break;
					case 33:
						{ return tok(sym.DO, this.yytext()); }
					case -34:
						break;
					case 34:
						{ return tok(sym.NIL, this.yytext()); }
					case -35:
						break;
					case 35:
						{ return tok(sym.FOR, this.yytext()); }
					case -36:
						break;
					case 36:
						{ return tok(sym.END, this.yytext()); }
					case -37:
						break;
					case 37:
						{ return tok(sym.LET, this.yytext()); }
					case -38:
						break;
					case 38:
						{ return tok(sym.VAR, this.yytext()); }
					case -39:
						break;
					case 39:
						{ return tok(sym.COMMENT, this.yytext()); }
					case -40:
						break;
					case 40:
						{ return tok(sym.THEN, this.yytext()); }
					case -41:
						break;
					case 41:
						{ return tok(sym.TYPE, this.yytext()); }
					case -42:
						break;
					case 42:
						{ return tok(sym.ELSE, this.yytext()); }
					case -43:
						break;
					case 43:
						{ return tok(sym.WHILE, this.yytext()); }
					case -44:
						break;
					case 44:
						{ return tok(sym.BREAK, this.yytext()); }
					case -45:
						break;
					case 45:
						{ return tok(sym.FUNCTION, this.yytext()); }
					case -46:
						break;
					case 46:
						{ return tok(sym.ARRAY_OF, this.yytext()); }
					case -47:
						break;
					case 47:
						{ }
					case -48:
						break;
					case 49:
						{ return tok(sym.ID, this.yytext()); }
					case -49:
						break;
					case 51:
						{ return tok(sym.ID, this.yytext()); }
					case -50:
						break;
					case 53:
						{ return tok(sym.ID, this.yytext()); }
					case -51:
						break;
					case 55:
						{ return tok(sym.ID, this.yytext()); }
					case -52:
						break;
					case 56:
						{ return tok(sym.ID, this.yytext()); }
					case -53:
						break;
					case 57:
						{ return tok(sym.ID, this.yytext()); }
					case -54:
						break;
					case 58:
						{ return tok(sym.ID, this.yytext()); }
					case -55:
						break;
					case 59:
						{ return tok(sym.ID, this.yytext()); }
					case -56:
						break;
					case 60:
						{ return tok(sym.ID, this.yytext()); }
					case -57:
						break;
					case 61:
						{ return tok(sym.ID, this.yytext()); }
					case -58:
						break;
					case 62:
						{ return tok(sym.ID, this.yytext()); }
					case -59:
						break;
					case 63:
						{ return tok(sym.ID, this.yytext()); }
					case -60:
						break;
					case 64:
						{ return tok(sym.ID, this.yytext()); }
					case -61:
						break;
					case 65:
						{ return tok(sym.ID, this.yytext()); }
					case -62:
						break;
					case 66:
						{ return tok(sym.ID, this.yytext()); }
					case -63:
						break;
					case 67:
						{ return tok(sym.ID, this.yytext()); }
					case -64:
						break;
					case 68:
						{ return tok(sym.ID, this.yytext()); }
					case -65:
						break;
					case 69:
						{ return tok(sym.ID, this.yytext()); }
					case -66:
						break;
					case 70:
						{ return tok(sym.ID, this.yytext()); }
					case -67:
						break;
					case 71:
						{ return tok(sym.ID, this.yytext()); }
					case -68:
						break;
					case 72:
						{ return tok(sym.ID, this.yytext()); }
					case -69:
						break;
					case 73:
						{ return tok(sym.ID, this.yytext()); }
					case -70:
						break;
					case 74:
						{ return tok(sym.ID, this.yytext()); }
					case -71:
						break;
					case 75:
						{ return tok(sym.ID, this.yytext()); }
					case -72:
						break;
					case 76:
						{ return tok(sym.ID, this.yytext()); }
					case -73:
						break;
					case 77:
						{ return tok(sym.ID, this.yytext()); }
					case -74:
						break;
					case 78:
						{ return tok(sym.ID, this.yytext()); }
					case -75:
						break;
					case 79:
						{ return tok(sym.ID, this.yytext()); }
					case -76:
						break;
					case 80:
						{ return tok(sym.ID, this.yytext()); }
					case -77:
						break;
					case 81:
						{ return tok(sym.ID, this.yytext()); }
					case -78:
						break;
					case 82:
						{ return tok(sym.ID, this.yytext()); }
					case -79:
						break;
					case 83:
						{ return tok(sym.ID, this.yytext()); }
					case -80:
						break;
					case 84:
						{ return tok(sym.ID, this.yytext()); }
					case -81:
						break;
					case 85:
						{ return tok(sym.ID, this.yytext()); }
					case -82:
						break;
					case 86:
						{ return tok(sym.ID, this.yytext()); }
					case -83:
						break;
					case 87:
						{ return tok(sym.ID, this.yytext()); }
					case -84:
						break;
					case 88:
						{ return tok(sym.ID, this.yytext()); }
					case -85:
						break;
					case 89:
						{ return tok(sym.ID, this.yytext()); }
					case -86:
						break;
					case 90:
						{ return tok(sym.ID, this.yytext()); }
					case -87:
						break;
					case 91:
						{ return tok(sym.ID, this.yytext()); }
					case -88:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
