package com.ics.transformer.operations;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArithmeticOperation {

    // opLeft = opRight1 calType opRight2


    private String opLeft;
    private String opRight1;
    private String opRight2;


    enum CalculationType {
        PLUS, MIN, MUL, DIV, AND, OR
    }

    String transOperation(CalculationType ct) {
        String toct = "NONE";
        switch (ct) {
            case PLUS:
                toct = "+";
                break;
            case MIN:
                toct = "-";
                break;
            case DIV:
                toct = "/";
                break;
            case MUL:
                toct = "*";
                break;
            case AND:
                toct = "&&";
                break;
            case OR:
                toct = "||";
                break;
            default:
                break;
        }
        return toct;
    }

}
