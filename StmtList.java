/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newlang8;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class StmtList extends Node {

    Node node;
    List<Node> stmt_List = new ArrayList<>();
    LexicalAnalyzer lex;

//    public static Node isMatch(Environment env,LexicalUnit unit){
//        if(unit.getType == LexicalType.NAME){
//            return new StmtList();
//        }
//        else return null;
//    }
    public static Node isMatch(Environment env, LexicalUnit first) {
        LexicalType ft = first.getType();
        //first集合に属するか
        if (ft == LexicalType.IF || ft == LexicalType.DO || ft == LexicalType.WHILE || ft == LexicalType.NAME || ft == LexicalType.FOR || ft == LexicalType.END) {
            return new StmtList(env, first);
        }
        return null;
    }

    private StmtList(Environment env, LexicalUnit first) {
        this.env = env;
        lex = env.getInput();//コンストラクタ
    }

    @Override
    public boolean Parse() {
        while (true) {
            LexicalUnit lu = lex.get();//最初の字句がとれる
            while (lu.getType() == LexicalType.NL) {
                lu = lex.get();
            }
            if (lu.getType() == LexicalType.END) {
                node = EndNode.isMatch(env, lu);
                lex.unget(lu);
                node.Parse();
                stmt_List.add(node);
                break;
            }
            //lex.unget(lu);
            node = Stmt.isMatch(env, lu);//statementにマッチするか確認してもらい違えばnull
            //文かブロックか
            if (node != null) {
                lex.unget(lu);
                if (node.Parse()) {
                    stmt_List.add(node);
                }
            } 
            else if (Block.isMatch(env, lu) != null) {
                node = Block.isMatch(env, lu);//ブロックにマッチするか確認
                if (node != null) {
                    lex.unget(lu);
                    //ブロックのパース
                    if (node.Parse()) {
                        stmt_List.add(node);
                    }
                }
            }
            else if(StmtList.isMatch(env, lu) != null){
                node =StmtList.isMatch(env, lu);
                if (node != null) {
                    lex.unget(lu);
                    if (node.Parse()) {
                        stmt_List.add(node);
                    }
                }
            } 
            else {
                lex.unget(lu);
                return true;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String tmp = "";
        for (int i = 0; i < stmt_List.size(); i++) {
            if (i != 0) {
                tmp += ";";
            }
            tmp += stmt_List.get(i).toString();
        }
        return tmp;
    }

    @Override
    public Value getValue() {
        for (Node node_getValue : stmt_List) {
            node_getValue.getValue();
        }
        return null;
    }

}
//stmtlistのgetvalueで文字の表示
