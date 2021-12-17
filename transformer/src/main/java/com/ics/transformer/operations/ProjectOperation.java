package com.ics.transformer.operations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectOperation {

    // opLeft = opRight

    private OperationObject opLeft;
    //    private String opLeftName;
    private OperationObject opRight;
//    private String opRightName;

    public void project(String opLeftName, String opRightName) {
        opLeft.getOpj().put(opLeftName, opRight.getOpj().get(opRightName));
    }
}
