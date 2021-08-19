package com.hyn.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_name", "mobile", "email"})})
@Setter
@Getter
@ToString
public class Users extends BaseEntity {
    /**
     * 用户名 用户名
     */
    @Column(length = 30, name = "user_name")
    private String userName;

    /**
     * 密码 密码
     */
    @Column(length = 200, name = "password")
    private String password;

    /**
     * 昵称 昵称
     */
    @Column(length = 30, name = "nick_name")
    private String nickName;

    /**
     * 真实姓名
     */
    @Column(length = 30, name = "real_name")
    private String realname;

    /**
     * 头像
     */
    @Column(length = 300, name = "face_path")
    private String facePath;

    /**
     * 手机号 手机号
     */
    @Column(length = 30, name = "mobile")
    private String mobile;

    /**
     * 邮箱地址 邮箱地址
     */
    @Column(length = 30, name = "email")
    private String email;

    /**
     * 性别
     *
     * @param sex 1:男  0:女  2:保密
     */
    @Column(name = "sex")
    private Integer sex;

    /**
     * 生日 生日
     */
    @Column(name = "birthday")
    private Date birthday;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "created_time")
    private Date createdTime;

    /**
     * 更新时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(name = "updated_time")
    private Date updatedTime;

    @Column(name = "role_id")
    private String roleId;

    @Column(name = "role_name")
    private String roleName;

    @Column
    private String sourceType;

    @Column
    private String sourceUuid;
}