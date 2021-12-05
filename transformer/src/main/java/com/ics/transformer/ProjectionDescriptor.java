package com.ics.transformer;

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
class ProjectionDescriptor {

    private String inputModelName;
    private String outputModelName;
    private String streamName;
    private ArrayList<Pair<String, String>> projectionPair;

    ProjectionDescriptor(String inputModelName, String outputModelName) {
        this.inputModelName = inputModelName;
        this.outputModelName = outputModelName;
        this.projectionPair = new ArrayList<>();
        this.streamName = this.inputModelName + "Output" + this.outputModelName;
    }

    StringBuilder projection() {
        StringBuilder sb1 = new StringBuilder();
        sb1.append("SingleOutputStreamOperator<Map> ").append(this.inputModelName).append("Output")
            .append(this.outputModelName).append(" = ").append(this.inputModelName)
            .append("OutputStream\n").append("\t\t\t\t.map(new MapFunction<Map, Map>() {")
            .append("\n").append("\t\t\t\t\t@Override\n")
            .append("\t\t\t\t\tpublic Map map(Map value) throws Exception {\n")
            .append("\t\t\t\t\t\tMap<String, Object> result = new HashMap<String, Object>();\n");
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
