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
public class Program extends Node {

    LexicalAnalyzer lex;
    Node stmt_list;

    public static Node isMatch(Environment env, LexicalUnit first) {
        return new Program(env);
    }

    private Program(Environment env) {
        this.env = env;
        lex = env.getInput();
    }

    @Override
    public String toString() {
        return stmt_list.toString();
    }

    @Override
    public boolean Parse() {
        while (true) {
            LexicalUnit lu = lex.get();//�ŏ��̎���̓ǂݍ���

            stmt_list = StmtList.isMatch(env, lu);//�����̎q���ɍ�����isMatch�Ăяo�� �W����Match���Ă�΃I�u�W�F�N�g��Ԃ�
            lex.unget(lu);
            if (stmt_list != null) {
                return stmt_list.Parse();
            }
        }
    }

    @Override
    public Value getValue() {
        return stmt_list.getValue();
    }
    
}
