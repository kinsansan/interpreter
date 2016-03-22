/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newlang8;

import java.util.EnumSet;

/**
 *
 * @author Admin
 */
public class Cond extends Node {

    LexicalAnalyzer lex;
    LexicalUnit operator;
    Node left;
    Node expr;
    
    static EnumSet<LexicalType> firstset = null;//重複を認めない集合　staticにすると値を保持できるので初期化は一度で済む
    static EnumSet<LexicalType> condset = null;

    static Node isMatch(Environment env, LexicalUnit first) {//isMatchの中で場合分け
        if (firstset == null) {
            firstset = EnumSet.of(LexicalType.SUB);//firstsetの初期化
            firstset.add(LexicalType.LP);//列挙型への追加
            firstset.add(LexicalType.NAME);
            firstset.add(LexicalType.INTVAL);
            firstset.add(LexicalType.DOUBLEVAL);
            firstset.add(LexicalType.LITERAL);
        }
        //first集合に属するか
        if (firstset.contains(first.getType())) {
            return new Cond(env);
        }
        return null;
    }

    private Cond(Environment env) {
        this.env = env;
        lex = env.getInput();
    }

    @Override
    public boolean Parse() {
        if (condset == null) {
            condset = EnumSet.of(LexicalType.EQ);//condsetの初期化
            condset.add(LexicalType.GT);//列挙型への追加
            condset.add(LexicalType.LT);
            condset.add(LexicalType.GE);
            condset.add(LexicalType.LE);
            condset.add(LexicalType.NE);
        }

        LexicalUnit lu = lex.get();//最初の字句読み込み
        left = Expr.isMatch(env, lu);
        lex.unget(lu);

        if (left==null) {
            return false;
        }
        
        if (!left.Parse()) {
            return false;
        }
        operator = lex.get();
        if (!condset.contains(operator.getType())) {
            return false;
        }

        lu = lex.get();
        expr = Expr.isMatch(env, lu);//Exprか確認
        lex.unget(lu);
        if (expr == null) {
            return false; //式でないものがきてるのでエラーのfalseを返す
        }
        if (expr.Parse() == true) {
            return true;//あっているのでtrue
        }
        return false;
    }

    @Override
    public String toString() {
        String symbol="";
        switch (operator.toString()) {
            case "LT":
                symbol ="<";
                break;
            default:
                symbol =operator.toString();
                break;
        }
        return symbol+"["+expr.toString()+" : "+left.toString()+"]";
    }

    @Override
    public Value getValue() {
        switch (operator.getType()) {
            case EQ:
                return new ValueImpl(left.getValue().getIValue() == expr.getValue().getIValue());//int型のみ
            case GT:
                return new ValueImpl(left.getValue().getIValue() > expr.getValue().getIValue());//int型のみ
            case LT:
                return new ValueImpl(left.getValue().getIValue() < expr.getValue().getIValue());//int型のみ
            case GE:
                return new ValueImpl(left.getValue().getIValue() >= expr.getValue().getIValue());//int型のみ
            case LE:
                return new ValueImpl(left.getValue().getIValue() <= expr.getValue().getIValue());//int型のみ
            case NE:
                return new ValueImpl(left.getValue().getIValue() != expr.getValue().getIValue());//int型のみ
            default:
                return null;
        }
    }
    
    
}
//condクラスのgetvalueはtrueかfalseかを判定して返す
