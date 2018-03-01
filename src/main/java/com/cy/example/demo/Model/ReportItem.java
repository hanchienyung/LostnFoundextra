package com.cy.example.demo.Model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
public class ReportItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String itemName;

    private String itemType;

    private String itemDesc;

    private String itemStatus;

    private String image;

    //@ManyToMany(mappedBy="reportItems", fetch = FetchType.LAZY)
    @ManyToMany
    private Set<AppUser> users;


    public ReportItem() {
        this.users = new HashSet<>();
    }

    public ReportItem(String itemName, String itemType, String itemDesc, String itemStatus, String image, Set<AppUser> users) {
        this.itemName = itemName;
        this.itemType = itemType;
        this.itemDesc = itemDesc;
        this.itemStatus = itemStatus;
        this.image = image;
        this.users = new HashSet<>();
    }


    public void addUsertoReport(AppUser aAppUser) {this.users.add(aAppUser);}




    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<AppUser> getUsers() {
        return users;
    }

    public void setUsers(Set<AppUser> users) {
        this.users = users;
    }


    @Override
    public String toString() {
        return "ReportItems{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", itemType='" + itemType + '\'' +
                ", itemDesc='" + itemDesc + '\'' +
                ", itemStatus='" + itemStatus +
                '}';
    }
}
