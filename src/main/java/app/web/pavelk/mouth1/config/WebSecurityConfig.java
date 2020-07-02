package app.web.pavelk.mouth1.config;


import app.web.pavelk.mouth1.domain.User;
import app.web.pavelk.mouth1.repo.UserDetailsRepo;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
@EnableOAuth2Sso
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http

                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/login**", "/static/js/**", "/error**").permitAll()
                .anyRequest().authenticated()

                .and().formLogin().loginPage("/login")
                .defaultSuccessUrl("/").failureUrl("/login?error").permitAll()

                .and()
                .logout().logoutSuccessUrl("/").permitAll()// для выхода
                .and()

//                .authorizeRequests()
//                .mvcMatchers("/").permitAll()
//                .anyRequest().authenticated()
//                .and()
                .csrf().disable();
    }

    // сохранение пользователя в базу данных
    @Bean
    public PrincipalExtractor principalExtractor(UserDetailsRepo userDetailsRepo) {
        return map -> {
            String id = (String) map.get("sub");

            //  создаем нового пользователя
            User user = userDetailsRepo.findById(id) // ищем по ид
                    .orElseGet(() -> { // если не нашли то создали нового
                User newUser = new User();

                newUser.setId(id);
                newUser.setName((String) map.get("name"));
                newUser.setEmail((String) map.get("email"));
                newUser.setGender((String) map.get("gender"));
                newUser.setLocale((String) map.get("locale"));
                newUser.setUserpic((String) map.get("picture"));

                return newUser;
            });
            user.setLastVisit(LocalDateTime.now());


            return userDetailsRepo.save(user);
        };
    }
}
