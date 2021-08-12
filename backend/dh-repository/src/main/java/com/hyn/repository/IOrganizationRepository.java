package com.hyn.repository;

import com.hyn.pojo.Menu;
import com.hyn.pojo.Organization;
import com.hyn.pojo.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface IOrganizationRepository extends JpaRepository<Organization, String>, JpaSpecificationExecutor<Organization> {

    List<Organization> findByIdIn(List<String> ids);

    List<Organization> findByName(String userName);

    Page<Organization> findByName(String name, Pageable pageable);

}
