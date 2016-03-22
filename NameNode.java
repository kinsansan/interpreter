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
public class NameNode extends Node {
    Variable va;
    LexicalUnit name;
    LexicalUnit value;
    Environment env;
    LexicalAnalyzer lex;
    LexicalUnit unit;
    Node expr;
    Node exprlist;
   
    static Node isMatch(Environment env, LexicalUnit unit) {
        if (unit.getType() == LexicalType.NAME) {
            return new NameNode(env);
        }
        return null;
    }

    public NameNode(Environment env) {
        this.env = env;
        lex = env.getInput();
    }

    @Override
    public String toString() {
        if (expr ==null) {
            return name.value+"["+value.value+"]";
        }
        else{
            return name.value.getSValue()+"["+expr.toString()+"]";
        }
    }

    @Override
    public boolean Parse() {//=�����Ă���Εϐ�����
        name = lex.get();//name
        
        LexicalUnit lu = lex.get();//EQ��exprlist
        if (lu.getType() == LexicalType.EQ) {
            va = (Variable)Variable.isMatch(env, name);
            value = lex.get();
            expr = Expr.isMatch(env, value);//Expr���m�F
            lex.unget(value);
            if (expr == null) {
                return false; //���łȂ����̂����Ă�̂ŃG���[��false��Ԃ�
            }
            return expr.Parse();
        }
        else{
            exprlist = Expr_list.isMatch(env, lu);
            lex.unget(lu);
            value =lex.get();
            lex.unget(value);
            if (exprlist == null) {
                return false; //���łȂ����̂����Ă�̂ŃG���[��false��Ԃ�
            }
            return exprlist.Parse();
        }
    }

    @Override
    public Value getValue() {
        if (env.library.containsKey(name.getValue().getSValue())) {
            Function func = (Function)env.library.get(name.getValue().getSValue());
            func.invoke((Expr_list)exprlist);
        }
        else{
            va.setValue(expr.getValue());
        }
        return null;
    }
}