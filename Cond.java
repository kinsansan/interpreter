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
    
    static EnumSet<LexicalType> firstset = null;//�d����F�߂Ȃ��W���@static�ɂ���ƒl��ێ��ł���̂ŏ������͈�x�ōς�
    static EnumSet<LexicalType> condset = null;

    static Node isMatch(Environment env, LexicalUnit first) {//isMatch�̒��ŏꍇ����
        if (firstset == null) {
            firstset = EnumSet.of(LexicalType.SUB);//firstset�̏�����
            firstset.add(LexicalType.LP);//�񋓌^�ւ̒ǉ�
            firstset.add(LexicalType.NAME);
            firstset.add(LexicalType.INTVAL);
            firstset.add(LexicalType.DOUBLEVAL);
            firstset.add(LexicalType.LITERAL);
        }
        //first�W���ɑ����邩
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
            condset = EnumSet.of(LexicalType.EQ);//condset�̏�����
            condset.add(LexicalType.GT);//�񋓌^�ւ̒ǉ�
            condset.add(LexicalType.LT);
            condset.add(LexicalType.GE);
            condset.add(LexicalType.LE);
            condset.add(LexicalType.NE);
        }

        LexicalUnit lu = lex.get();//�ŏ��̎���ǂݍ���
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
        expr = Expr.isMatch(env, lu);//Expr���m�F
        lex.unget(lu);
        if (expr == null) {
            return false; //���łȂ����̂����Ă�̂ŃG���[��false��Ԃ�
        }
        if (expr.Parse() == true) {
            return true;//�����Ă���̂�true
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
                return new ValueImpl(left.getValue().getIValue() == expr.getValue().getIValue());//int�^�̂�
            case GT:
                return new ValueImpl(left.getValue().getIValue() > expr.getValue().getIValue());//int�^�̂�
            case LT:
                return new ValueImpl(left.getValue().getIValue() < expr.getValue().getIValue());//int�^�̂�
            case GE:
                return new ValueImpl(left.getValue().getIValue() >= expr.getValue().getIValue());//int�^�̂�
            case LE:
                return new ValueImpl(left.getValue().getIValue() <= expr.getValue().getIValue());//int�^�̂�
            case NE:
                return new ValueImpl(left.getValue().getIValue() != expr.getValue().getIValue());//int�^�̂�
            default:
                return null;
        }
    }
    
    
}
//cond�N���X��getvalue��true��false���𔻒肵�ĕԂ�
