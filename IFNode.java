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
public class IFNode extends Node {

    Environment env;
    LexicalAnalyzer lex;
    Node cond;
    Node body;
    Node stmt;
    Node else_body;

    public static Node isMatch(Environment env, LexicalUnit first) {
        if (first.getType() != LexicalType.IF) {
            return null;
        }
        return new IFNode(env); //����������ĕԂ�
    }

    private IFNode(Environment env) {
        this.env = env;
        lex = env.getInput();
    }

    @Override
    public boolean Parse() {
        LexicalUnit lu = lex.get();//IF

        lu = lex.get();//����ǂ�
        cond = Cond.isMatch(env, lu);
        lex.unget(lu);
        if (cond == null) {
            return false; //���łȂ����̂����Ă�̂ŃG���[��false��Ԃ�
        }
        if (cond.Parse() == false) {
            return false;//�G���[��Ԃ�
        }
        lu = lex.get();//����ǂ�
        if (lu.getType() != LexicalType.THEN) {
            return false;//Then�����ĂȂ��Ƃ�����������G���[
        }
        lu = lex.get();//����ǂ�
        if (lu.getType() == LexicalType.NL) {
            lu = lex.get();//stmtlist
            body = StmtList.isMatch(env, lu);//������statment�ɂȂ��Ă邩
            lex.unget(lu);
            if (body.Parse() != true) {
                return false;
            }
            else_body = Else_if_block.isMatch(env, lu);
            if (else_body == null) {
                return false;
            }
            if (!else_body.Parse()) {
                return false;
            }

            lu = lex.get();//ENDIF
            if (lu.getType() != LexicalType.ENDIF) {
                return false;
            }

            lu = lex.get();//NL
            if (lu.getType() == LexicalType.NL) {
                return true;
            }
            return false;
        } else {//stmt
            stmt = Stmt.isMatch(env, lu);
            lex.unget(lu);
            if (stmt == null) {
                return false;
            }
            if (stmt.Parse() == false) {
                return false;
            }
            lu = lex.get();//����ǂ�
            if (lu.getType() != LexicalType.ELSE) {//ELSE���ǂ���
                if (lu.getType() == LexicalType.NL) {
                    return true;
                }
                return false;
            } else {//ELSE
                lu = lex.get();
                else_body = Stmt.isMatch(env, lu);//������statment�ɂȂ��Ă邩
                lex.unget(lu);
                if (else_body == null || else_body.Parse() == false) {
                    return false;
                }
                lu = lex.get();
                if (lu.getType() == LexicalType.NL) {
                    return true;
                }
                return false;
            }
        }
    }

    @Override
    public String toString() {
        if (body != null) {
            return "IF[" + body.toString() + "]";
        } else {
            if (else_body == null) {
                return "IF[" + stmt.toString() + "]";
            } else {
                return "IF[" + else_body.toString() + "," + stmt.toString() + "]";
            }
        }
    }

    @Override
    public Value getValue() {
        if (body != null) {// if NL stmtlist elseblock ENDIF NL
            if (cond.getValue().getBValue()) {
                //muzui
            }
        } else {
            if (else_body == null) {//if stmt NL
                if (cond.getValue().getBValue()) {
                    stmt.getValue();
                }
            } else {//if stmt ELSE stmt NL
                if (cond.getValue().getBValue()) {
                    stmt.getValue();
                }
                else{
                    else_body.getValue();
                }
            }
        }
        return null;
    }
}
