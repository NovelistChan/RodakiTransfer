package com.ics.transformer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
由DSL定义的模型
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ModelDescriptor {

    private String modelName;

    private String[] attributeNameList;

}
