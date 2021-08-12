package com.hyn.pojo;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/*
 * @Classname BaseEntity
 * @Description TODO
 * @Date 2020/10/9 21:07
 * @Created by 62538
 */
@Setter
@Getter
@ToString
@MappedSuperclass
public class BaseEntity {

    /**
     * 主键id 用户id
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 36, name = "id")
    private String id;

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

}
