package com.ics.transformer.descriptors;

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
public
class TransformationDescriptor extends OperationDescriptor {

    private ArrayList<Pair<String, String>> formulationList;
    private String inputModelName;
    private String outputModelName;


    public TransformationDescriptor(String inputModelName, String outputModelName) {
        this.inputModelName = inputModelName;
        this.outputModelName = outputModelName;
        this.formulationList = new ArrayList<>();
        this.streamName = "TransformationOutput" + this.outputModelName;
    }

    public void addFormulation(String outputElementName, String formulation) {
        this.formulationList.add(new Pair<String, String>(outputElementName, formulation));
    }

    public StringBuilder transformation() {
        StringBuilder sb1 = new StringBuilder();
        sb1.append("SingleOutputStreamOperator<JSONObject> ").
            append(this.inputModelName).append("Transform")
            .append(this.outputModelName).append(" = ").append(this.inputModelName)
            .append("OutputStream\n")
            .append("\t\t\t\t.map(new MapFunction<JSONObject, JSONObject>() {")
            .append("\n").append("\t\t\t\t\t@Override\n")
            .append("\t\t\t\t\tpublic JSONObject map(JSONObject value) throws Exception {\n")
            .append("\t\t\t\t\t\tJSONObject result = new JSONObject();\n");
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
