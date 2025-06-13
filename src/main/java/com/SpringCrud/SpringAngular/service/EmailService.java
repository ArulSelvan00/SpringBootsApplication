package com.SpringCrud.SpringAngular.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendInvoice(String toEmail, String paymentId, String customerName) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setSubject("Invoice for Razorpay Payment");
        msg.setText("Hello " + customerName + ",\n\nThank you for your payment.\nPayment ID: " + paymentId + "\n\nRegards,\nSree Sivam Sarees");
        mailSender.send(msg);
    }
}
