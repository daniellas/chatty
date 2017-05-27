package dl.chatty.config;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import dl.chatty.config.properties.SecurityProperties;
import dl.chatty.security.AngularUsernamePasswordAuthenticationFilter;
import dl.chatty.security.Roles;
import dl.chatty.security.UserDetailsController;
import dl.chatty.security.UserDetailsView;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableConfigurationProperties({ SecurityProperties.class })
@ComponentScan(basePackageClasses = { UserDetailsController.class })
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_URL = "/sec/login";
    private static final String LOGOUT_URL = "/sec/logout";

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ObjectMapper mapper;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/index.html", "/app/**", LOGIN_URL).permitAll()
                .anyRequest()
                .authenticated();

        http.addFilterAfter(
                new AngularUsernamePasswordAuthenticationFilter(LOGIN_URL, authenticationManager, mapper),
                UsernamePasswordAuthenticationFilter.class);

        http.logout()
                .logoutUrl(LOGOUT_URL)
                .logoutSuccessHandler((request, response, authentication) -> {
                });

        // TODO Enable and support on UI side
        http.csrf().disable();
    }

    @Bean
    public UserDetailsService inMemoryUserDetailsService() {
        return new InMemoryUserDetailsManager(testUsers());
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            mapper.writeValue(response.getOutputStream(), UserDetailsView.of(authentication));
        };
    }

    private Collection<UserDetails> testUsers() {
        return Stream.of(
                User.withUsername(securityProperties.getCustomerUsername())
                        .password(securityProperties.getCustomerPassword())
                        .roles(Roles.CUSTOMER).build(),
                User.withUsername(securityProperties.getEmployeeUsername())
                        .password(securityProperties.getEmployeePassword())
                        .roles(Roles.CUSTOMER).build())
                .collect(Collectors.toList());
    }
}
