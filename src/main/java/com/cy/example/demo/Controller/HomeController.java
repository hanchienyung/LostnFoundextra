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
    public String listfounditemadm(Authentication auth, Model model)
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

       // userRepository.save(user);
       // return "redirect:/";
    }

    @RequestMapping("/addreportitem")
    public String addpreportitem(Model model)
    {
        ReportItem reportItem  = new ReportItem();
        reportitemRepository.save(reportItem);

        model.addAttribute("users",userRepository.findAll());
        model.addAttribute("reportitem", new ReportItem());
        return "addreportitem";
    }

    @PostMapping("/addreportitem")
    public String addreportitem(@Valid @ModelAttribute("aReport") ReportItem reportItem, BindingResult result, Model model, Authentication auth)
    {
        if(result.hasErrors())
        {
            return "addreportitem";
        }
        reportItem.setItemStatus("lost");

        reportitemRepository.save(reportItem);
       // model.addAttribute("reportlist",reportitemRepository.findAll());
        return "redirect:/";
    }

    @RequestMapping("/addreportitemadm")
    public String addreportitemadm(Model model, ReportItem reportItem)
    {
        model.addAttribute("users",userRepository.findAll());
        model.addAttribute("reportitem", new ReportItem());
        return "addreportitemadm";
    }

    @PostMapping("/addreportitemadm")
    public String addreportitemadm(@Valid @ModelAttribute("aReport") ReportItem reportItem, BindingResult result, Model model)
    {
        if(result.hasErrors())
        {
            return "addreportitemadm";
        }
        reportitemRepository.save(reportItem);
        model.addAttribute("reportlist",reportitemRepository.findAll());
        return "redirect:/";
    }

    @RequestMapping("/addusertoreport")
    public String addusertoreport(HttpServletRequest request, Model model)
    {
        String reportid = request.getParameter("reportid");
        model.addAttribute("newreport", reportitemRepository.findOne(new Long(reportid)));
        model.addAttribute("users",userRepository.findAll());

        return "addusertoreport";
    }

    @PostMapping("/savesusertoreport")
    public String saveusertopledg(HttpServletRequest request, @ModelAttribute("newreport") ReportItem reportItem)
    {
        String username = request.getParameter("username");
        System.out.println("Reportid from add user to report item:"+reportItem.getId()+" User name:"+username);
      //  reportItem.addUsertoReport(userRepository.findOne(new Long(userid)));
        reportItem.addUsertoReport(userRepository.findAppUserByUsername(username));
        reportitemRepository.save(reportItem);
        return "redirect:/listitem";
    }

    /* flip the item status between lost and found by the administrator */
    @RequestMapping(value="/processupdstatus", params={"id"}, method=GET)
    public String processupdstatus(@RequestParam("id") String id, Model model)
    {
        System.out.println("Entering processupdstatus id = " +id);
        HashSet<ReportItem> reportItemList = reportitemRepository.findReportItemsById(Long.parseLong(id));
        for (ReportItem reportItem: reportItemList) {
            if (reportItem.getItemStatus() == "lost")
            {  reportItem.setItemStatus("found"); }
            else if (reportItem.getItemStatus() == "found")
            {   reportItem.setItemStatus("lost"); }

            reportitemRepository.save(reportItem);
        }

      /*  if (result.hasErrors()){
            return "borrowbookform";
        }*/
        return "mainpage";
        //return "redirect:/";
    }


/*
    @GetMapping("/search")
    public String getSearch(){
        return "searchform";
    }


    @PostMapping("/search")
    public String searchpledgeditem(HttpServletRequest request, Model model)
    {
        String searchItems = request.getParameter(("search");
        model.addAttribute("search", )

        model.addAttribute("pledgeditems",pledgeditemRepository.findAllByItemName());


        return "searchpledgeditem";
    }




    @PostMapping("/search")
    public String showSearchResults(HttpServletRequest request, Model model){
        String searchProducts = request.getParameter("search");
        model.addAttribute("search",searchProducts);
//

//        Expecting multiple parameters or else will throw No parameter available Need to pass as many as are in constructor in Entity.
        model.addAttribute("productsearch",productRepository.findAllByProductNameContainingIgnoreCase(searchProducts));
//
        return "searchproductlist";
    }


    /*

    @PostMapping("/update/pledgeditem")
    public String updateJob(HttpServletRequest request, Model model)
    {
        model.addAttribute("newjob",jobRepository.findOne(new Long(request.getParameter("id"))));
        return "addjob";
    }


    @RequestMapping("/listjobs")
    public String listJobs(Model model)
    {
        model.addAttribute("joblist",jobRepository.findAll());
        return "listjobs";
    }

    @PostMapping("/addskilltojob")
    public String showSkillsForJob(HttpServletRequest request, Model model)
    {
        String jobid = request.getParameter("jobid");
        model.addAttribute("newjob",jobRepository.findOne(new Long(jobid)));

        //Make skills disappear from add form when they are already included (Set already makes it impossible to add multiple)
        model.addAttribute("skillList",skillRepository.findAll());

        return "addskilltojob";
    }

    @PostMapping("/saveskilltojob")
    public String addSkilltoJob(HttpServletRequest request, @ModelAttribute("newjob") AppJob job)
    {
        String skillid = request.getParameter("skillid");
        System.out.println("Job id from add skill to job:"+job.getId()+" Skill id:"+skillid);
        job.addSkilltoJob(skillRepository.findOne(new Long(skillid)));
        jobRepository.save(job);
        return "redirect:/listjobs";
    }

    @PostMapping("/viewjobskills")
    public String viewJobSkills(HttpServletRequest request, Model model)
    {
        String jobid = request.getParameter("jobid");
        AppJob job = jobRepository.findOne(new Long(jobid));
        if(job.getJobSkills().size()<1)
            return "redirect:/listjobs";
        model.addAttribute("newjob",job);
        return "viewjobskills";
    }

    @RequestMapping("/getMyJobs")
    public String getJobsThatApply(Authentication auth, Model model)
    {
        HashSet <Skill> mySkills = new HashSet(userRepository.findAppUserByUsername(auth.getName()).getMySkills());
        HashSet <AppJob> matchingJobs = jobRepository.findAppJobsByJobSkillsIn(mySkills);

        System.out.println(matchingJobs.toString());
        model.addAttribute("joblist",matchingJobs);
        return "viewsuggestedjobs";
    }
    */

}
