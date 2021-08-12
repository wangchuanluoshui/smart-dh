package com.hyn.service.user;

import com.hyn.common.ICodes;
import com.hyn.common.IPageResponse;
import com.hyn.common.PageReponse;
import com.hyn.pojo.Users;
import com.hyn.repository.IRoleRepository;
import com.hyn.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import java.util.Optional;

/*
 * @Classname IUserService
 * @Description TODO
 * @Date 2020/9/20 16:53
 * @Created by 62538
 */
@Service
public class UsersServiceImpl implements IUsersService {

    @Autowired
    IUserRepository iUserRepository;

    @Autowired
    IRoleRepository iRoleRepository;

    @Override
    public PageReponse getUserList(String userName, Integer sex, String mobile, String createdTime, Pageable pageable) {
        Page<Users> users = iUserRepository.findAll(this.getWhereClause(userName, sex, mobile, createdTime), pageable);
        return new IPageResponse<Users>(users.getContent(), pageable, users.getTotalElements()).buildMyPage();
    }

    private Specification<Users> getWhereClause(String userName, Integer sex, String mobile, String createdTime) {
        return new Specification<Users>() {

            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Users> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();

                if (!StringUtils.isEmpty(userName)) {
                    predicate.getExpressions()
                            .add(criteriaBuilder.like(root.get("userName"), "%" + userName + "%"));
                }

                if (!StringUtils.isEmpty(mobile)) {
                    predicate.getExpressions().add(criteriaBuilder.like(root.get("mobile"), "%" + mobile + "%"));
                }

                if (sex != null) {
                    predicate.getExpressions().add(criteriaBuilder.equal(root.<Integer>get("sex"), sex));
                }

                if (!StringUtils.isEmpty(createdTime)) {
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    predicate.getExpressions().add(criteriaBuilder.between(root.get("createdTime"),
                            Date.from(LocalDate.parse(createdTime).atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant()),
                            Date.from(LocalDate.parse(createdTime).atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant())));
                }
                return predicate;
            }
        };
    }



    @Override
    public String save(Users users) {

        String userName = users.getUserName();
        String mobile = users.getMobile();
        String email = users.getEmail();

        List<Users> userList = iUserRepository.findByUserNameOrMobileOrEmail(userName, mobile, email);

        if (userList.size() > 0) {
            return ICodes.CODE_1003;
        }
        users.setPassword((new BCryptPasswordEncoder()).encode(users.getPassword()));
        users.setRoleName(iRoleRepository.getOne(users.getRoleId()).getRoleChinaName());
        iUserRepository.save(users);
        return ICodes.CODE_0000;
    }

    @Override
    public String update(Users users) {
        String userName = users.getUserName();
        String mobile = users.getMobile();
        String email = users.getEmail();

        Optional<Users> optional = iUserRepository.findById(users.getId());
        Users currentUser = optional.get();

        if (!users.getEmail().equals(currentUser.getEmail())) {
            Optional<Users> tmpUsers = iUserRepository.findByEmail(email);
            if (tmpUsers.isPresent() && !tmpUsers.get().getId().equals(currentUser.getId())) {
                return ICodes.CODE_1003;
            }
        }

        if (!users.getUserName().equals(currentUser.getUserName())) {
            Optional<Users> tmpUsers = iUserRepository.findByUserName(userName);
            if (tmpUsers.isPresent() && !tmpUsers.get().equals(currentUser.getId())) {
                return ICodes.CODE_1003;
            }
        }

        if (!users.getMobile().equals(currentUser.getMobile())) {
            Optional<Users> tmpUsers = iUserRepository.findByMobile(mobile);
            if (tmpUsers.isPresent() && !tmpUsers.get().equals(currentUser.getId())) {
                return ICodes.CODE_1003;
            }
        }

        String resultCode = ICodes.CODE_0000;
        try {
            users.setCreatedTime(currentUser.getCreatedTime());
            users.setFacePath(currentUser.getFacePath());
            users.setRoleName(iRoleRepository.findById(users.getRoleId()).get().getRoleChinaName());
            iUserRepository.saveAndFlush(users);
        } catch (Exception e) {
            e.printStackTrace();
            resultCode = ICodes.CODE_9993;
        }
        return resultCode;
    }

    @Override
    public String delete(List<String> userIds) {
        List<Users> usersList = iUserRepository.findByIdIn(userIds);
        iUserRepository.deleteAll(usersList);
        return ICodes.CODE_0000;
    }

}
