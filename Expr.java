/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newlang8;
import java.util.EnumSet;
import java.util.Stack;

/**
 *
 * @author Admin
 */
//演算子は一つで二つの数字までしか処理しない
//最初からまとめていきまとまったらバイナリにする1*a+b-2の場合(((1*a)+b)-2)
//operator演算子とoperand変数をそれぞれスタックに入れる
//binaryクラス
//public みんなの　protected おれらの private おれの　つけない　パッケージ内グローバル
public class Expr extends Node {

    LexicalAnalyzer lex;
    static EnumSet<LexicalType> firstset = null;//重複を認めない集合　staticにすると値を保持できるので初期化は一度で済む
    static EnumSet<LexicalType> exprset = null;
    LexicalUnit oper;
    Stack<LexicalUnit> lustack = new Stack<LexicalUnit>();
    Stack<Node> first = new Stack<Node>();

    static Node isMatch(Environment env, LexicalUnit first) {
        if (firstset == null) {
            firstset = EnumSet.of(LexicalType.SUB);//firstsetの初期化
            firstset.add(LexicalType.LP);//列挙型への追加
            firstset.add(LexicalType.NAME);
            firstset.add(LexicalType.INTVAL);
            firstset.add(LexicalType.DOUBLEVAL);
            firstset.add(LexicalType.LITERAL);
        }

        if (exprset == null) {
            exprset = EnumSet.of(LexicalType.ADD);//exprsetの初期化
            exprset.add(LexicalType.SUB);
            exprset.add(LexicalType.MUL);
            exprset.add(LexicalType.DIV);
            exprset.add(LexicalType.LP);
            exprset.add(LexicalType.NAME);
            exprset.add(LexicalType.INTVAL);
            exprset.add(LexicalType.DOUBLEVAL);
            exprset.add(LexicalType.LITERAL);
            exprset.add(LexicalType.RP);
        }

        //first集合に属するか
        if (firstset.contains(first.getType())) {
            return new Expr(env, first);
        }
        return null;
    }

    private Expr(Environment env, LexicalUnit first) {
        this.env = env;
        lex = env.getInput();
    }

    @Override
    public String toString() {
        return first.peek().toString();
    }

    //左右の数字がくるスタックと演算子のスタックを作る
    //左辺にくるものを数字のスタックに　演算子がきて演算子のスタックに入れる　右辺にくるものをスタックに 
    @Override
    public boolean Parse() {
        if (!getOperand()) {
            return false;
        }
        
        oper =lex.get();
        if (oper==null||!exprset.contains(oper.getType())) {
            lex.unget(oper);
            return true;
        }
        
        lex.unget(oper);
        while (true) {//格納
            oper = lex.get();
            if (oper == null) {
                break;
            } else if (!exprset.contains(oper.getType()) || oper.getType() == LexicalType.RP) {
                lex.unget(oper);
                break;
            }
            LexicalUnit lu = oper;
            lustack.push(lu);//oper.getType()?
            if (!getOperand()) {
                return false;
            }
            
            if (lu.getType() ==LexicalType.MUL||lu.getType() ==LexicalType.DIV ) {
                reduce();
            }
        }
        while (!lustack.isEmpty()) {//空じゃないときに回る 計算
            reduce();
        }
        return true;
    }

    private boolean getOperand() {
        oper = lex.get();//左辺の値
        Node var = Variable.isMatch(env, oper);
        if (var != null) {
            first.push(var);//変数ならpush
        }
        else{
            first.push(new Const(oper.getValue()));
        }
        return true;
    }

    private void reduce() {
        Node firstp = first.pop();
        Node secondp = first.pop();
        LexicalType type = lustack.pop().getType();
        first.push(new Operation(firstp,secondp,type));
    }

    @Override
    public Value getValue() {
        return first.peek().getValue();
    }

    
}
//exprクラスのgetvalueは計算した値を返す
//        boolean reduce;
//        if(!getOperand()){
//            return false;
//        }
//        
//        while (true) {
//            oper = lex.get();
//            
//            if (oper == null) {
//                break;
//            }
//            else if(!(exprset.contains(oper.getType()))||oper.getType == LexicalType.LP || oper.getType ==LexicalType.NL||oper.getType() == LexicalType.EOF){
//                lex.unget(oper);
//                break;
//            }
//        }
//        return false;
//    }
//    
//    public boolean getOperand(){
//        oper = lex.get();
//        
//    }

        //    @Override
//    public boolean Parse() {
//        Node opr =getOperand();
//        LexicalUnit lu =lex.get();
//        lex.unget(lu);
//        if (opr != null) {
//            return opr.Parse();
//        }
//        return false;
//    }
//    private Node getOperand(){//Nodetypeみてね
//        LexicalUnit lu = lex.get();
//        switch (lu.getType()) {
//            case RP://（
//                lu = lex.get();
//                Node pn = Expr.isMatch(env, lu);
//                if(pn.Parse() == false)return null;
//                lu = lex.get();
//                if(lu.getType() == LexicalType.LP) return pn;
//                else return null;
//            case NAME://変数
//                return Variable.isMatch(env, lu);
//            case INTVAL://定数
//                return INT_CONSTANT.isMatch(env, lu);
//            case DOUBLEVAL://定数
//                return DOUBLE_CONSTANT.isMatch(env, lu);
//            case LITERAL:
//                return STRING_CONSTANT.isMatch(env,lu);
//            default:
//                lex.unget(lu);
//                return null;
//        }
//        //Operandのかっこ、変数、定数でなければnullを返す
//        //そのさいにはunget
        //    }

