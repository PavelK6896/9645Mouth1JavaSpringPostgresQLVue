package app.web.pavelk.mouth1.config;

import app.web.pavelk.mouth1.service.CustomOidcUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private CustomOidcUserService customOidcUserService;

    @Autowired
    public void setCustomOidcUserService(CustomOidcUserService customOidcUserService) {
        this.customOidcUserService = customOidcUserService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/**")
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/login**", "/static/js/**", "/js/**", "/error**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .oidcUserService(customOidcUserService)
                .and()
                .defaultSuccessUrl("/").failureUrl("/login?error").permitAll()
                .and()
                .logout().logoutSuccessUrl("/").permitAll();// для выхода
    }
}