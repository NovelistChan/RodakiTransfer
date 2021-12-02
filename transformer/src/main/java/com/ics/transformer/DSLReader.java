package com.ics.transformer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
读取一个DSL模型文件，将其转换为数据对象与转换逻辑
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
class DSLReader {

    private String[] inputNameList;

    private String outputName;

    private ArrayList<ProjectionDescriptor> projectionDescriptors;

    private ArrayList<TransformationDescriptor> transformationDescriptors;

    private ArrayList<AggregationDescriptor> aggregationDescriptors;

    enum OperationType {
        PROJECTION, TRANSFORMATION, AGGREGATION, NONE
    }

    private OperationType getOperationType(String line) {
        if (line.contains("<-")) {
            if (line.contains("[")) {
                return OperationType.AGGREGATION;
            }
            if (line.contains("(")) {
                return OperationType.TRANSFORMATION;
            }
            return OperationType.PROJECTION;
        }
        return OperationType.NONE;
    }

    public void printProjectorPairs() {
        for (ProjectionDescriptor pd : projectionDescriptors) {
            if (!pd.getProjectionPair().isEmpty()) {
                System.out.println(
                    pd.getInputModelName() + " project to " + pd.getOutputModelName());
                StringBuilder sb = pd.projection();
                System.out.println(sb);
            }
        }
    }

    void initial() {
        this.projectionDescriptors = new ArrayList<>();
        this.aggregationDescriptors = new ArrayList<>();
        this.transformationDescriptors = new ArrayList<>();
    }

    DSLReader(String fileName) {
//        inputNameList = new ArrayList<>();
        initial();
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String str;
            // 读取 Rule { 一行
            in.readLine();
            // 读取 from
            str = in.readLine().split(":")[1];
            inputNameList = str.replace(" ", "").split(",");
            // 读取 to
            str = in.readLine().split(":")[1];
            outputName = str.replace(" ", "").replace("{", "");
            // 创建 projectionDescriptors
            for (String name : inputNameList) {
                projectionDescriptors.add(new ProjectionDescriptor(name, outputName));
            }
            while (!(str = in.readLine()).contains("}")) {
                switch (getOperationType(str)) {
                    case PROJECTION:
                        str = str.replace(" ", "").replace(",", "");
                        String toElement = str.split("<-")[0].replace(" ", "");
                        String fromElement = str.split("<-")[1].replace(".", " ").split(" ")[1];
                        String fromName = str.split("<-")[1].replace(".", " ").split(" ")[0];
                        Pair<String, String> pair = new Pair<>(fromElement, toElement);
                        for (ProjectionDescriptor pd : projectionDescriptors) {
                            if (pd.getInputModelName().equals(fromName)) {
                                pd.addPair(pair);
                            }
                        }
                        break;
                    case TRANSFORMATION:
                    case AGGREGATION:
                    default:
                        break;
                }
            }
        } catch (IOException e) {
        }
    }
}
