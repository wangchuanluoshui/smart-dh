package com.hyn.activiti.controller;

import com.hyn.activiti.entity.ProcessInstanceVO;
import com.hyn.activiti.util.SecurityUtil;
import com.hyn.common.*;
import com.hyn.pojo.Users;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;


@Api(value = "processInstance", tags = "流程实例模块")
@RestController
@RequestMapping("activiti/processInstance")
public class ProcessInstanceController {

    private static final Logger log = LoggerFactory.getLogger(ProcessInstanceController.class);

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private RepositoryService repositoryService;

    @ApiOperation(value = "查询流程实例")
    @GetMapping(value = "/getInstances")
    public PageReponse<ProcessInstanceVO> getInstances(
            @ApiParam(value = "排序", required = false) @RequestParam(name = "sorter", required = false) String sorter,
            @ApiParam(value = "页码", required = false) @RequestParam(name = "current", required = false) Integer current,
            @ApiParam(value = "页大小", required = false) @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        if (current == null) {
            current = 1;
        }
        if (pageSize == null) {
            pageSize = 20;
        }
        Page<ProcessInstance> processInstances = null;
        try {
            processInstances = processRuntime.processInstances(Pageable.of(0, 50));
            List<ProcessInstance> list = processInstances.getContent();
            list.sort((y, x) -> x.getStartDate().toString().compareTo(y.getStartDate().toString()));
            List<ProcessInstanceVO> listMap = new ArrayList<ProcessInstanceVO>();
            for (ProcessInstance pi : list) {
                //因为processRuntime.processDefinition("流程部署ID")查询的结果没有部署流程与部署ID，所以用repositoryService查询
                ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                        .processDefinitionId(pi.getProcessDefinitionId())
                        .singleResult();
                listMap.add(convertFlowDefinitionVO(pi, pd));
            }

            return new PageReponse<ProcessInstanceVO>(current, listMap, pageSize, true, listMap.size());
        } catch (Exception e) {
            return new PageReponse<ProcessInstanceVO>(1, null, 10, false, 0);
        }
    }

    public ProcessInstanceVO convertFlowDefinitionVO(ProcessInstance processInstance, ProcessDefinition processDefinition) {
        ProcessInstanceVO processInstanceVO = new ProcessInstanceVO();
        processInstanceVO.setId(processInstance.getId());
        processInstanceVO.setName(processInstance.getName());
        processInstanceVO.setStatus(processInstance.getStatus().name());
        processInstanceVO.setProcessDefinitionId(processInstance.getProcessDefinitionId());
        processInstanceVO.setProcessDefinitionKey(processInstance.getProcessDefinitionKey());
        processInstanceVO.setStartDate(processInstance.getStartDate());
        processInstanceVO.setProcessDefinitionVersion(processInstance.getProcessDefinitionVersion());
        processInstanceVO.setResourceName(processDefinition.getResourceName());
        processInstanceVO.setDeploymentId(processDefinition.getDeploymentId());
        return processInstanceVO;
    }

    //启动
    @ApiOperation(value = "启动流程实例")
    @PostMapping(value = "/startProcess")
    public IResult startProcess(@ApiIgnore @SessionAttribute("LOGIN_USER") Users users, @ApiParam(value = "流程定义Key", required = true) @RequestParam(value = "processDefinitionKey", required = true) String processDefinitionKey,
                                @ApiParam(value = "流程实例名称", required = true) @RequestParam(value = "instanceName", required = true) String instanceName,
                                @ApiParam(value = "流程实例变量", required = false) @RequestParam(value = "instanceVariable", required = false) String instanceVariable,
                                @ApiParam(value = "流程实例业务id", required = false) @RequestParam(value = "businessKey", required = false) String businessKey) {
        try {
            ProcessInstance processInstance = processRuntime.start(ProcessPayloadBuilder
                    .start()
                    .withProcessDefinitionId(processDefinitionKey)
                    .withName(instanceName)
                    .withBusinessKey("1111")
                    .build());
            return IResultUtil.responseMsg(ICodes.CODE_0000);
        } catch (Exception e) {
            log.error("启动流程失败", e);
            return IResultUtil.responseMsg(ICodes.CODE_9999);
        }
    }

}
