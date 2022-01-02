package pl.krejnstudio.smarttools.coldbedroomnotifier.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${REGULAR_USER}")
    private String regularUser;

    @Value("${REGULAR_USER_PASSWORD}")
    private String regularUserPassword;

    @Value("${ADMIN_USER}")
    private String adminUser;

    @Value("${ADMIN_USER_PASSWORD}")
    private String adminUserPassword;

    @Value("${SENSOR_USER}")
    private String sensorUser;

    @Value("${SENSOR_USER_PASSWORD}")
    private String sensorUserPassword;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .passwordEncoder(getPasswordEncoder())
            .withUser(regularUser).password(getPasswordEncoder().encode(regularUserPassword)).roles("USER")
            .and()
            .withUser(adminUser).password(getPasswordEncoder().encode(adminUserPassword)).roles("USER", "ADMIN")
            .and()
            .withUser(sensorUser).password(getPasswordEncoder().encode(sensorUserPassword)).roles("SENSOR_USER")
        ;
    }

    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/temperature").hasRole("SENSOR_USER")
                .antMatchers("/**").hasRole("USER")
                .anyRequest().authenticated()
                .and()
            .httpBasic()
            .and()
            .formLogin()
            .defaultSuccessUrl("/", true)
            .and()
            .logout()
            .logoutUrl("/perform_logout")
            .logoutSuccessUrl("/login")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .and().logout().logoutSuccessUrl("/login").permitAll()
            .and().csrf().disable();
    }
}
