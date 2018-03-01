package com.cy.example.demo.setup;


import com.cy.example.demo.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    AppUserRepository appuserRepository;

    @Autowired
    AppRoleRepository approleRepository;

    @Autowired
    ReportItemRepository reportItemRepository;


    @Override
    public void run(String... strings) throws Exception {
        System.out.println("Loading data...");

        AppRole myUserRole = new AppRole("USER");
        approleRepository.save(myUserRole);

        AppRole myAdminRole = new AppRole("ADMIN");
        approleRepository.save(myAdminRole);


        // Add user roles
        AppUser user1 = new AppUser("burgerb", "password", "Bobby",  "Burger", "bob@burger.com");
        user1.addRole(myUserRole);
       // appuserRepository.save(user1);

        AppUser user2 = new AppUser("virginj", "password", "Jane", "Virgin", "jane@virgin.com");
        user2.addRole(myUserRole);
        appuserRepository.save(user2);

        AppUser user3 = new AppUser("smithm", "password", "Mike", "Smith","mike@smith.com");
        user3.addRole(myUserRole);
        appuserRepository.save(user3);

        AppUser user4 = new AppUser("williamsr", "password", "Rod", "Williams", "rod@williams.com");
        user4.addRole(myUserRole);
        appuserRepository.save(user4);

        AppUser user5 = new AppUser("blackl", "password", "Larry", "Black", "larry@black.com");
        user5.addRole(myAdminRole);
        appuserRepository.save(user5);


        ReportItem reportitem1 = new ReportItem();
        reportitem1.setItemName("dress");
        reportitem1.setItemType("clothes");
        reportitem1.setItemDesc("red flared");
        reportitem1.setItemStatus("lost");
        reportitem1.setImage("https://n.nordstrommedia.com/ImageGallery/store/product/Zoom/8/_102183868.jpg?crop=pad&pad_color=FFF&format=jpeg&w=489&h=750&quality=70");
        reportitem1.addUsertoReport(appuserRepository.findAppUserByUsername("virginj"));
        reportItemRepository.save(reportitem1);

     //   user1.addReportItem(reportitem1);
     //   appuserRepository.save(user1);

        ReportItem reportitem2 = new ReportItem();
        reportitem2.setItemName("shorts");
        reportitem2.setItemType("clothes");
        reportitem2.setItemDesc("sweat blue");
        reportitem2.setItemStatus("lost");
        reportitem2.addUsertoReport(appuserRepository.findAppUserByUsername("smithm"));
        reportItemRepository.save(reportitem2);

    //    user2.addReportItem(reportitem2);
    //    appuserRepository.save(user2);


        ReportItem reportitem3 = new ReportItem();
        reportitem3.setItemName("dog");
        reportitem3.setItemType("pet");
        reportitem3.setItemDesc("small white poodle");
        reportitem3.setItemStatus("lost");
        reportitem3.setImage("https://i.pinimg.com/736x/5b/b5/47/5bb547089cc71e6da7441dde70592dc9--poodle-mix-poodle-cuts.jpg");
        reportitem3.addUsertoReport(appuserRepository.findAppUserByUsername("williamsr"));
        reportItemRepository.save(reportitem3);

     //   user3.addReportItem(reportitem3);
     //   appuserRepository.save(user3);

        ReportItem reportitem4 = new ReportItem();
        reportitem4.setItemName("watch");
        reportitem4.setItemType("other");
        reportitem4.setItemDesc("gold Rolex");
        reportitem4.setItemStatus("lost");
        reportitem4.addUsertoReport(appuserRepository.findAppUserByUsername("blackl"));
        reportItemRepository.save(reportitem4);

       // user4.addReportItem(reportitem4);
      //  appuserRepository.save(user4);

    }
}