package com.cy.example.demo.Model;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

//Assigns Roles Store of all roles avialble just a list of roles availble as a List.
@Entity
public class AppRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique=true)
    private String roleName;

    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)
        Set<AppUser> users;


    public AppRole() {
        this.users = new HashSet<>();
    }

    public AppRole(String roleName) {
        this.roleName = roleName;
        this.users = new HashSet <>();

    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<AppUser> getUsers() {
        return users;
    }

    public void setUsers(Set<AppUser> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "AppRole{" +
                "roleName='" + roleName + '\'' +
                '}';
    }
}

