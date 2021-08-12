package com.hyn.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

/*
 * @Classname Role
 * @Description TODO
 * @Date 2020/9/26 22:58
 * @Created by 62538
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "role", uniqueConstraints = {@UniqueConstraint(columnNames = {"role_china_name", "role_full_name"})})
@Setter
@Getter
@ToString
public class Role extends BaseEntity {

    /**
     * 角色名
     */
    @Column(length = 30, name = "role_china_name")
    private String roleChinaName;

    /**
     * 角色全称
     */
    @Column(length = 30, name = "role_full_name")
    private String roleFullName;

}
