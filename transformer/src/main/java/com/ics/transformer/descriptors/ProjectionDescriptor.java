package com.ics.transformer.descriptors;

import java.util.ArrayList;
import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/*
直接映射操作
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public
class ProjectionDescriptor extends OperationDescriptor {

    private String inputModelName;
    private String outputModelName;
    private ArrayList<Pair<String, String>> projectionPair;

    public ProjectionDescriptor(String inputModelName, String outputModelName) {
        this.inputModelName = inputModelName;
        this.outputModelName = outputModelName;
        this.projectionPair = new ArrayList<>();
        this.streamName = "ProjectionOutput" + this.outputModelName;
    }

    public StringBuilder projection() {
        StringBuilder sb1 = new StringBuilder();
        sb1.append("SingleOutputStreamOperator<JSONObject> ").append(this.inputModelName)
            .append("Project")
            .append(this.outputModelName).append(" = ").append(this.inputModelName)
            .append("OutputStream\n")
            .append("\t\t\t\t.map(new MapFunction<JSONObject, JSONObject>() {")
            .append("\n").append("\t\t\t\t\t@Override\n")
            .append("\t\t\t\t\tpublic JSONObject map(JSONObject value) throws Exception {\n")
            .append("\t\t\t\t\t\tJSONObject result = new JSONObject();\n");
        for (Pair<String, String> pair : projectionPair) {
            sb1.append("\t\t\t\t\t\tresult.put(\"").append(pair.getValue())
                .append("\", value.get(\"").append(pair.getKey()).append("\"));\n");
        }

        sb1.append("\t\t\t\t\t\treturn result;\n"
            + "\t\t\t\t\t}\n"
            + "\t\t\t\t});");
        return sb1;
    }

    public void addPair(Pair<String, String> pair) {
        this.projectionPair.add(pair);
    }
}
