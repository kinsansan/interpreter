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
class ForNode extends Node {
    LexicalAnalyzer lex;
    Node expr;
    Node stmtlist;
    
    public ForNode(Environment env) {
        this.env = env;
        lex = env.getInput();
    }
    
    public static Node isMatch(Environment env,LexicalUnit first){
        if (first.getType() == LexicalType.FOR) {
            return new ForNode(env);
        }
        return null;
    }

    @Override
    public String toString() {
        return "for"+stmtlist.toString();
    }

    @Override
    public Value getValue() {
        return super.getValue(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean Parse() {
        LexicalUnit lu = lex.get();//FOR
        
        lu = lex.get();//NAME
        if (lu.getType() != LexicalType.NAME) {
            return false;
        }
        
        lu = lex.get();//EQ
        if (lu.getType() != LexicalType.EQ) {
            return false;
        }
        
        lu = lex.get();//expr
        expr = Expr.isMatch(env, lu);
        if (expr == null) {
            return false;
        }
        if (!expr.Parse()) {
            return false;
        }
        
        lu = lex.get();//TO
        if (lu.getType() != LexicalType.TO) {
            return false;
        }
        
        lu = lex.get();//INTVAL
        if (lu.getType() != LexicalType.INTVAL) {
            return false;
        }
        
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
        
        lu = lex.get();//NEXT
        if (lu.getType() != LexicalType.NEXT) {
            return false;
        }
        
        lu = lex.get();//NAME
        return lu.getType() == LexicalType.NAME;
    }
}
