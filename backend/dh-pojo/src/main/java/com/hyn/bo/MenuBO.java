package com.hyn.bo;

import lombok.Data;

import java.util.List;

/*
 * @Classname MenuBO
 * @Description TODO
 * @Date 2020/10/9 21:13
 * @Created by 62538
 */
@Data
public class MenuBO {
    String id;
    String name;
    String path;
    String component;
    String redirect;
    String icon;
    String pid;
    String chineseName;
    String showTag;
    List<MenuBO> children;
}
