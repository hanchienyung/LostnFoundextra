package com.cy.example.demo.Model;

import org.springframework.data.repository.CrudRepository;

import java.util.HashSet;
import java.util.List;

public interface ReportItemRepository extends CrudRepository<ReportItem, Long> {
    HashSet<ReportItem> findReportItemsByUsersIn(AppUser appUser) ;
    HashSet<ReportItem> findReportItemsByUsers(HashSet <AppUser> users);
    HashSet<ReportItem> findReportItemsByItemStatus(String itemstatus);
    HashSet<ReportItem> findReportItemsByUsersInAndItemStatus(AppUser appuser, String itemstatus);
    HashSet<ReportItem> findReportItemsById(Long id);
    HashSet<ReportItem> findReportItemsByItemTypeAndItemNameContains(String itemtype, String itemname);
    HashSet<ReportItem> findReportItemsByItemTypeAndItemStatus(String itemtype, String itemstatus);
}


