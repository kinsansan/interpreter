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
public class DoNode extends Node {

    Environment env;
    LexicalAnalyzer lex;
    Node cond;
    Node stmt_list;

    public static Node isMatch(Environment env, LexicalUnit first) {
        if (first.getType() != LexicalType.DO) {
            return null;
        }
        return new DoNode(env);
    }

    private DoNode(Environment env) {
        this.env = env;
        lex = env.getInput();
    }

    @Override
    public boolean Parse() {
        LexicalUnit lu = lex.get();
        lu = lex.get();

        if (lu.getType() == LexicalType.WHILE) {
            lu = lex.get();//cond
            cond = Cond.isMatch(env, lu);
            if (cond == null) {
                return false; //式でないものがきてるのでエラーのfalseを返す
            }
            if (cond.Parse() == false) {
                return false;//エラーを返す
            }
            
            lu = lex.get();//NL
            if (lu.getType() != LexicalType.NL) {
                return false;
            }

            lu = lex.get();//stmt_list
            stmt_list = StmtList.isMatch(env, lu);
            if (stmt_list == null) {
                return false;
            }
            if (stmt_list.Parse() == false) {
                return false;
            }

            lu = lex.get();//LOOP
            if (lu.getType() != LexicalType.LOOP) {
                return false;
            }

            lu = lex.get();//NL
            if (lu.getType() == LexicalType.NL) {
                return true;
            }
            return false;
        } else if (lu.getType() == LexicalType.UNTIL) {
            lu = lex.get();//cond
            cond = Cond.isMatch(env, lu);
            lex.unget(lu);
            if (cond == null) {
                return false; //式でないものがきてるのでエラーのfalseを返す
            }
            if (cond.Parse() == false) {
                return false;//エラーを返す
            }
            
            lu = lex.get();//NL
            if (lu.getType() != LexicalType.NL) {
                return false;
            }

            lu = lex.get();//stmt_list
            stmt_list = StmtList.isMatch(env, lu);
            if (stmt_list == null) {
                return false;
            }
            if (stmt_list.Parse() == false) {
                return false;
            }

            lu = lex.get();//LOOP
            if (lu.getType() != LexicalType.LOOP) {
                return false;
            }

            lu = lex.get();//NL
            if (lu.getType() == LexicalType.NL) {
                return true;
            }
            return false;
        } else {//NL
            lu = lex.get();//stmt_list
            stmt_list = StmtList.isMatch(env, lu);
            if (stmt_list == null) {
                return false;
            }
            if (stmt_list.Parse() == false) {
                return false;
            }

            lu = lex.get();//LOOP
            if (lu.getType() != LexicalType.LOOP) {
                return false;
            }

            lu = lex.get();//WHILE or UNTIL
            if (lu.getType() != LexicalType.UNTIL) {//WHILE
                lu = lex.get();//cond
                cond = Cond.isMatch(env, lu);
                if (cond == null) {
                    return false; //式でないものがきてるのでエラーのfalseを返す
                }
                if (cond.Parse() == false) {
                    return false;//エラーを返す
                }
                
                lu = lex.get();//NL
                if (lu.getType() == LexicalType.NL) {
                    return true;
                }
                return false;
            } else {//UNTIL
                lu = lex.get();//cond
                cond = Cond.isMatch(env, lu);
                if (cond == null) {
                    return false; //式でないものがきてるのでエラーのfalseを返す
                }
                if (cond.Parse() == false) {
                    return false;//エラーを返す
                }
                
                lu = lex.get();//NL
                if (lu.getType() != LexicalType.NL) {
                    return true;
                }
                return false;
            }
        }
    }

    @Override
    public String toString() {
        return "DO";
    }
    
}
