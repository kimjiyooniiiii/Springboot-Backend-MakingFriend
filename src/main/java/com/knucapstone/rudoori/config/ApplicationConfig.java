package com.knucapstone.rudoori.config;

import com.knucapstone.rudoori.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

//    @Bean
//    public UserDetailsService userDetailsService(){
//        return new UserDetailsService() {
//            @Override
//            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//               return null;
//            }
//        };
//    }
    //위 로직과 같음
    @Bean
    public UserDetailsService userDetailsService(){
        return userId -> userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자가 없습니다."));
    }

    /**
     *AuthenticationProvider는 인증을 처리하는 인터페이스로, 사용자의 인증을 위해 필요한 정보를 제공하고, 인증을 검증하는 역할을 합니다.
     * 해당 메서드에서는 DaoAuthenticationProvider 객체를 생성하고 설정을 해줍니다.
     * DaoAuthenticationProvider는 데이터베이스에서 사용자 정보를 조회하여 인증을 처리하는 인증 프로바이더입니다.
     * @return
     */
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        //사용자 정보를 가져오는 UserDetailsService 를 설정합니다.
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
