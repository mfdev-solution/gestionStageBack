package sn.sonatel.mfdev.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.sonatel.mfdev.service.PaymentService;

@RestController
@RequestMapping("/api/payment")
public class PayementController {

    private final PaymentService paymentService;

    public PayementController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<?> getAllPayment() {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.getAllPayement());
    }
}
