/*
 * Decompiled with CFR 0.139.
 * 
 * Could not load the following classes:
 *  java_cup.runtime.Symbol
 */
package Parse;

import java.io.IOException;
import java_cup.runtime.Symbol;

interface Lexer {
    public Symbol yylex() throws IOException;
}
