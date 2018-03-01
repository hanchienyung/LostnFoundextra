package com.cy.example.demo.Controller;


import com.cy.example.demo.Model.*;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
        model.addAttribute("reportitems",reportitemRepository.findAll());
        //  model.addAttribute("users",userRepository.findAll());
        return "listlostitem";
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
    public String addpledgeditem(Model model, ReportItem reportItem)
    {
        model.addAttribute("users",userRepository.findAll());
        model.addAttribute("reportitem", new ReportItem());
        return "addpledgeditem";
    }

    @PostMapping("/addreportitem")
    public String addreportitem(@Valid @ModelAttribute("aPledge") ReportItem reportdItem, BindingResult result)
    {
        if(result.hasErrors())
        {
            return "addreportditem";
        }
        reportitemRepository.save(reportdItem);
        return "redirect:/";
    }

    @RequestMapping("/addusertoreport")
    public String addusertoreport(HttpServletRequest request, Model model)
    {
        String reportid = request.getParameter("reportid");
        model.addAttribute("newreportg", reportitemRepository.findOne(new Long(reportid)));
        model.addAttribute("users",userRepository.findAll());

        return "addusertopledg";
    }

    @PostMapping("/savesusertoreport")
    public String saveusertopledg(HttpServletRequest request, @ModelAttribute("newpledg") ReportItem reportItem)
    {
        String userid = request.getParameter("userid");
        System.out.println("Reportid from add user to pledge:"+reportItem.getId()+" User id:"+userid);
        reportItem.addUsertoReport(userRepository.findOne(new Long(userid)));
       reportitemRepository.save(reportItem);
        return "redirect:/listreportitem";
    }


    @GetMapping("/search")
    public String getSearch(){
        return "searchform";
    }

   /*
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
