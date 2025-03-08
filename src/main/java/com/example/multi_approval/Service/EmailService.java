package com.example.multi_approval.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String emailId;

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendTaskAssignmentEmail(String approverEmail, int taskId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailId);
        message.setTo(approverEmail);
        message.setSubject("New Task Assigned: Task ID " + taskId);
        message.setText("You have been assigned a new task. Please review and approve.");
        javaMailSender.send(message);
        System.out.println(approverEmail+" has been assigned task:"+taskId);
    }

    public void sendSingleApprovalEmail(String userEmail,int approverId, int taskId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setFrom(emailId);
        message.setSubject("New Task Assigned: Task ID " + taskId);
        message.setText("User: "+ approverId+" has approved task:"+taskId);
        javaMailSender.send(message);
        System.out.println("User: "+ approverId+" has approved task:"+taskId);
    }

    public void sendFinalApprovalNotification(List<String> emails, int taskId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailId);
        message.setTo(emails.toArray(new String[0]));
        message.setSubject("Task ID " + taskId + " Approved");
        message.setText("All approvers have approved Task ID " + taskId);
        javaMailSender.send(message);
        System.out.println("Sending mail to emails"+ emails.toArray(new String[0])+"TaskId:"+taskId+"is approved" );
    }
}