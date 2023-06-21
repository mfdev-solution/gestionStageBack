package sn.sonatel.mfdev.service;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import sn.sonatel.mfdev.domain.AttestationPresence;
import sn.sonatel.mfdev.domain.Payement;
import sn.sonatel.mfdev.repository.AttestationPresenceRepository;
import sn.sonatel.mfdev.security.SecurityUtils;

@Service
public class AttestationPresenceService {

    private final AttestationPresenceRepository attesttionPresenceRepository;
    private final PaymentService paymentService;

    public AttestationPresenceService(AttestationPresenceRepository attestationPresenceRepository, PaymentService paymentService) {
        this.attesttionPresenceRepository = attestationPresenceRepository;
        this.paymentService = paymentService;
    }

    @Transactional
    public AttestationPresence save(AttestationPresence attestationPresence) {
        var attestation = attesttionPresenceRepository.save(attestationPresence);
        var payement = new Payement();
        payement.setAttestationPresence(attestation);
        payement.setPaied(false);
        paymentService.addPayement(payement);
        return attestation;
    }

    public void delete(Long id) {
        attesttionPresenceRepository.deleteById(id);
    }

    public Optional<AttestationPresence> findById(Long id) {
        return attesttionPresenceRepository.findById(id);
    }

    public List<AttestationPresence> findAll() {
        var login = SecurityUtils.getCurrentUserLogin().orElseThrow();
        return attesttionPresenceRepository.findAllByContratStage_Stagiaire_Manager_Login(login);
    }

    public List<AttestationPresence> findAllStagiaire(Long id) {
        return attesttionPresenceRepository.findAllByContratStage_Stagiaire_Id(id);
    }
}
