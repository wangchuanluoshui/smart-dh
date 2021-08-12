package com.hyn.activiti.entity;

import lombok.Data;
import org.activiti.api.process.model.ProcessInstance;

import java.util.Date;

/*
 * @Classname ProcessInstanceVO
 * @Description TODO
 * @Date 2021-01-01 16:44
 * @Created by 62538
 */
@Data
public class ProcessInstanceVO {
    String id;
    String name;
    String status;
    String processDefinitionId;
    String processDefinitionKey;
    Date startDate;
    Integer processDefinitionVersion;
    String resourceName;
    String deploymentId;
}
