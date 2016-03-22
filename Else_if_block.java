/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newlang8;

/**
 *
 * @author Admin
 */
public class Else_if_block extends Node{
    LexicalAnalyzer lex;
    Node stmtlist;
    
    public static Node isMatch(Environment env, LexicalUnit first) {
        if (first.getType() == LexicalType.ELSE) {
            return null;
        }
        return new Else_if_block(env); //é©ï™ÇçÏÇ¡Çƒï‘Ç∑
    }

    private Else_if_block(Environment env) {
        this.env = env;
        lex = env.getInput();
    }

    @Override
    public String toString() {
        return stmtlist.toString();
    }

    @Override
    public Value getValue() {
        return super.getValue(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean Parse() {
        LexicalUnit lu = lex.get();//ELSE
        
        lu = lex.get();//NL
        if (lu.getType() != LexicalType.NL) {
            return false;
        }
        
        lu = lex.get();//stmtlist
        stmtlist = StmtList.isMatch(env, lu);
        lex.unget(lu);
        if (stmtlist == null) {
            return false;
        }
        if (!stmtlist.Parse()) {
            return false;
        }
        return true;
    }
    
    
}
