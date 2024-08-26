package com.example.application.service;

import com.example.application.data.User;

public interface EmailService {

    boolean sendRegistrationEmail(User user);

}
