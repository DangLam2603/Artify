package com.example.artworksharingplatform.config;


import com.example.artworksharingplatform.service.JWTServices.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JWTConfiguration extends OncePerRequestFilter {

    private final JWTService _jwtService;
    private final UserDetailsService _userDetailsService;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException
    {
        final String authHeader = request.getHeader("Authorization");
        final String JwtToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")){ //remember jwt start with bearer
            filterChain.doFilter(request,response);
            return;
        }
        //exact jwt token
        JwtToken = authHeader.substring(7); // take the string start after 7 letter "Bearer " (whitespace is )
        userEmail = _jwtService.GetUserEmailJWT(JwtToken);// todo exact the email form token => make a function to do it
        if (userEmail !=null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this._userDetailsService.loadUserByUsername(userEmail);
            if (_jwtService.isTokenValid(JwtToken, userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                  userDetails,
                  null,
                  userDetails.getAuthorities()
                );
                authToken.setDetails( new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
