package com.hyn.repository;

import java.util.List;
import java.util.Optional;

import com.hyn.pojo.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface IUserRepository extends JpaRepository<Users, String>, JpaSpecificationExecutor<Users> {

    List<Users> findByIdIn(List<String> ids);

    Optional<Users> findByUserName(String userName);

    Optional<Users> findByEmail(String email);

    Optional<Users> findByMobile(String mobile);

    List<Users> findByUserNameOrMobileOrEmail(String userName, String mobile, String email);
}
