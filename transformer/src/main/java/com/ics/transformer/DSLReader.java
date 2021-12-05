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

    public void printTransformation() {
        for (TransformationDescriptor td : transformationDescriptors) {
            if (!td.getFormulationList().isEmpty()) {
                System.out.println(td.transformation());
            }
        }
    }

    public void printProjectorPairs() {
        int cnt = 0;
        for (ProjectionDescriptor pd : projectionDescriptors) {
            if (!pd.getProjectionPair().isEmpty()) {
                cnt++;
//                System.out.println(
//                    pd.getInputModelName() + " project to " + pd.getOutputModelName());
                StringBuilder sb = pd.projection();
                System.out.println(sb);
            }
        }
        if (cnt >= 2) {
            StringBuilder sb = new StringBuilder();
            int index = 0;
            for (ProjectionDescriptor pd : projectionDescriptors) {
                if (!pd.getProjectionPair().isEmpty()) {
                    index++;
                    if (index == 1) {
                        sb.append("DataStream<TrafficTransaction> ").append(pd.getStreamName())
                            .append(" = ")
                            .append(pd.getInputModelName())
                            .append(".union(");
                    } else if (index == 2) {
                        sb.append(pd.getInputModelName());
                    } else {
                        sb.append(", ").append(pd.getInputModelName());
                    }
                }
            }
            sb.append(");");
            System.out.println(sb);
        }
    }

    private void addTransformDefinition(String toModelName, String formulation) {
        if (this.transformationDescriptors.isEmpty()) {
            return;
        }
        String fromName = " ";
        String[] elementList = formulation.split(" ");
        for (String element : elementList) {
            if (element.contains(".")) {
                fromName = element.split("\\.")[0];
                break;
            }
        }
        for (TransformationDescriptor td : transformationDescriptors) {
            if (td.getInputModelName().equals(fromName)) {
                td.addFormulation(toModelName, formulation);
            }
        }
    }

    private void initial() {
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
                transformationDescriptors.add(new TransformationDescriptor(name, outputName));
            }

            while (!(str = in.readLine()).contains("}")) {
                String toElement, fromElement, fromName;
                switch (getOperationType(str)) {
                    case PROJECTION:
                        str = str.replace(" ", "").replace(",", "");
                        toElement = str.split("<-")[0].replace("\t", "");
//                        System.out.println("toElement: " + toElement);
//                        System.out.println("replace: " + toElement.replace("\t", ""));
                        fromElement = str.split("<-")[1].replace(".", " ").split(" ")[1];
                        fromName = str.split("<-")[1].replace(".", " ").split(" ")[0];
                        Pair<String, String> pair = new Pair<>(fromElement, toElement);
                        for (ProjectionDescriptor pd : projectionDescriptors) {
                            if (pd.getInputModelName().equals(fromName)) {
                                pd.addPair(pair);
                            }
                        }
                        break;
                    case TRANSFORMATION:
                        str = str.replace(",", "");
                        toElement = str.split(" <- ")[0].replace("\t", "").replace(" ", "");
                        String formulation = str.split(" <- ")[1];
                        addTransformDefinition(toElement, formulation);
                        break;
                    case AGGREGATION:
                    default:
                        break;
                }
            }
        } catch (
            IOException e) {
        }
    }
}
