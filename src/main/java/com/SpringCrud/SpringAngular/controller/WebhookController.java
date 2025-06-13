package com.yourproject.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.yourproject.service.EmailService;
import com.yourproject.utils.Utils;

@RestController
@RequestMapping("/api/razorpay")
public class WebhookController {

    @Autowired
    private EmailService emailService;

    private final String webhookSecret = "y@qKWxEn2iGU5mP"; // Replace with your actual webhook secret

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload,
                                                @RequestHeader("X-Razorpay-Signature") String signature) {
        try {
            boolean isValid = Utils.verifyWebhookSignature(payload, signature, webhookSecret);
            if (!isValid) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");

            JSONObject json = new JSONObject(payload);
            JSONObject entity = json.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity");

            String email = entity.optString("email", "default@email.com");
            String name = entity.optJSONObject("notes") != null ? entity.getJSONObject("notes").optString("name", "Customer") : "Customer";
            String paymentId = entity.getString("id");

            emailService.sendInvoice(email, paymentId, name);
            emailService.sendInvoice("arulkarthiksasi@gmail.com", paymentId, name); // Replace with your email

            return ResponseEntity.ok("Invoice sent.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Webhook failed.");
        }
    }
}
