package sn.sonatel.mfdev.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.sonatel.mfdev.domain.Payement;
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

    @GetMapping("/etat/{etat}")
    public ResponseEntity<?> getAllPaymentByEtat(@PathVariable("etat") String etat) {
        var payement = paymentService.getAllPayementByEtat(etat);
        if (payement != null) {
            return ResponseEntity.status(HttpStatus.OK).body(payement);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePayment(@PathVariable("id") Long id, @RequestBody Payement payement) {
        var existedPayment = paymentService.updatePayment(id, payement);
        if (existedPayment != null) {
            return ResponseEntity.status(HttpStatus.OK).body(existedPayment);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
