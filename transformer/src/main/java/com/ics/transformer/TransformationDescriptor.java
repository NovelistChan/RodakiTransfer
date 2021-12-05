package com.ics.transformer;

import java.util.ArrayList;
import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
逻辑/算数运算
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
class TransformationDescriptor {

    private ArrayList<Pair<String, String>> formulationList;
    private String inputModelName;
    private String outputModelName;


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

    TransformationDescriptor(String inputModelName, String outputModelName) {
        this.inputModelName = inputModelName;
        this.outputModelName = outputModelName;
        this.formulationList = new ArrayList<>();
    }

    void addFormulation(String outputElementName, String formulation) {
        this.formulationList.add(new Pair<String, String>(outputElementName, formulation));
    }

    StringBuilder transformation() {
        StringBuilder sb1 = new StringBuilder();
        sb1.append("SingleOutputStreamOperator<Map> ").append("Output")
            .append(this.outputModelName).append(" = ").append(this.inputModelName)
            .append("OutputStream\n").append("\t\t\t\t.map(new MapFunction<Map, Map>() {")
            .append("\n").append("\t\t\t\t\t@Override\n")
            .append("\t\t\t\t\tpublic Map map(Map value) throws Exception {\n")
            .append("\t\t\t\t\t\tMap<String, Object> result = new HashMap<String, Object>();\n");
        ;
        for (Pair<String, String> pair : formulationList) {
            String outputElementName = pair.getKey();
            String formulation = pair.getValue();
            String[] elementList = formulation.split(" ");
            sb1.append("\t\t\t\t\t\tresult.put(\"").append(outputElementName).append("\", ");
            for (String e : elementList) {
                if (e.contains(".")) {
                    String fe = e.split("\\.")[1];
                    sb1.append("value.get(\"").append(fe).append("\") ");
                } else {
                    sb1.append(e).append(" ");
                }
            }
            sb1.append(");\n");
        }
        sb1.append("\t\t\t\t\t\treturn result;\n"
            + "\t\t\t\t\t}\n"
            + "\t\t\t\t});");
        return sb1;
    }
}
