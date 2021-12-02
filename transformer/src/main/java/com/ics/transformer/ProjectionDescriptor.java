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
public class ProjectionDescriptor {

    String inputModelName;
    String outputModelName;

    ArrayList<Pair<String, String>> projectionPair;

    StringBuilder projection() {
        StringBuilder sb1 = new StringBuilder();
        sb1.append("\n");
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
            + "\t\t\t\t});;");
        return sb1;
    }
}
