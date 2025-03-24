package com.moldavets.task_management_system.email.service;

public interface EmailSenderService {
    void send(String to, String subject, String body);
}
