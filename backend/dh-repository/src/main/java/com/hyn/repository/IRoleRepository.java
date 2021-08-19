package com.hyn.repository;

import com.hyn.pojo.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface IRoleRepository extends JpaRepository<Role, String>, JpaSpecificationExecutor<Role> {

    List<Role> findByIdIn(List<String> ids);

    Optional<Role> findByRoleChinaName(String roleChinaName);

    Page<Role> findByRoleChinaName(String roleChinaName, Pageable pageable);

    List<Role> findByRoleChinaNameOrRoleFullName(String roleChinaName, String fullName);

    Optional<Role> findByRoleFullName(String roleFullName);
}
