/*
 * Decompiled with CFR 0.139.
 * 
 * Could not load the following classes:
 *  Absyn.Exp
 *  ErrorMsg.ErrorMsg
 *  Frame.Frame
 *  JavaVM.JavaFrame
 *  Main.ArgInvalidoException
 *  Parse.Parse
 *  Semant.ExpTy
 *  Semant.Semant
 *  Symbol.InstalaSimbolos
 *  Translate.Exp
 *  Translate.Frag
 *  Translate.Level
 *  Translate.Print
 *  Translate.Translate
 *  Types.Type
 *  Types.VOID
 */
package Main;

import Absyn.Exp;
import ErrorMsg.ErrorMsg;
import Frame.Frame;
import JavaVM.JavaFrame;
import Main.ArgInvalidoException;
import Parse.Parse;
import Tree2.Tree;
import Tree2.Leaf;
import Semant.ExpTy;
import Semant.Semant;
import Symbol.InstalaSimbolos;
import Translate.Frag;
import Translate.Level;
import Translate.Print;
import Translate.Translate;
import Types.Type;
import Types.VOID;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;

class Main {
    private static boolean absyn;
    private static boolean listinput;
    private static boolean listparse;
    private static boolean intermcode;
    private static boolean debug;

    public static void main(String[] arrstring) throws IOException {
        System.out.println();
        if (arrstring.length == 0) {
            System.out.println("Nome do arquivo n\u00e3o fornecido!");
            Main.ImprimeTelaDefault();
            System.exit(1);
        }
        for (int i = 2; i <= arrstring.length; ++i) {
            try {
                Main.VerificaArg(arrstring[i - 1]);
                continue;
            }
            catch (ArgInvalidoException argInvalidoException) {
                System.out.println("Argumento \"" + arrstring[i - 1] + "\" inv\u00e1lido!");
                Main.ImprimeTelaDefault();
                System.exit(1);
            }
        }
        if (listinput) {
            System.out.println("===============================================================================");
            Main.ImprimeArquivoEntrada(arrstring[0]);
            System.out.println("===============================================================================");
        }
        Parse parse = new Parse(arrstring[0], absyn, listparse);
        System.out.println("An\u00e1lise Sint\u00e1tica: OK!");
	
        //Parse parse2 = new Parse(arrstring[0], absyn, listparse);

       
	/*       new Symbol.InstalaSimbolos(parse.sintaxe_abstrata);
        Translate translate = new Translate();
        Level level = new Level((Frame)new JavaFrame());
        Semant semant = new Semant(parse.errorMsg, translate, level, intermcode);
        translate.procEntryExit(level, semant.transExp((Exp)parse.sintaxe_abstrata).exp, (Type)new VOID());
        if (parse.errorMsg.anyErrors) {
            System.out.println("An\u00e1lise Sem\u00e2ntica: ERRO!");
        } else {
            System.out.println("An\u00e1lise Sem\u00e2ntica: OK!");
        }
        if (intermcode) {
            Print.printer((Frag)translate.getResult());
        }*/
	System.out.println("done");
    }

    private static void VerificaArg(String string) throws ArgInvalidoException {
        if (string.equals("-absyn")) {
            absyn = true;
            return;
        }
        if (string.equals("-listinput")) {
            listinput = true;
            return;
        }
        if (string.equals("-listparse")) {
            listparse = true;
            return;
        }
        if (string.equals("-intermcode")) {
            intermcode = true;
            return;
        }
        throw new ArgInvalidoException();
    }

    private static void ImprimeTelaDefault() {
        System.out.println("===============================================================================");
        System.out.println("Uso correto: java Main.Main nome_arquivo.tig [-opcoes]");
        System.out.println();
        System.out.println("onde opcoes incluem:");
        System.out.println("-absyn       Imprime na saida padrao a arvore de sintaxe abstrata");
        System.out.println("-listinput   Imprime na saida padrao a listagem do arquivo de entrada");
        System.out.println("-listparse   Imprime na saida padrao os estados percorridos pelo parser");
        System.out.println("-intermcode  Imprime na saida padrao o codigo intermediario gerado");
        System.out.println("===============================================================================");
    }

    private static void ImprimeArquivoEntrada(String string) {
        int n = 0;
        boolean bl = false;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(string));
            while (!bl) {
                ++n;
                String string2 = bufferedReader.readLine();
                if (string2 == null) {
                    bl = true;
                    continue;
                }
                System.out.println(String.valueOf(n) + ":" + string2);
            }
            bufferedReader.close();
            return;
        }
        catch (FileNotFoundException fileNotFoundException) {
            System.out.println("Arquivo " + string + " inv\u00e1lido!");
            return;
        }
        catch (IOException iOException) {
            System.out.println("Arquivo " + string + " inv\u00e1lido!");
            return;
        }
    }

    Main() {
    }
}
