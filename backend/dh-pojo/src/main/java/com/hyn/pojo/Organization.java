package com.hyn.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/*
 * @Classname Organization
 * @Description TODO
 * @Date 2020/10/9 21:15
 * @Created by 62538
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "organization")
@Setter
@Getter
@ToString
public class Organization extends BaseEntity {

    @Column(length = 50, name = "menu_name")
    String name;

    @Column(length = 36, name = "principal_user_id")
    String principalUserId;

    @Column(length = 36, name = "pid")
    String pid;
}
