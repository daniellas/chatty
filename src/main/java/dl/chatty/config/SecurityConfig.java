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
import dl.chatty.security.AuthenticationSupplier;
import dl.chatty.security.CustomerUsernameEnforcer;
import dl.chatty.security.UsernameSupplier;
import dl.chatty.security.controller.UserDetailsController;
import dl.chatty.security.filter.AngularUsernamePasswordAuthenticationFilter;
import dl.chatty.security.SecurityContextAuthenticationSupplier;
import dl.chatty.security.SecurityContextUsernameSupplier;
import dl.chatty.security.UserDetailsView;
import dl.chatty.security.UsernameEnforcer;

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
        configureReuestsSecurity(http);
        configureLoginFilter(http);
        configureLogout(http);
        configureCsrf(http);
    }

    private void configureReuestsSecurity(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/index.html", "/app/**", LOGIN_URL).permitAll()
                .anyRequest()
                .authenticated();
    }

    private void configureLoginFilter(HttpSecurity http) {
        http.addFilterAfter(
                new AngularUsernamePasswordAuthenticationFilter(LOGIN_URL, authenticationManager, mapper),
                UsernamePasswordAuthenticationFilter.class);

    }

    private void configureLogout(HttpSecurity http) throws Exception {
        http.logout()
                .logoutUrl(LOGOUT_URL)
                .logoutSuccessHandler((request, response, authentication) -> {
                });
    }

    private void configureCsrf(HttpSecurity http) throws Exception {
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

    @Bean
    public AuthenticationSupplier authenticationSupplier() {
        return new SecurityContextAuthenticationSupplier();
    }

    @Bean
    public UsernameSupplier usernameSupplier(AuthenticationSupplier authenticationSupplier) {
        return new SecurityContextUsernameSupplier(authenticationSupplier);
    }

    @Bean
    public UsernameEnforcer customerUsernameEnforcer(AuthenticationSupplier authenticationSupplier) {
        return new CustomerUsernameEnforcer(authenticationSupplier);
    }

    private Collection<UserDetails> testUsers() {
        return Stream.of(
                User.withUsername(securityProperties.getCustomerUsername())
                        .password(securityProperties.getCustomerPassword())
                        .roles(securityProperties.getCustomerRole()).build(),
                User.withUsername(securityProperties.getEmployeeUsername())
                        .password(securityProperties.getEmployeePassword())
                        .roles(securityProperties.getEmployeeRole()).build(),
                User.withUsername(securityProperties.getAdminUsername())
                        .password(securityProperties.getAdminPassword())
                        .roles(securityProperties.getAdminRole()).build())
                .collect(Collectors.toList());
    }
}
