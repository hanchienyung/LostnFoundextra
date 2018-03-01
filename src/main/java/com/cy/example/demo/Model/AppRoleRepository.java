package com.cy.example.demo.Model;

import com.cy.example.demo.Model.AppRole;
import org.springframework.data.repository.CrudRepository;

public interface AppRoleRepository extends CrudRepository<AppRole, Long> {

    AppRole findAppRoleByRoleName(String roleName);
}
