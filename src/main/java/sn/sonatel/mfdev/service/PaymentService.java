package sn.sonatel.mfdev.service;

import java.util.List;
import org.springframework.stereotype.Service;
import sn.sonatel.mfdev.domain.Gwte;
import sn.sonatel.mfdev.domain.Payement;
import sn.sonatel.mfdev.repository.AttestationPresenceRepository;
import sn.sonatel.mfdev.repository.GwteRepository;
import sn.sonatel.mfdev.repository.PaymentRepository;
import sn.sonatel.mfdev.security.SecurityUtils;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final AttestationPresenceRepository attestationPresenceRepository;
    private final GwteRepository gwteRepository;

    public PaymentService(
        PaymentRepository paymentRepository,
        AttestationPresenceRepository attestationPresenceRepository,
        GwteRepository gwteRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.attestationPresenceRepository = attestationPresenceRepository;
        this.gwteRepository = gwteRepository;
    }

    public Payement addPayement(Payement payement) {
        return paymentRepository.save(payement);
    }

    public List<Payement> getAllPayement() {
        Gwte gwte = gwteRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow();
        return paymentRepository.findAllByAttestationPresence_ContratStage_GwteAndPaied(gwte, false).orElseThrow();
    }
}
