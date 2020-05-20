package com.blue.tnb.service;

import com.blue.tnb.model.User;
import com.blue.tnb.repository.UserRepository;
import com.blue.tnb.utils.UserPrinciple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with -> username or email : " + email));

        return UserPrinciple.build(user);
    }

    public List<String> getAllSubscribersThatCanBookTickets(){
        return userRepository.getAllSubscribersThatCanBookTickets();
    }
    public Integer updateSubscribeForUser(String userCredential){
        try{
            String[] headerSplitted=userCredential.substring("Bearer".length()).trim().split("\\.");
            byte[] userDecoded= Base64.getDecoder().decode(headerSplitted[1]);
            String userCredentialDecoded=new String(userDecoded);
            String userEmail=userCredentialDecoded.split(",")[0].split(":")[1];
            userEmail=userEmail.substring(1,userEmail.length()-1);
//            Long userId=userRepository.getUserIdByEmail(userEmail);
            Optional<User> user=userRepository.findByEmail(userEmail);
            if(user.isPresent()){
                userRepository.updateSubscribeForUser(user.get().getId());
                return user.get().getSubscriber() ?1:0;
            }
            else return -1;
        }
        catch(Exception ex){
            return -1;
        }
    }
}
