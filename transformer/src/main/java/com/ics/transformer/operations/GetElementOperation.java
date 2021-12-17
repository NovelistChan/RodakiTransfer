package com.ics.transformer.operations;


import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetElementOperation {

    // opLeft = opRight[i]

    private String opLeft;
    private ArrayList<String> opRightList;
    private int index;
}
