/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newlang8;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 *
 * @author Admin
 */
class Expr_list extends Node {

    List<Node> expr_list = new ArrayList<Node>();
    LexicalAnalyzer lex;
    Environment env;
    LexicalUnit lu;
    static EnumSet<LexicalType> firstset = null;
    Node exprlist;
    Node expr;

    private Expr_list(Environment env, LexicalUnit first) {
        this.env = env;
        lex = env.getInput();
    }

    public static Node isMatch(Environment env, LexicalUnit first) {
        if (firstset == null) {
            firstset = EnumSet.of(LexicalType.SUB);//firstsetÇÃèâä˙âª
            firstset.add(LexicalType.LP);//óÒãìå^Ç÷ÇÃí«â¡
            firstset.add(LexicalType.NAME);
            firstset.add(LexicalType.INTVAL);
            firstset.add(LexicalType.DOUBLEVAL);
            firstset.add(LexicalType.LITERAL);
        }
        if (firstset.contains(first.getType())) {
            return new Expr_list(env, first);
        }
        return null;
    }

    @Override
    public String toString() {
        String buffer = "";
        for (Node node_toString:expr_list) {
            buffer += node_toString.toString();
        }
        return buffer;
    }

    @Override
    public boolean Parse() {
        while (true) {
            lu = lex.get();//expr_list
            expr = Expr.isMatch(env, lu);
            expr_list.add(expr);
            lex.unget(lu);
            if (expr == null) {
                return false; //éÆÇ≈Ç»Ç¢Ç‡ÇÃÇ™Ç´ÇƒÇÈÇÃÇ≈ÉGÉâÅ[ÇÃfalseÇï‘Ç∑
            }
            if (expr.Parse() == false) {
                return false;
            }

            lu = lex.get();//COMMA
            if (lu.getType() != LexicalType.COMMA) {
                lex.unget(lu);
                return true;
            }
        }

    }
    
    public List<Node> getList(){
        return expr_list;
    }

}
