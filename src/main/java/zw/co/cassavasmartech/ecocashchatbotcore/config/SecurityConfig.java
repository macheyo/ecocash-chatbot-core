package zw.co.cassavasmartech.ecocashchatbotcore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.search.LdapUserSearch;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import zw.co.cassavasmartech.ecocashchatbotcore.security.CustomFilter;
import zw.co.cassavasmartech.ecocashchatbotcore.security.JWTConfigurer;
import zw.co.cassavasmartech.ecocashchatbotcore.security.JwtTokenProvider;
import zw.co.cassavasmartech.ecocashchatbotcore.security.MyBasicAuthenticationEntryPoint;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class SecurityConfig{

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, AuthenticationProvider authenticationProvider) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Configuration
    @Order(1)
    public class DialogflowSecurityConfig extends WebSecurityConfigurerAdapter{
        @Autowired
        private MyBasicAuthenticationEntryPoint authenticationEntryPoint;

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication()
                    .withUser("dialogflow").password(passwordEncoder().encode("C@55@v@"))
                    .authorities("ROLE_DIALOGFLOW");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/dialogflow/**")
                    .csrf().disable()
                    .cors().disable()
                    .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    .httpBasic()
                    .authenticationEntryPoint(authenticationEntryPoint);

            http.addFilterAfter(new CustomFilter(),
                    BasicAuthenticationFilter.class);
        }

    }

    @Configuration
    public class ApiWebSecurityConfig extends WebSecurityConfigurerAdapter
    {
        @Value("${ecocash.chatbot.core.ldap.url}")
        private String ldapUrl;
        @Value("${ecocash.chatbot.core.ldap.manager.password}")
        private String ldapPassword;
        @Value("${ecocash.chatbot.core.ldap.basedn}")
        private String baseDn;
        @Value("${ecocash.chatbot.core.ldap.manager.user}")
        private String managerUser;
        @Value("${ecocash.chatbot.core.ldap.searchFilter.login}")
        private String searchFilter;
        @Value("${ecocash.chatbot.core.ldap.UserDnPattern}")
        private String ldapUserDnPattern;
        @Autowired
        @Lazy
        private JwtTokenProvider jwtTokenProvider;

        @Override
        protected void configure (HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable();
            http
                    .csrf()
                    .disable()
                    .cors()
                    .disable()
                    .exceptionHandling()
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/api/auth/**", "/eip/**","/v2/api-docs",
                            "/v2/api-docs",
                            "/configuration/ui",
                            "/swagger-resources/**",
                            "/configuration/security",
                            "/swagger-ui.html",
                            "/webjars/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .apply(securityConfigurerAdapter());
        }

        private JWTConfigurer securityConfigurerAdapter () {
            return new JWTConfigurer(jwtTokenProvider);
        }

        @Bean(BeanIds.AUTHENTICATION_MANAGER)
        @Override
        public AuthenticationManager authenticationManagerBean () throws Exception {
            return super.authenticationManagerBean();
        }


        @Bean
        public BaseLdapPathContextSource contextSource () {
            final DefaultSpringSecurityContextSource source = new DefaultSpringSecurityContextSource(ldapUrl);
            source.setUserDn(managerUser);
            source.setPassword(ldapPassword);
            return source;
        }

        @Bean
        public LdapUserSearch ldapUserSearch () {
            final FilterBasedLdapUserSearch userSearch = new FilterBasedLdapUserSearch(baseDn, searchFilter, contextSource());
            userSearch.setSearchSubtree(true);
            return userSearch;
        }

        @Bean
        public LdapAuthenticationProvider authenticationProvider (BaseLdapPathContextSource contextSource, LdapUserSearch ldapUserSearch){
            final BindAuthenticator bindAuthenticator = new BindAuthenticator(contextSource);
            bindAuthenticator.setUserSearch(ldapUserSearch);

            return new LdapAuthenticationProvider(bindAuthenticator);
        }

        @Bean
        public LdapContextSource ldapContextSource () {
            final LdapContextSource source = new LdapContextSource();
            source.setUrl(ldapUrl);
            source.setBase(baseDn);
            source.setUserDn(managerUser);
            source.setPassword(ldapPassword);
            return source;
        }

        @Bean
        public LdapTemplate internalLdapTemplate (LdapContextSource ldapContextSource){

            return new LdapTemplate(ldapContextSource);
        }

    }

    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }
}