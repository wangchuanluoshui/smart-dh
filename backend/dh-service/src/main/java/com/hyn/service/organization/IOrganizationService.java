package com.hyn.service.organization;

import com.hyn.common.PageReponse;
import com.hyn.pojo.Organization;
import org.springframework.data.domain.Pageable;

import java.util.List;

/*
 * @Classname IUserService
 * @Description TODO
 * @Date 2020/9/20 16:53
 * @Created by 62538
 */
public interface IOrganizationService {
    PageReponse getNameList(String name, Pageable pageable);

    String save(Organization organization);

    String update(Organization organization);

    String delete(List<String> organizationIds);
}
