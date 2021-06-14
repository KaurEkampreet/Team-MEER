package edu.greenriver.sdev.capstone.fsmp.config;


import edu.greenriver.sdev.capstone.fsmp.service.AdministratorRepositoryService;
import edu.greenriver.sdev.capstone.fsmp.service.CustodianRepositoryService;
import edu.greenriver.sdev.capstone.fsmp.service.FacilityAdministratorRepositoryService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Security Configuration class for Spring Security Web App
 *
 * @author Robert Cox
 * @version 1.0
 */
@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AdministratorRepositoryService administratorRepositoryService;
    private final FacilityAdministratorRepositoryService facilityAdministratorRepositoryService;
    private final CustodianRepositoryService custodianRepositoryService;


    /**
     * @return an object to encrypt our passwords
     */
    @Bean
    public BCryptPasswordEncoder encoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        auth.userDetailsService(administratorRepositoryService).passwordEncoder(encoder);
        auth.userDetailsService(facilityAdministratorRepositoryService).passwordEncoder(encoder);
        auth.userDetailsService(custodianRepositoryService).passwordEncoder(encoder);
    }

    @Override
    public void configure(WebSecurity web) {
        web
            .ignoring().antMatchers("/js/**")
            .and()
            .ignoring().antMatchers("/css/**")
            .and()
            .ignoring().antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
            .authorizeRequests()
                .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .antMatchers("/home/**").hasAuthority("ROLE_USER")
                .antMatchers("/facility_admin/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_FACILITY_ADMIN")
                .antMatchers("/facility/**").hasAuthority("ROLE_FACILITY_ADMIN")
                .antMatchers("/**").permitAll()
            .and()
            .formLogin()
                .permitAll()
                .loginPage("/login")
                .defaultSuccessUrl("/home")
                .failureUrl("/login?error=true")
            .and()
            .exceptionHandling()
                .accessDeniedPage("/access_denied")
            .and()
            .logout()
                .permitAll()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true");
    }

    @Override
    public String toString() {
        return "SecurityConfig{" +
                "service=" + administratorRepositoryService +
                '}';
    }
}
