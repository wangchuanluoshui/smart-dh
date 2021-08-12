package com.hyn.activiti.entity;

import lombok.Data;
import org.activiti.engine.repository.ProcessDefinition;

/*
 * @Classname FlowDefinitionVO
 * @Description TODO
 * @Date 2020-12-22 19:36
 * @Created by 62538
 */
@Data
public class FlowDefinitionVO {

    String flowId;
    String flowName;
    String flowFile;
    String flowKey;
    Integer flowVersion;
    String flowStatus;


}
