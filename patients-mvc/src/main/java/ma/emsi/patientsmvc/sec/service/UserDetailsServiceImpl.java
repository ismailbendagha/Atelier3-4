package ma.emsi.patientsmvc.sec.service;

import ma.emsi.patientsmvc.sec.entities.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private SecurityService securityService;
//    @Autowired
//    private SecurityService securityService;
    public UserDetailsServiceImpl(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser=securityService.loadUserByUserName(username);
        //technique imperative classique
//        Collection<GrantedAuthority> authorities=new ArrayList<>();
//        appUser.getAppRoles().forEach(appRole -> {
//            SimpleGrantedAuthority authority=new SimpleGrantedAuthority(appRole.getRoleName());
//            authorities.add(authority);
//        });

        //technique avec api streams
        Collection<GrantedAuthority> authorities1=
                appUser.getAppRoles()
                        .stream()
                        .map(appRole -> new SimpleGrantedAuthority(appRole.getRoleName()))
                        .collect(Collectors.toList());
        User user=new User(appUser.getUsername(), appUser.getPassword(),authorities1);
        return user;
    }
}
