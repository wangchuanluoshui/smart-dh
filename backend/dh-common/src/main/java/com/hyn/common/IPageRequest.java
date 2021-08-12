package com.hyn.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author hyn  
 * @version V1.0  
 * @Title: IPageRequest.java
 * @Package com.hyn.spring.utils
 * @Description: TODO
 * @date 2018年12月9日 下午3:19:09
 */
public class IPageRequest {
    private static final Logger log = LoggerFactory.getLogger(IPageRequest.class);

    /**
     * 排序方式
     */
    String sort;

    /**
     * 排序字段
     */
    String sidx;
    /**
     * 当前頁面
     */
    Integer page = 0;
    /**
     * 页大小
     */
    Integer size = 10;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSidx() {
        return sidx;
    }

    public void setSidx(String sidx) {
        this.sidx = sidx;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public IPageRequest(String sorter, Integer page, Integer size) {
        super();
        if (!StringUtils.isEmpty(sorter)) {
            List<String> sortList = parseSorter(sorter);
            if (sortList.size() > 0) {
                this.sidx = sortList.get(0);
                this.sort = sortList.get(1).equals("descend") ? "DESC" : "AES";
            }
        }
        this.page = page;
        this.size = size;
    }

    public IPageRequest() {
        super();
    }

    @Override
    public String toString() {
        return "IPageRequest [sort=" + sort + ", sidx=" + sidx + ", page=" + page + ", size=" + size + "]";
    }

    public AbstractPageRequest getRequestPage() {

        Sort mysort = null;
        List<String> sorts = null;

        if (StringUtils.isEmpty(this.sidx)) {
            this.sidx = "id";
        }
        if (StringUtils.isEmpty(this.sort)) {
            mysort = Sort.by(Sort.Direction.DESC, this.sidx);
        } else {
            if (Sort.Direction.DESC.name().equals(this.sort.toUpperCase())) {
                mysort = Sort.by(Sort.Direction.DESC, this.sidx);
            } else {
                mysort = Sort.by(Sort.Direction.ASC, this.sidx);
            }
        }

        int pagTmp = 0;
        int sizeTmp = 0;
        if (this.page == null || this.page < 1) {
            pagTmp = 0;
        } else {
            pagTmp = this.page - 1;
        }

        if (this.size == null || this.size <= 0) {
            sizeTmp = 20;
        } else {
            sizeTmp = this.size;
        }
        AbstractPageRequest pageable = null;
        if (mysort != null) {
            pageable = PageRequest.of(pagTmp, sizeTmp, mysort);
        }
        return pageable;
    }

    private List<String> parseSorter(String sorter) {
        List result = null;
        try {
            JSONObject jsonObject = JSON.parseObject(sorter);
            List keyList = new ArrayList<>(jsonObject.keySet());
            if (keyList.size() > 0) {
                String key = (String) keyList.get(0);
                result = Arrays.asList(key, jsonObject.getString(key));
            }
        } catch (Exception e) {
            log.error("Parse sorter error!", e);
        } finally {
            if (result == null) {
                result = new ArrayList();
            }
        }
        return result;
    }

}
