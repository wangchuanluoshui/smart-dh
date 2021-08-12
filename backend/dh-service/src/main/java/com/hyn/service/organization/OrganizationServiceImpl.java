package com.hyn.service.organization;

import com.hyn.common.ICodes;
import com.hyn.common.IPageResponse;
import com.hyn.common.PageReponse;
import com.hyn.pojo.Organization;
import com.hyn.repository.IOrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/*
 * @Classname IUserService
 * @Description TODO
 * @Date 2020/9/20 16:53
 * @Created by 62538
 */
@Service
public class OrganizationServiceImpl implements IOrganizationService {

    @Autowired
    IOrganizationRepository iOrganizationRepository;

    @Override
    public PageReponse getNameList(String name, Pageable pageable) {
        Page<Organization> organizations = iOrganizationRepository.findAll(this.getWhereClause(name), pageable);
        return new IPageResponse<Organization>(organizations.getContent(), pageable, organizations.getTotalElements()).buildMyPage();
    }

    private Specification<Organization> getWhereClause(String name) {
        return new Specification<Organization>() {

            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Organization> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();

                if (!StringUtils.isEmpty(name)) {
                    predicate.getExpressions()
                            .add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
                }

                return predicate;
            }
        };
    }

    @Override
    public String save(Organization organization) {

        String organizationName = organization.getName();
        List<Organization> organizations = iOrganizationRepository.findByName(organizationName);

        if (organizations.size() > 0) {
            return ICodes.CODE_1003;
        }
        iOrganizationRepository.save(organization);
        return ICodes.CODE_0000;
    }

    @Override
    public String update(Organization organization) {
        String organizationName = organization.getName();
        List<Organization> organizations = iOrganizationRepository.findByName(organizationName);

        Organization currentOrganization = null;
        int organizationListSize = organizations.size();
        for (Organization cOrganization : organizations) {
            if (cOrganization.getId().equals(organization.getId())) {
                currentOrganization = cOrganization;
                organizationListSize--;
            }
        }
        if (organizationListSize != 0) {
            return ICodes.CODE_1003;
        }
        String resultCode = ICodes.CODE_0000;
        try {
            organization.setCreatedTime(currentOrganization.getCreatedTime());
            iOrganizationRepository.saveAndFlush(organization);
        } catch (Exception e) {
            e.printStackTrace();
            resultCode = ICodes.CODE_9993;
        }
        return resultCode;
    }

    @Override
    public String delete(List<String> organizationIds) {
        List<Organization> organizationList = iOrganizationRepository.findByIdIn(organizationIds);
        iOrganizationRepository.deleteAll(organizationList);
        return ICodes.CODE_0000;
    }
}
