package sn.sonatel.mfdev.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.sonatel.mfdev.domain.AttestationFinStage;
import sn.sonatel.mfdev.service.AttestationFinStageService;

@RestController
@RequestMapping("/api/attesttion-fin-stage")
public class AttestationFinStageController {

    private final AttestationFinStageService attestationFinStageService;

    public AttestationFinStageController(AttestationFinStageService attestationFinStageService) {
        this.attestationFinStageService = attestationFinStageService;
    }

    @GetMapping
    public ResponseEntity<?> getAllAttestationFinStage() {
        return ResponseEntity.status(HttpStatus.OK).body(attestationFinStageService.getAllAttestationFinStage());
    }

    @PostMapping
    public ResponseEntity<?> addAttestationFinStage(@RequestBody AttestationFinStage attestationFinStage) {
        return ResponseEntity.status(HttpStatus.OK).body(attestationFinStageService.addAttestationFinStage(attestationFinStage));
    }
}
