package com.virusvaccine.service;

import com.virusvaccine.dto.LoginRequest;
import com.virusvaccine.dto.Member;
import com.virusvaccine.dto.SignUpRequest;
import com.virusvaccine.exception.WrongPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import utils.SHA256;

import javax.servlet.http.HttpSession;

public abstract class AccountService {
    public static final String SESSION_KEY_USER = "USER_ID";
    public static final String SESSION_KEY_ROLE = "AUTH_ROLE";

    public enum Role{AGENCY, USER}

    @Autowired
    private HttpSession session;

    public abstract boolean validateDuplicate(String email);

    public abstract void signUp(SignUpRequest signUpRequest);

    public void login(LoginRequest request) {
        Member member = getByEmail(request.getUserEmail());
        String password = member.getPassword();
        Long id = member.getId();

        if (!password.equals(SHA256.getSHA(request.getUserPassword())))
            throw new WrongPasswordException();

        session.setAttribute(SESSION_KEY_USER, id);
        session.setAttribute(SESSION_KEY_ROLE, getRole());
    }

    protected abstract Member getByEmail(String email);

    protected abstract Role getRole();
}
