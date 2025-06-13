package com.app.music.security;

import com.app.music.entity.User;
import com.app.music.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Lấy thông tin người dùng từ database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Kiểm tra thông tin username và password
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username không được để trống");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            // Mật khẩu rỗng, nhưng nếu người dùng đăng nhập qua Google, mật khẩu có thể là placeholder
            System.out.println("User không có mật khẩu. Có thể là tài khoản Google or Facebook. Đặt mật khẩu giả.");
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    "placeholder-password", // Mật khẩu thay thế cho Google login
                    Collections.emptyList() // Không có role (danh sách rỗng)
            );
        }

        // Trả về UserDetails của Spring Security
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.emptyList() // Nếu không có role, để danh sách trống
        );
    }
}