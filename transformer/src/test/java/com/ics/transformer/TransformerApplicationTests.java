package com.ics.transformer;

import java.util.ArrayList;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class TransformerApplicationTests {

    @Test
    void contextLoads() {
    }


    @Test
    void projectionTest() {
        Pair<String, String> pair = new Pair<>("a1", "b1");
        ArrayList<Pair<String, String>> pairArrayList = new ArrayList<>();
        pairArrayList.add(pair);
//        ProjectionDescriptor projectionDescriptor = new ProjectionDescriptor("A", "B",
//            pairArrayList);
//        System.out.println(projectionDescriptor.projection());
    }

    @Test
    void readTest() {
        DSLReader reader = new DSLReader(
            "/Users/novelistchan/Documents/RodakiTransfer/transformer/src/main/resources/ProjectionDSLMultiSrc");
//        for (String name : reader.getInputNameList()) {
//            System.out.println(name);
//        }
        reader.printProjectorPairs();
//        System.out.println(reader.outputName);
    }

    @Test
    void transformationTest() {
//        System.out.println("a <- b".split(" <- ")[1]);
        DSLReader reader = new DSLReader(
            "/Users/novelistchan/Documents/RodakiTransfer/transformer/src/main/resources/TransformationDSL");
//        for (String name : reader.getInputNameList()) {
//            System.out.println(name);
//        }
        reader.printTransformation();
    }

    @Test
    void aggregationTest() {

    }
}
