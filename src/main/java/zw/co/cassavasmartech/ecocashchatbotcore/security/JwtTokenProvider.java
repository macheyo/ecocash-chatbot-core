package zw.co.cassavasmartech.ecocashchatbotcore.security;


import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.stereotype.Component;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Partner;

import zw.co.cassavasmartech.ecocashchatbotcore.security.partner.PartnerService;
import zw.co.cassavasmartech.ecocashchatbotcore.security.payload.LoginRequest;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${ecocash.chatbot.core.jwt.jwtsecret}")
    private String jwtSecret;
    @Value("${ecocash.chatbot.core.jwt.jwtexpirationinms}")
    private int jwtExpirationInMs;
    @Autowired
    private PartnerService partnerService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;


    public String generateToken(LoginRequest loginRequest){
        final Optional<Partner> optionalPartner = partnerService.findByPartnerId(loginRequest.getUsername());
        if (optionalPartner.isPresent()){
            Partner partner = optionalPartner.get();
            if(passwordEncoder.matches(loginRequest.getPassword(),partner.getPartnerKey())){
                return this.tokenBuilder(loginRequest.getUsername());
            }
        }
        return this.generateLdapToken(loginRequest);
    }

    private String generateLdapToken(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        LdapUserDetailsImpl userPrincipal = (LdapUserDetailsImpl) authentication.getPrincipal();
        return this.tokenBuilder(userPrincipal.getUsername());
    }

    private String tokenBuilder(String username){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public Authentication getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities = Collections.emptyList();

        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
}