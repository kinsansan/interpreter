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
public class Const extends Node{
    Value v;
    LexicalAnalyzer lex;
    
    static Node isMatch(Environment env, LexicalUnit oper) {
        if (oper.getType() == LexicalType.INTVAL||oper.getType() == LexicalType.DOUBLEVAL||oper.getType() == LexicalType.LITERAL) {
            return new Const(env);
        }
        return null;
    }
    
    public Const (Value v){
        this.v=v;
    }

    private Const(Environment env) {
        this.env = env;
        lex = env.getInput();
    }
    
    @Override
    public String toString() {
        return v.toString();
    }

    @Override
    public Value getValue() {
        return v;
    }

}
