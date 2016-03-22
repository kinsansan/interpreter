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
//���Z�q�͈�œ�̐����܂ł����������Ȃ�
//�ŏ�����܂Ƃ߂Ă����܂Ƃ܂�����o�C�i���ɂ���1*a+b-2�̏ꍇ(((1*a)+b)-2)
//operator���Z�q��operand�ϐ������ꂼ��X�^�b�N�ɓ����
//binary�N���X
//public �݂�Ȃ́@protected ������ private ����́@���Ȃ��@�p�b�P�[�W���O���[�o��
public class Expr extends Node {

    LexicalAnalyzer lex;
    static EnumSet<LexicalType> firstset = null;//�d����F�߂Ȃ��W���@static�ɂ���ƒl��ێ��ł���̂ŏ������͈�x�ōς�
    static EnumSet<LexicalType> exprset = null;
    LexicalUnit oper;
    Stack<LexicalUnit> lustack = new Stack<LexicalUnit>();
    Stack<Node> first = new Stack<Node>();

    static Node isMatch(Environment env, LexicalUnit first) {
        if (firstset == null) {
            firstset = EnumSet.of(LexicalType.SUB);//firstset�̏�����
            firstset.add(LexicalType.LP);//�񋓌^�ւ̒ǉ�
            firstset.add(LexicalType.NAME);
            firstset.add(LexicalType.INTVAL);
            firstset.add(LexicalType.DOUBLEVAL);
            firstset.add(LexicalType.LITERAL);
        }

        if (exprset == null) {
            exprset = EnumSet.of(LexicalType.ADD);//exprset�̏�����
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

        //first�W���ɑ����邩
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

    //���E�̐���������X�^�b�N�Ɖ��Z�q�̃X�^�b�N�����
    //���ӂɂ�����̂𐔎��̃X�^�b�N�Ɂ@���Z�q�����ĉ��Z�q�̃X�^�b�N�ɓ����@�E�ӂɂ�����̂��X�^�b�N�� 
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
        while (true) {//�i�[
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
        while (!lustack.isEmpty()) {//�󂶂�Ȃ��Ƃ��ɉ�� �v�Z
            reduce();
        }
        return true;
    }

    private boolean getOperand() {
        oper = lex.get();//���ӂ̒l
        Node var = Variable.isMatch(env, oper);
        if (var != null) {
            first.push(var);//�ϐ��Ȃ�push
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
//expr�N���X��getvalue�͌v�Z�����l��Ԃ�
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
//    private Node getOperand(){//Nodetype�݂Ă�
//        LexicalUnit lu = lex.get();
//        switch (lu.getType()) {
//            case RP://�i
//                lu = lex.get();
//                Node pn = Expr.isMatch(env, lu);
//                if(pn.Parse() == false)return null;
//                lu = lex.get();
//                if(lu.getType() == LexicalType.LP) return pn;
//                else return null;
//            case NAME://�ϐ�
//                return Variable.isMatch(env, lu);
//            case INTVAL://�萔
//                return INT_CONSTANT.isMatch(env, lu);
//            case DOUBLEVAL://�萔
//                return DOUBLE_CONSTANT.isMatch(env, lu);
//            case LITERAL:
//                return STRING_CONSTANT.isMatch(env,lu);
//            default:
//                lex.unget(lu);
//                return null;
//        }
//        //Operand�̂������A�ϐ��A�萔�łȂ����null��Ԃ�
//        //���̂����ɂ�unget
        //    }

