/*
 * Decompiled with CFR 0.139.
 * 
 * Could not load the following classes:
 *  Absyn.Exp
 *  Absyn.Print
 *  ErrorMsg.ErrorMsg
 *  Parse.Grm
 *  Parse.Lexer
 *  Parse.Yylex
 *  java_cup.runtime.Symbol
 */
package Parse;

import Absyn.Exp;
import Absyn.Print;
import Tree2.Tree;
import Tree2.Leaf;
import ErrorMsg.ErrorMsg;
import Parse.Grm;
import Parse.Lexer;
import Parse.Yylex;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java_cup.runtime.Symbol;
import Table.Table;
import Table.Register;

public class Parse {
    public ErrorMsg errorMsg;
    public Exp sintaxe_abstrata;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Parse(String string, boolean bl, boolean bl2) {
        Symbol symbol;
	Symbol aux;
        FileInputStream fileInputStream;
        this.errorMsg = new ErrorMsg(string);
        try {
            fileInputStream = new FileInputStream(string);
        }
        catch (FileNotFoundException fileNotFoundException) {
            throw new Error("Arquivo nao encontrado: " + string);
        }
        Grm grm = new Grm((Lexer)new Yylex((InputStream)fileInputStream, this.errorMsg), this.errorMsg);
        try {
            try {
                symbol = bl2 ? grm.debug_parse() : grm.parse();
            }
            catch (Throwable throwable) {
                System.out.println("Erro de parsing:");
                throwable.printStackTrace();
                System.out.println(throwable.toString());
                System.out.println("Abortando..");
                System.exit(1);
                Object var8_8 = null;
                try {
                    ((InputStream)fileInputStream).close();
                    return;
                }
                catch (IOException iOException) {}
                return;
            }
        }
    }
}
