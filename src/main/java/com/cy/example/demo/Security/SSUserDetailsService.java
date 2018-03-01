package com.cy.example.demo.Security;


import com.cy.example.demo.Model.AppRole;
import com.cy.example.demo.Model.AppUser;
import com.cy.example.demo.Model.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

//Once you authentification the USerDetails service is to make sure once you have authetification from dtabase you can see what roles they have and access for spring security

@Transactional
@Service
public class SSUserDetailsService implements UserDetailsService {

    private AppUserRepository userRepository;

    public SSUserDetailsService(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
//            Must be from class user
            AppUser user = userRepository.findAppUserByUsername(username);
            if (user == null) {
                System.out.println("User not found with the provided username: " + user.toString());
                return null;
            }

            System.out.println(" user from username " + user.toString());
            return new org.springframework.security.core.userdetails.User(
//                    Return this withUser("user").password("password").authorities("USER").
//                    Dataloader for Spring Security see line in user1.setRoles(Arrays.asList(userRole)); in DataLoader Class
                    user.getUsername(), user.getPassword(), getAuthorities(user));
        }
        catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }
    }

    private Set<GrantedAuthority> getAuthorities(AppUser user) {
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        for (AppRole role : user.getRoles()) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRoleName());
            authorities.add(grantedAuthority);
        }
        System.out.println("User authorities are " + authorities.toString());
        return authorities;
    }

}




