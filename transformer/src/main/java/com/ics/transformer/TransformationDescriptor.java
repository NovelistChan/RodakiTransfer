package com.ics.transformer;

import lombok.Data;

/*
逻辑/算数运算
 */
//@AllArgsConstructor
//@NoArgsConstructor
@Data
public class TransformationDescriptor {

    String inputModelName;
    String outputModelName;

    String[] formulationList;
}
