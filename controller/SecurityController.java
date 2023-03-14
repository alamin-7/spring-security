package com.testSecurity.controller;

import com.testSecurity.models.AuthenticationRequest;
import com.testSecurity.models.AuthenticationResponse;
import com.testSecurity.services.MyUserDetailsService;
import com.testSecurity.util.JwtUtil;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
 public class SecurityController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private JwtUtil jwtutil;
 @RequestMapping("/")
 @ResponseBody
    public String first(){
        return ("<h1>Hello</h1>");
    }

    @RequestMapping("/user")
    @ResponseBody
    public String user(){
        return ("<h1>Hello from user</h1>");
    }

    @RequestMapping("/admin")
    @ResponseBody
    public String admin(){
        return ("<h1>Hello from admin</h1>");
    }


    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
     try {
         authenticationManager.authenticate(
                 new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
         );
     }catch (BadCredentialsException e){
         throw new Exception("Invalid username or password");
     }
     final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
     final String jwt = jwtutil.generateToken(userDetails);

     return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @RequestMapping(value = "/refreshtoken", method = RequestMethod.POST)
    public ResponseEntity<?> createRefreshToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        }catch (BadCredentialsException e){
            throw new Exception("Invalid username or password");
        }
        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtutil.generateRefreshToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

}
