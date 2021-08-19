package com.hyn.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class UuidUtil {

    /**
     * 主键生成器，未处理
     *
     * @return 32位的UUID
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 主键生成器，已处理
     *
     * @return 32位的UUID
     */
    public static String getUUIDDispose() {
        return getUUID().replace("-", "");
    }

    public static String get16UUID() {
        // 1.开头两位，标识业务代码或机器代码（可变参数）
        String machineId = "";
        // 2.中间四位整数，标识日期
        SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
        String dayTime = sdf.format(new Date());
        // 3.生成uuid的hashCode值
        int hashCode = UUID.randomUUID().toString().hashCode();
        // 4.可能为负数
        if (hashCode < 0) {
            hashCode = -hashCode;
        }
        // 5.算法处理: 0-代表前面补充0; 10-代表长度为10; d-代表参数为正数型
        String value = machineId + dayTime + String.format("%010d", hashCode);
        return value;
    }


}
