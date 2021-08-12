package com.hyn.activiti.controller;

import com.hyn.activiti.entity.AddFlowDefinitionVO;
import com.hyn.activiti.entity.FlowDefinitionVO;
import com.hyn.bo.DeleteIdBo;
import com.hyn.common.ICodes;
import com.hyn.common.IResult;
import com.hyn.common.IResultUtil;
import com.hyn.common.PageReponse;
import com.hyn.utils.MultipartFileToFileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Api(value = "processDefinition", tags = "流程定义模块")
@RestController
@RequestMapping("activiti/processDefinition")
public class ProcessDefinitionController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessRuntime processRuntime;

    @Value("${system-bussines.bmpn-file-path}")
    private String sysBpmnFilePath;


    @ApiOperation(value = "上传bpmn文件")
    @PostMapping(value = "/upload-bpmnfile")
    public IResult<String> uploadBpmnfile(@RequestParam("file") MultipartFile multipartFile) {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        String bpmnfilepath = sysBpmnFilePath + "\\" + new SimpleDateFormat("yyyyMMddHHmmss").format(date);
        String result = MultipartFileToFileUtils.saveFile(multipartFile, bpmnfilepath);
        if (StringUtils.isEmpty(result)) {
            return IResultUtil.responseMsg(ICodes.CODE_9999);
        } else {
            return IResultUtil.responseMsg(ICodes.CODE_0000, result);
        }
    }

    @ApiOperation(value = "新增流程定义")
    @PostMapping(value = "/add-definition")
    public IResult<String> addDefinition(@ApiParam(value = "新增流程定义信息", required = true) @RequestBody AddFlowDefinitionVO addFlowDefinitionVO) {
        String resultCode = ICodes.CODE_0000;
        File file = new File(addFlowDefinitionVO.getFlowFilePath());
        if (!file.isFile()) {
            return IResultUtil.responseMsg(ICodes.CODE_9999);
        }
        try {
            Deployment deployment = repositoryService.createDeployment()//初始化流程
                    .addInputStream(file.getName(), new FileInputStream(file))
                    .name(addFlowDefinitionVO.getFlowName())
                    .deploy();
            System.out.println(deployment);
        } catch (FileNotFoundException e) {
            resultCode = ICodes.CODE_9999;
            e.printStackTrace();
        }
        return IResultUtil.responseMsg(resultCode);
    }

    @ApiOperation(value = "查询流程定义")
    @GetMapping(value = "/getDefinitions")
    public PageReponse<FlowDefinitionVO> getDefinitions(
            @ApiParam(value = "排序", required = false) @RequestParam(name = "sorter", required = false) String sorter,
            @ApiParam(value = "页码", required = false) @RequestParam(name = "current", required = false) Integer current,
            @ApiParam(value = "页大小", required = false) @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        if (current == null) {
            current = 1;
        }
        if (pageSize == null) {
            pageSize = 20;
        }
        try {
            List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionVersion().desc().listPage((current - 1) * pageSize, pageSize);
            long count = repositoryService.createProcessDefinitionQuery().count();
            processDefinitionList.sort((y, x) -> x.getVersion() - y.getVersion());
            List<FlowDefinitionVO> resultList = processDefinitionList.stream().map(pd -> convertFlowDefinitionVO(pd)).collect(Collectors.toList());
            return new PageReponse<>(current, resultList, pageSize, true, count);
        } catch (Exception e) {
            return new PageReponse<>(1, null, 10, false, 0);
        }
    }

    public FlowDefinitionVO convertFlowDefinitionVO(ProcessDefinition processDefinition) {
        FlowDefinitionVO flowDefinitionVO = new FlowDefinitionVO();
        flowDefinitionVO.setFlowId(processDefinition.getDeploymentId());
        flowDefinitionVO.setFlowName(processDefinition.getName());
        flowDefinitionVO.setFlowKey(processDefinition.getKey());
        flowDefinitionVO.setFlowFile(processDefinition.getResourceName());
        flowDefinitionVO.setFlowVersion(processDefinition.getVersion());
        flowDefinitionVO.setFlowStatus(processDefinition.isSuspended() ? "1" : "0");
        return flowDefinitionVO;
    }

    //获取流程定义XML
    @GetMapping(value = "/getDefinitionXML")
    public IResult getProcessDefineXML(HttpServletResponse response,
                                       @RequestParam("flowId") String deploymentId,
                                       @RequestParam("flowFile") String resourceName) {
        InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
        BpmnModel bpmnModel = null;
        try {
            bpmnModel = new BpmnXMLConverter().convertToBpmnModel(XMLInputFactory.newInstance().createXMLStreamReader(inputStream));
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        return IResultUtil.responseMsg(ICodes.CODE_0000, bpmnModel);
    }

    @GetMapping(value = "/getDeployments")
    public PageReponse<HashMap<String, Object>> getDeployments(@ApiParam(value = "排序", required = false) @RequestParam(name = "sorter", required = false) String sorter,
                                                               @ApiParam(value = "页码", required = false) @RequestParam(name = "current", required = false) Integer current,
                                                               @ApiParam(value = "页大小", required = false) @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
        try {
            List<Deployment> list = repositoryService.createDeploymentQuery().list();
            for (Deployment dep : list) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", dep.getId());
                hashMap.put("name", dep.getName());
                hashMap.put("deploymentTime", dep.getDeploymentTime());
                listMap.add(hashMap);
            }
            return new PageReponse<HashMap<String, Object>>(current, listMap, pageSize, true, list.size());

        } catch (Exception e) {
            return new PageReponse<HashMap<String, Object>>(current, listMap, pageSize, false, 0);
        }
    }

    //删除流程定义
    @ApiOperation(value = "删除流程定义")
    @RequestMapping(value = "/delDefinition", method = RequestMethod.DELETE)
    @ResponseBody
    public IResult delDefinition(
            @ApiParam(value = "流程定义ID", required = true) @RequestBody DeleteIdBo ids) {
        try {
            //删除数据
            ids.getIds().forEach(id -> repositoryService.deleteDeployment(id, true));
            return IResultUtil.responseMsg(ICodes.CODE_0000);
        } catch (Exception e) {
            return IResultUtil.responseMsg(ICodes.CODE_9994);
        }
    }
}
