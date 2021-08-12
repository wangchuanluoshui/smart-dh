package com.hyn.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/*
 * @Classname Menu
 * @Description TODO
 * @Date 2020/10/9 21:09
 * @Created by 62538
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "path"})})
@Setter
@Getter
@ToString
public class Menu extends BaseEntity {

    @Column(length = 50, name = "name")
    String name;

    @Column(length = 50, name = "chineseName")
    String chineseName;

    @Column(length = 100, name = "path")
    String path;

    @Column(length = 100, name = "component")
    String component;

    @Column(length = 100, name = "redirect")
    String redirect;

    @Column(length = 100, name = "icon")
    String icon;

    @Column(length = 36, name = "pid")
    String pid;

    @Column(length = 20)
    String showTag;

}
