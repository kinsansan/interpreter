/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newlang8;

import java.util.List;

/**
 *
 * @author Admin
 */
public class PrintFunction extends Function{

    @Override
    public Value invoke(Expr_list arg) {
        List<Node> exprlist = arg.getList();
        for (Node expr : exprlist) {
            System.out.print(expr.getValue().getSValue());
        }
        System.out.println("");
        return null;
    }
    
}
