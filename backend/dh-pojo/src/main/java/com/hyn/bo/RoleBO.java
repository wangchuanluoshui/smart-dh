package com.hyn.bo;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/*
 * @Classname RoleBO
 * @Description TODO
 * @Date 2020/9/26 22:58
 * @Created by 62538
 */
@Data
public class RoleBO {
    private String id;
    private Date createdTime;
    private Date updatedTime;
    private String roleChinaName;
    private String roleFullName;
}
