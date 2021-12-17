package com.ics.transformer.operations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtractOperation {

    // opLeft = partOf(opRight)

    private String opLeft;
    private String opRight;
}
