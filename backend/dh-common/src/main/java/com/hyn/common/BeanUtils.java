package com.hyn.common;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.util.*;

public class BeanUtils {

    /**
     * 转换单一对象
     *
     * @param clazz
     * @param src
     * @return
     * @throws Exception
     */
    public static <T> T oneCopy(Class<T> clazz, Object src) throws Exception {
        if (src == null) {
            return null;
        }
        T instance = null;
        instance = clazz.newInstance();
        org.springframework.beans.BeanUtils.copyProperties(src, instance, getNullPropertyNames(src));
        return instance;
    }

    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null)
                emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }


    /**
     * List转换
     *
     * @param clazz
     * @param srcList
     * @return
     * @throws Exception
     */
    public static <T> List<T> batchTransform(final Class<T> clazz, List<? extends Object> srcList) throws Exception {
        if (CollectionUtils.isEmpty(srcList)) {
            return Collections.emptyList();
        }

        List<T> result = new ArrayList<>(srcList.size());
        for (Object srcObject : srcList) {
            result.add(oneCopy(clazz, srcObject));
        }
        return result;
    }

}
