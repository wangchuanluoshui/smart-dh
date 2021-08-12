package com.hyn.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author hyn  
 * @version V1.0  
 * @Title: IPageResponse.java
 * @Package com.hyn.spring.utils
 * @Description: TODO
 * @date 2018年12月9日 下午2:48:16
 */
public class IPageResponse<T> extends PageImpl<T> implements Page<T> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public IPageResponse(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    /**
     * 重写当前页，将当前页加1返回前台，spring data jpa起始页0加1后返回前台
     *
     * @return
     */
    @Override
    public int getNumber() {
        return super.getNumber() + 1;
    }

    @Override
    public Pageable getPageable() {
        return null;
    }

    public PageReponse buildMyPage() {
        return new PageReponse(this.getNumber(), this.getContent(), this.getSize(), true, this.getTotalElements());
    }


}

