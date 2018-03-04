package com.cy.example.demo.Controller;

import org.springframework.security.core.Authentication;
import com.cy.example.demo.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class HomeController {
    @Autowired
    AppRoleRepository roleRepository;

    @Autowired
    AppUserRepository userRepository;

    @Autowired
    ReportItemRepository reportitemRepository;

    @RequestMapping("/")
    public String mainpage(Model model){
        return "mainpage";
    }

    @RequestMapping("/login")
    public String login(Model model)
    {
        return "login";
    }

    @RequestMapping("/logout")
    public String logout(Model model)
    {
        return "mainpage";
    }

    @RequestMapping("/listlostitem")
    public String listlostitem(Model model)
    {
        model.addAttribute("reportitems",reportitemRepository.findReportItemsByItemStatus("lost"));
        return "listitem";
    }

    @RequestMapping("/listfounditem")
    public String listfounditem(Authentication auth, Model model)
    {

        //allow users to see a list of their items that have been found
        model.addAttribute("reportitems",reportitemRepository.findReportItemsByUsersInAndItemStatus(
                userRepository.findAppUserByUsername(auth.getName()), "found"));
        return "listitem";
    }

    @RequestMapping("/listlostitemadm")
    public String listlostitemadm(Model model)
    {
        model.addAttribute("reportitems",reportitemRepository.findReportItemsByItemStatus("lost"));
        return "listitemadm";
    }

    @RequestMapping("/listfounditemadm")
    //public String listfounditemadm(Authentication auth, Model model)
    public String listfounditemadm(Model model)
    {
        //allow users to see a list of their items that have been found
        model.addAttribute("reportitems",reportitemRepository.findReportItemsByItemStatus("found"));
        return "listitemadm";
    }



    @GetMapping("/register")
    public String registerUser(Model model)
    {
        model.addAttribute("user",new AppUser());
        return "registration";
    }

    @PostMapping("/register")
    public String saveUser(@Valid @ModelAttribute("user") AppUser user, BindingResult result, HttpServletRequest request)
    {
        if(result.hasErrors())
        {
            return "registration";
        }

        if(request.getParameter("isAdmin")!=null) // where does that come from
            user.addRole(roleRepository.findAppRoleByRoleName("ADMIN"));
        else
            user.addRole(roleRepository.findAppRoleByRoleName("USER"));
        userRepository.save(user);
        return "redirect:/login";
    }

    @RequestMapping("/addreportitem")
    public String addreportitem(Model model, Authentication auth)
    {
        ReportItem reportItem  = new ReportItem();
        //System.out.println ("addreportitem (request) = reportItem id / Name = " +  reportItem.getId() + " / " + reportItem.getItemName());
        reportitemRepository.save(reportItem);

        //System.out.println("addreportitem getname = " + auth.getName());
        model.addAttribute("reportitem", reportItem);
        model.addAttribute("users",userRepository.findAppUserByUsername(auth.getName()));
        return "addreportitem";
    }

    @PostMapping("/addreportitem")
   // public String addreportitem(HttpServletRequest request,@Valid @ModelAttribute("reportitem") ReportItem reportItem, Authentication auth, BindingResult result, Model model)
    public String addreportitem(HttpServletRequest request,@Valid @ModelAttribute("reportitem") ReportItem reportItem, BindingResult result, Model model)
    {
        //System.out.println ("addreportitem (post) = reportItem id / Name = " +  reportItem.getId() + " / " + reportItem.getItemName());
        if(result.hasErrors())
        {
            return "addusertoreport";
        }
        String userid = request.getParameter("userid");
        AppUser appuser = userRepository.findOne(new Long(userid));
        reportItem.addUsertoReport(appuser);

        reportItem.setItemStatus("lost");
        reportitemRepository.save(reportItem);
        model.addAttribute("reportlist",reportitemRepository.findAll());

        return "redirect:/";
    }

    @RequestMapping("/addreportitemadm")
    //public String addreportitemadm(HttpServletRequest request, Model model)
    public String addreportitemadm(Model model)
    {
        ReportItem reportItem  = new ReportItem();
        reportitemRepository.save(reportItem);

        model.addAttribute("reportitem", reportItem);
        model.addAttribute("users",userRepository.findAll());

        return "addreportitemadm";
    }

    @PostMapping("/addreportitemadm")
    //public String addreportitemadm(HttpServletRequest request,@Valid @ModelAttribute("reportitem") ReportItem reportItem, Authentication auth, BindingResult result, Model model)
    public String addreportitemadm(HttpServletRequest request,@Valid @ModelAttribute("reportitem") ReportItem reportItem, Authentication auth, BindingResult result, Model model)
    {
        if(result.hasErrors())
        {
            return "addreportitemadm";
        }

        String userid = request.getParameter("userid");
        AppUser appuser = userRepository.findOne(new Long(userid));
        reportItem.addUsertoReport(appuser);

        reportitemRepository.save(reportItem);
        model.addAttribute("reportlist",reportitemRepository.findAll());
        return "redirect:/";
    }

    @RequestMapping("/addusertoreport")
    public String addusertoreport(HttpServletRequest request, Model model) //, @ModelAttribute("reportid")String reportid)
    {
       String reportid = request.getParameter("reportid");

        //System.out.println ("addusertoreport = reportid = " +  reportid);
        model.addAttribute("reportitem", reportitemRepository.findOne(new Long(reportid)));
        model.addAttribute("users",userRepository.findAll());

        return "addusertoreport";
    }

    @PostMapping("/saveusertoreport")
    public String saveusertoreport(HttpServletRequest request, @ModelAttribute("reportitem") ReportItem reportItem)
    {
        String userid = request.getParameter("userid");
        //System.out.println("Reportid from add user to report item:"+reportItem.getId()+" User name:"+userid);
        reportItem.addUsertoReport(userRepository.findOne(new Long(userid)));
        reportitemRepository.save(reportItem);
        return "redirect:/";
    }

    /* flip the item status between lost and found by the administrator */
    @RequestMapping(value="/processupdstatus", params={"id"}, method=GET)
    public String processupdstatus(@RequestParam("id") String id, Model model)
    {
        //System.out.println("Entering processupdstatus id = " +id);
        HashSet<ReportItem> reportItemList = reportitemRepository.findReportItemsById(Long.parseLong(id));
        for (ReportItem reportItem: reportItemList) {
            if (reportItem.getItemStatus() == "lost")
            {  reportItem.setItemStatus("found"); }
            else if (reportItem.getItemStatus() == "found")
            {   reportItem.setItemStatus("lost"); }

            reportitemRepository.save(reportItem);
        }
        return "mainpage";
    }


    @RequestMapping("/addlostitem")
    public String addlostitem(Model model)
    {
        ReportItem reportItem  = new ReportItem();
        //System.out.println ("addreportitem (request) = reportItem id / Name = " +  reportItem.getId() + " / " + reportItem.getItemName());
        reportitemRepository.save(reportItem);

        //System.out.println("addreportitem getname = " + auth.getName());
        model.addAttribute("reportitem", reportItem);
        return "addlostitem";
    }

    @PostMapping("/addlostitem")
    public String addlostitem(@Valid @ModelAttribute("reportitem") ReportItem reportItem, BindingResult result, Model model)
    {
        //System.out.println ("addreportitem (post) = reportItem id / Name = " +  reportItem.getId() + " / " + reportItem.getItemName());
        if(result.hasErrors())
        {
            return "addlostitem";
        }

        reportItem.setItemStatus("lost");
        reportitemRepository.save(reportItem);
        model.addAttribute("reportlist",reportitemRepository.findAll());

        return "redirect:/";
    }

    @RequestMapping("/searchitem")
    public String searchItem(HttpServletRequest request, @ModelAttribute("reportitem") ReportItem reportItem,BindingResult result,Model model)
    {

        return "searchform";
    }

    @PostMapping("/searchitem")
    public String searchItem(HttpServletRequest request, Model model){

        String searchString1 = request.getParameter("itemtype");
        String searchString2 = request.getParameter("itemname");
       // System.out.println("searchString1 is " + searchString1);
       // System.out.println("searchString2 is " + searchString2);
        model.addAttribute("reportitems",reportitemRepository.findReportItemsByItemTypeAndItemName(searchString1, searchString2));
        return "listitem";
    }

    @RequestMapping("/displayclothes")
    public String displayclothes(Model model)
    {
        model.addAttribute("reportitems",reportitemRepository.findReportItemsByItemTypeAndItemStatus("Clothes", "lost"));
        return "listitem";
    }
    @RequestMapping("/displaypets")
    public String displaypets(Model model)
    {
        model.addAttribute("reportitems",reportitemRepository.findReportItemsByItemTypeAndItemStatus("Pets", "lost"));
        return "listitem";
    }
    @RequestMapping("/displayother")
    public String displayother(Model model)
    {
        model.addAttribute("reportitems",reportitemRepository.findReportItemsByItemTypeAndItemStatus("Other", "lost"));
        return "listitem";
    }


}
