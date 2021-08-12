package com.hyn.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

import java.util.List;

/*
 * @Classname PageReponse
 * @Description TODO
 * @Date 2020/9/20 21:55
 * @Created by 62538
 */
@Getter
@Setter
public class PageReponse<T> {

    Integer current;
    List<T> data;
    long pageSize;
    Boolean success;
    long total;

    public PageReponse(Integer current, List<T> data, long pageSize, Boolean success, long total) {
        this.current = current;
        this.data = data;
        this.pageSize = pageSize;
        this.success = success;
        this.total = total;
    }
}
