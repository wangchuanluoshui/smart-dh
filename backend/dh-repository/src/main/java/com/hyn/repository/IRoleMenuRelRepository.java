package com.hyn.repository;

import com.hyn.pojo.RoleMenuRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IRoleMenuRelRepository extends JpaRepository<RoleMenuRelation, String>, JpaSpecificationExecutor<RoleMenuRelation> {

    List<RoleMenuRelation> findByRoleId(String roleId);

    void deleteByRoleId(String roleId);

}
