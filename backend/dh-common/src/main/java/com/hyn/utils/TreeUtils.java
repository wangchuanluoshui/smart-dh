package com.hyn.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * list转树形结构数据工具类可根据需求重载方法扩展）
 *
 * @author zhengpeng
 * @version 1.0
 * @create 2020/1/22 13:45
 **/
public class TreeUtils<T> {
    /**
     * 默认ID字段
     */
    private static final String DEFAULT_ID = "id";
    /**
     * 默认父级id字段
     */
    private static final String DEFAULT_PARENT_ID = "pid";
    /**
     * 默认子级 集合字段
     */
    private static final String DEFAULT_CHILDREN = "children";
    /**
     * 顶级节点的 父级id
     */
    private static final Object TOP_PARENT_ID = "-1";

    /**
     * 都采用默认配置 构建树形结构
     *
     * @param list
     * @return
     */
    public static <T> List<T> createTree(List<T> list) {
        return createTree(list, DEFAULT_ID, DEFAULT_PARENT_ID, DEFAULT_CHILDREN);
    }

    /**
     * 只有 实体主键不同 构建树形结构
     *
     * @param list
     * @param idName
     * @return
     */
    public static <T> List<T> createTree(List<T> list, String idName) {
        return createTree(list, idName, DEFAULT_PARENT_ID, DEFAULT_CHILDREN);
    }

    /**
     * 构建树形结构数据
     *
     * @param list      要转换的集合
     * @param idName    实体id字段名
     * @param pIdName   实体父级id字段名
     * @param childName 实体子集集合字段名
     * @return
     */
    public static <T> List<T> createTree(List<T> list, String idName, String pIdName, String childName) {
        List<T> result = new ArrayList<T>();
        try {
            for (T t : list) {
                Class<?> aClass = t.getClass();
                Field parentId = aClass.getDeclaredField(pIdName);
                parentId.setAccessible(true);
                Object o = parentId.get(t);
                if (String.valueOf(o).equals(String.valueOf(TOP_PARENT_ID))) {
                    result.add(t);
                }
            }
            for (T parent : result) {
                recursiveTree(parent, list, idName, pIdName, childName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static <T> T recursiveTree(T parent, List<T> list, String idName, String pIdName, String childName) throws Exception {
        List<T> children = new ArrayList<T>();
        Class<?> pClass = parent.getClass();
        Field id = pClass.getDeclaredField(idName);
        id.setAccessible(true);
        Object o = id.get(parent);
        for (T t : list) {
            Class<?> aClass = t.getClass();
            Field parentId = aClass.getDeclaredField(pIdName);
            parentId.setAccessible(true);
            Object o1 = parentId.get(t);
            if (String.valueOf(o).equals(String.valueOf(o1))) {
                t = recursiveTree(t, list, idName, pIdName, childName);
                children.add(t);
            }
        }
        Field child = pClass.getDeclaredField(childName);
        child.setAccessible(true);
        child.set(parent, children.size() == 0 ? null : children);
        return parent;
    }
}
