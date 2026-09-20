package com.example.bp.security;

import com.example.bp.dal.repository.IridologistRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final IridologistRepository iridologistRepository;

    public CustomUserDetailsService(IridologistRepository iridologistRepository) {
        this.iridologistRepository = iridologistRepository;
    }

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return iridologistRepository.findByUsername(username)
                .map(iridologist -> new User(
                        iridologist.getUsername(),
                        iridologist.getPassword(),
                        Collections.emptyList()
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
