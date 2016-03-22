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
//二つの変数と一つの演算子を覚えて一つのオブジェクトにする
//binaryが計算する
public class Operation extends Node {

    Node operand1;
    Node operand2;
    LexicalType operator;

    public Operation(Node operand1, Node operand2, LexicalType operator) {
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.operator = operator;
    }

    @Override
    public String toString() {
        String symbol;
        switch (operator) {
            case ADD:
                symbol = "+";
                break;
            case SUB:
                symbol = "-";
                break;
            case MUL:
                symbol = "*";
                break;
            case DIV:
                symbol = "/";
                break;
            default:
                symbol = operator.toString();
        }
        return symbol+"["+operand2+", "+operand1+"]";
    }

    @Override
    public Value getValue() {
        if (operand1.getValue().getType() == ValueType.INTEGER) {
            switch (operator) {
                case ADD:
                    return new ValueImpl(operand2.getValue().getIValue()+operand1.getValue().getIValue());
                case SUB:
                    return new ValueImpl(operand2.getValue().getIValue()-operand1.getValue().getIValue());
                case MUL:
                    return new ValueImpl(operand2.getValue().getIValue()*operand1.getValue().getIValue());
                case DIV:
                    return new ValueImpl(operand2.getValue().getIValue()/operand1.getValue().getIValue());
                default:
                    return null;
            }
        }
        else if(operand1.getValue().getType() == ValueType.STRING){
            if (operator == LexicalType.ADD) {
                return new ValueImpl(operand1.getValue().getSValue()+operand2.getValue().getSValue());
            }
        }
        return null;
        //first.getIvalue+second.getIvalue をnew ValueImpl
    }
    
}
