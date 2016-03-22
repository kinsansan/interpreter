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
class EndNode extends Node {
    Environment env;
    LexicalAnalyzer lex;
    LexicalType lt;
    
    static Node isMatch(Environment env,LexicalUnit unit){
        if(unit.getType() == LexicalType.END){
            return new EndNode(env);
        }
        return null;
    }
    
    public EndNode(Environment env){
        this.env = env;
        lex = env.getInput();
    }
    
    
    @Override
    public boolean Parse(){
        LexicalUnit end = lex.get();
        return true;
    }
    
    @Override
    public String toString(){
        return "END";
    }
}
