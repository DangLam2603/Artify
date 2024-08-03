package com.example.artworksharingplatform.service.JWTServices;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.artworksharingplatform.entity.EWallet;
import com.example.artworksharingplatform.entity.Role;
import com.example.artworksharingplatform.entity.User;
import com.example.artworksharingplatform.model.AuthenticationRequest;
import com.example.artworksharingplatform.model.AuthenticationResponse;
import com.example.artworksharingplatform.model.RegisterRequest;
import com.example.artworksharingplatform.repository.UserRepository;
import com.example.artworksharingplatform.service.EWalletService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository _repository;
    private final PasswordEncoder _passwordEncoder;
    private final JWTService _jwtService;
    private final AuthenticationManager _authMannager;
    private final EWalletService walletService;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        if (registerRequest.getPass() == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        var user = User.builder()
                .name(registerRequest.getUserName())
                .emailAddress(registerRequest.getEmailAddress())
                .pass(_passwordEncoder.encode(registerRequest.getPass()))
                .telephone((registerRequest.getTelephone()))
                .createdDate(Timestamp.valueOf(LocalDateTime.now()))
                .status("ACTIVE")
                .role(Role.AUDIENCE)
                .build();
        _repository.save(user);
        EWallet wallet = new EWallet();
        wallet.setUser(user);
        walletService.createWallet(wallet);
        var jwtToken = _jwtService.generateToken(user);
        return AuthenticationResponse.builder().Token(jwtToken).build();

    }

    public AuthenticationResponse registerCreator(RegisterRequest registerRequest) {
        if (registerRequest.getPass() == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        var user = User.builder()
                .name(registerRequest.getUserName())
                .emailAddress(registerRequest.getEmailAddress())
                .pass(_passwordEncoder.encode(registerRequest.getPass()))
                .telephone((registerRequest.getTelephone()))
                .createdDate(Timestamp.valueOf(LocalDateTime.now()))
                .status("ACTIVE")
                .role(Role.CREATOR)
                .build();
        _repository.save(user);
        EWallet wallet = new EWallet();
        wallet.setUser(user);
        walletService.createWallet(wallet);
        var jwtToken = _jwtService.generateToken(user);
        return AuthenticationResponse.builder().Token(jwtToken).build();

    }

    public AuthenticationResponse registerAdmin(RegisterRequest registerRequest) {
        if (registerRequest.getPass() == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        var user = User.builder()
                .name(registerRequest.getUserName())
                .emailAddress(registerRequest.getEmailAddress())
                .pass(_passwordEncoder.encode(registerRequest.getPass()))
                .telephone((registerRequest.getTelephone()))
                .createdDate(Timestamp.valueOf(LocalDateTime.now()))
                .status("ACTIVE")
                .role(Role.ADMIN)
                .build();
        _repository.save(user);
        EWallet wallet = new EWallet();
        wallet.setUser(user);
        walletService.createWallet(wallet);
        var jwtToken = _jwtService.generateToken(user);
        return AuthenticationResponse.builder().Token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authRequest) {
        _authMannager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPass()));
        var user = _repository.findByEmailAddress(authRequest.getEmail()).orElseThrow();
        if (user.getStatus().equals("ACTIVE") || user.getStatus().equals("REQUESTING")) {
            var jwtToken = _jwtService.generateToken(user);
            return AuthenticationResponse.builder().Token(jwtToken).build();
        } else {
            return null;
        }
    }
}
