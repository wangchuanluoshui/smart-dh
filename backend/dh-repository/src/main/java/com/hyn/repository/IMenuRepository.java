package com.hyn.repository;

import com.hyn.pojo.Menu;
import com.hyn.pojo.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface IMenuRepository extends JpaRepository<Menu, String>, JpaSpecificationExecutor<Menu> {

    List<Menu> findByIdIn(List<String> ids);

    Optional<Menu> findByName(String name);

    Page<Menu> findByName(String name, Pageable pageable);

    List<Menu> findByNameOrPath(String name, String path);

}
