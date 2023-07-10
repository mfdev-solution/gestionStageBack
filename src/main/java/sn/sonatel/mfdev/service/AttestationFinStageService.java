package sn.sonatel.mfdev.service;

import java.util.List;
import org.springframework.stereotype.Service;
import sn.sonatel.mfdev.domain.AttestationFinStage;
import sn.sonatel.mfdev.repository.AttestationFinStageRepository;

@Service
public class AttestationFinStageService {

    private final AttestationFinStageRepository attestationFinStageRepository;

    public AttestationFinStageService(AttestationFinStageRepository attestationFinStageRepository) {
        this.attestationFinStageRepository = attestationFinStageRepository;
    }

    public List<AttestationFinStage> getAllAttestationFinStage() {
        return attestationFinStageRepository.findAll();
    }

    public AttestationFinStage addAttestationFinStage(AttestationFinStage attestationFinStage) {
        if (attestationFinStage.getContratStage() != null) return attestationFinStageRepository.save(attestationFinStage);
        return null;
    }
}
