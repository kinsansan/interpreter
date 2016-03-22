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
public class Loopblock extends Node{
    LexicalAnalyzer lex;
    LexicalUnit lu;
    Node cond;
    Node stmt_list;
    
    private Loopblock(Environment env, LexicalUnit first) {
        this.env = env;
        lex = env.getInput();
    }
    
    static Node isMatch(Environment env, LexicalUnit first) {
        if (first.getType()==LexicalType.DO||first.getType()==LexicalType.WHILE) {
            return new Loopblock(env, first);
        }
        return null;
    }

    @Override
    public boolean Parse() {
        lex.get();//DO
        
        lu = lex.get();//UNTIL
        if (lu.getType() != LexicalType.UNTIL) {
            return false;
        }
        
        lu = lex.get();//cond
        cond = Cond.isMatch(env, lu);
        lex.unget(lu);
        if (cond == null) {
            return false;
        }
        if (cond.Parse() == false) {
            return false;
        }
        
        lu = lex.get();//NL
        if (lu.getType() != LexicalType.NL) {
            return false;
        }
        
        lu = lex.get();
        stmt_list = StmtList.isMatch(env, lu);
        lex.unget(lu);
        if (stmt_list == null) {
            return false;
        }
        if (!stmt_list.Parse()) {
            return false;
        }
        
//        do {            
            lu = lex.get();//LOOP?
            if (lu.getType() == LexicalType.LOOP) {
                return true;
            }
            return false;
//                lex.unget(lu);
//                break;
//            }
//            lu = lex.get();
//            sstmt_list = StmtList.isMatch(env, lu);
//            lex.unget(lu);
//            if (sstmt_list == null) {
//                return false;
//            }
//            if (sstmt_list.Parse() == false) {
//                return false;
//            }
//            lu = lex.get();
//        } while (true);
//        lu = lex.get();
//        return lu.getType() == LexicalType.LOOP;
    }
    
    @Override
    public String toString() {
        return "LOOP ["+cond.toString()+"["+stmt_list.toString()+"][]]";
//        return "LOOP "+"["+cond.toString()+"["+fstmt_list.toString()+sstmt_list.toString()+"][]]";
    }

    @Override
    public Value getValue() {
        while (!cond.getValue().getBValue()) {
            stmt_list.getValue();
        }
        return null;
    }    
    
}
