package com.hyn.repository;

import com.hyn.pojo.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface IUserRepository extends JpaRepository<Users, String>, JpaSpecificationExecutor<Users> {

    List<Users> findByIdIn(List<String> ids);

    Optional<Users> findByUserName(String userName);

    Optional<Users> findByEmail(String email);

    Optional<Users> findByMobile(String mobile);

    List<Users> findByUserNameOrMobileOrEmail(String userName, String mobile, String email);

    Optional<Users> findBySourceTypeAndSourceUuid(String sourceType,String sourceUuid);
}
