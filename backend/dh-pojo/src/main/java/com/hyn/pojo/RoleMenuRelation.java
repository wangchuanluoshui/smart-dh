package com.hyn.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

/*
 * @Classname Role
 * @Description TODO
 * @Date 2020/9/26 22:58
 * @Created by 62538
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "role_menu_rel")
@Setter
@Getter
@ToString
public class RoleMenuRelation extends BaseEntity{

    /**
     * 角色名
     */
    @Column
    private String roleId;

    /**
     * 角色全称
     */
    @Column
    private String menuId;

}
