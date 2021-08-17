package com.virusvaccine.controller;

import com.virusvaccine.dto.*;

import com.virusvaccine.exception.NotIdenticalPasswordException;
import com.virusvaccine.exception.NotLoginException;
import com.virusvaccine.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;


@RestController
@RequestMapping("/api")
public class UserController {

    static String userKey = "USER_ID";

    private final AccountService<User> userAccountService;

    private final AccountService<Agency> agencyAccountService;

    public UserController(AccountService<User> userAccountService, AccountService<Agency> agencyAccountService) {
        this.userAccountService = userAccountService;
        this.agencyAccountService = agencyAccountService;
    }

    @PostMapping("/user")
    public ResponseEntity<Void> signupUser(@RequestBody @Valid UserSignupRequest signUpRequest) {

        if (!signUpRequest.getPassword1().equals(signUpRequest.getPassword2())) {
            throw new NotIdenticalPasswordException();
        }

        userAccountService.signup(signUpRequest);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // TODO: 2021/08/14 일반 회원 정보 수정
    public ResponseEntity<Void> editUser() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // TODO: 2021/08/14 일반 회원 삭제
    public ResponseEntity<Void> deleteUser(){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/agency")
    public ResponseEntity<Void> signupAgency(@RequestBody @Valid AgencySignUpRequest signUpRequest) {

        if (!signUpRequest.getPassword().equals(signUpRequest.getValidPassword()))
            throw new NotIdenticalPasswordException();

        agencyAccountService.signup(signUpRequest);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // TODO: 2021/08/14 기관 회원 정보 수정
    public ResponseEntity<Void> editAgency() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // TODO: 2021/08/14 기관 회원 삭제
    public ResponseEntity<Void> deleteAgency(){
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest loginRequest, HttpSession session) {
        Long id;
        if(loginRequest.isAgency())
            id = agencyAccountService.login(loginRequest);
        else
            id = userAccountService.login(loginRequest);

        session.setAttribute(userKey, id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        if (session.getAttribute(userKey) == null) {
            throw new NotLoginException();
        }

        session.invalidate();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
