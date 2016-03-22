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
public class Stmt extends Node{
    Environment env;
    Node node;
    static EnumSet<LexicalType> firstset = null;
    public static Node isMatch(Environment env,LexicalUnit unit){
        switch (unit.getType()) {
            case NAME:
                return new NameNode(env);
            case FOR:
                return new ForNode(env);
            case END:
                return new EndNode(env);
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return node.toString(); 
    }
   
    @Override
    public boolean Parse(){
        return false;
    }

    @Override
    public Value getValue() {
        return node.getValue();
    }
    
}
