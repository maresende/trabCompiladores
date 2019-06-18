package Parse;
import java.io.FileInputStream;
import java.util.Scanner;
import java.io.InputStream;
import ErrorMsg.ErrorMsg;
import java_cup.runtime.Symbol;
import java.io.FileNotFoundException;

class Tester {
      public static void main(String[] args) {
	try {
      		System.out.println(args[0]);
                    InputStream inputstream;
                    inputstream = new FileInputStream(args[0]);
		    ErrorMsg error = new ErrorMsg("eita");
  		    Yylex lexer = new Yylex(inputstream, error);
	     	    //inputstream.close();
		    Symbol symbol;
		    while(true){ 
		        symbol  = (Yylex.nextToken());
			if(symbol.sym == 0) break;
			if(symbol.sym == 3) break;
			System.out.println(symbol.sym);
		    }
             	
	} catch(FileNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
         }
     }
}

