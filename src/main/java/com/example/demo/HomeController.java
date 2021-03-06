package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Controller
public class HomeController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/logout")
    public String logout() {
        return "redirect:/login?logout=true";
    }

    @RequestMapping("/admin")
    public String admin() {
        return "admin";
    }

    @RequestMapping("/secure")
    public String secure(Principal principal, Model model) {

        String username = principal.getName();
        model.addAttribute("user", userRepository.findByUsername(username));
//        Set<Role> userRoles = new HashSet<>();
//        ArrayList<Role> userRoles = new ArrayList<Role>();
//        userRoles = roleRepository.findAllByUsername(username);

        model.addAttribute("roles", roleRepository.findAllByUsername(username));

//        model.addAttribute("roles", userRoles);
        return "secure";
    }
    @GetMapping("/register")
    public String showRegisterationPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegisterationPage(@Valid @ModelAttribute("user") User user,
                                           BindingResult result, Model model) {
        model.addAttribute("user", user);
        if(result.hasErrors()) {
            user.clearPassword();
            return "register";
        }
        else {
            model.addAttribute("message", "User Account Created");

            user.setEnabled(true);
            Role role = new Role(user.getUsername(), "ROLE_USER");
            Set<Role> roles = new HashSet<Role>();
            roles.add(role);

            roleRepository.save(role);
            userRepository.save((user));
        }
        return "index";
    }


}
