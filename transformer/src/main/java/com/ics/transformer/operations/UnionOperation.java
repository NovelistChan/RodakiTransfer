package com.ics.transformer.operations;


import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnionOperation {

    // opLeft = U(opRightList)

    private String opLeft;
    private ArrayList<String> opRightList;
}
