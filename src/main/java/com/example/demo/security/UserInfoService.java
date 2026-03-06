//package com.example.demo.security;
//
//
//import com.example.demo.entity.Employee;
//import com.example.demo.repository.EmployeeRepository;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class UserInfoService implements UserDetailsService {
//
//    private final EmployeeRepository repository;
//    private final PasswordEncoder encoder;
//
//    public UserInfoService(EmployeeRepository repository, PasswordEncoder encoder) {
//        this.repository = repository;
//        this.encoder = encoder;
//    }
//
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        Employee user = repository.findByEmail(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
//
//        // Convert roles string -> authorities
//        List<SimpleGrantedAuthority> authorities =
//                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
//
//        return new org.springframework.security.core.userdetails.User(
//                user.getEmail(),
//                user.getPassword(),
//                authorities
//        );
//    }
//
////    public String addEmployee(Employee userInfo) {
////        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
////        repository.save(userInfo);
////        return "User added successfully!";
////    }
////
////
////    public List<Employee> getAllUsers(){
////
////        return repository.findAll();
////
////    }
//
//
//
//}
