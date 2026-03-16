package com.example.demo.security;

import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInfoService implements UserDetailsService {

    private final EmployeeRepository repository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserInfoService(EmployeeRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // ✅ Load from Employee entity using email
        Employee employee = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Employee not found with email: " + email));

        // ✅ Role enum directly → authority (e.g. ROLE_ADMIN)
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(employee.getRole().name())
        );

        return new org.springframework.security.core.userdetails.User(
                employee.getEmail(),
                employee.getPassword(),
                employee.isActive(),   // ✅ uses your active field
                true,
                true,
                true,
                authorities
        );
    }
}