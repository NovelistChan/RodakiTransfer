package com.ics.transformer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javafx.util.Pair;

/*
读取一个DSL模型文件，将其转换为数据对象与转换逻辑
 */
public class DSLReader {

    String inputName;

    String outputName;

    DSLReader(String fileName) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String str;
            // 读取 Rule { 一行
            in.readLine();
            // 读取 from
            str = in.readLine().split(":")[1];
            inputName = str.replace(" ", "");
            // 读取 to
            str = in.readLine().split(":")[1];
            outputName = str.replace(" ", "").replace("{", "");
            // 读取 projection
            ArrayList<Pair<String, String>> projectionPair = new ArrayList<>();
            while (!(str = in.readLine()).contains("}")) {
                str = str.replace(" ", "").replace(",", "");
                String toElement = str.split("<-")[0];
                String fromElement = str.split("<-")[1].replace(".", " ").split(" ")[1];
                Pair<String, String> pair = new Pair<>(fromElement, toElement);
                projectionPair.add(pair);
            }
            ProjectionDescriptor projectionDescriptor = new ProjectionDescriptor(inputName,
                outputName, projectionPair);
            System.out.println(projectionDescriptor.projection());
        } catch (IOException e) {
        }
    }
}
