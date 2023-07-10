package sn.sonatel.mfdev.service;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import sn.sonatel.mfdev.domain.EtatAttestationPresence;
import sn.sonatel.mfdev.domain.Gwte;
import sn.sonatel.mfdev.domain.Message;
import sn.sonatel.mfdev.domain.Payement;
import sn.sonatel.mfdev.repository.AttestationPresenceRepository;
import sn.sonatel.mfdev.repository.GwteRepository;
import sn.sonatel.mfdev.repository.PaymentRepository;
import sn.sonatel.mfdev.security.SecurityUtils;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final AttestationPresenceRepository attestationPresenceRepository;
    private final AttestationPresenceService attestationPresenceService;
    private final GwteRepository gwteRepository;

    public PaymentService(
        PaymentRepository paymentRepository,
        AttestationPresenceRepository attestationPresenceRepository,
        AttestationPresenceService attestationPresenceService,
        GwteRepository gwteRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.attestationPresenceRepository = attestationPresenceRepository;
        this.attestationPresenceService = attestationPresenceService;
        this.gwteRepository = gwteRepository;
    }

    public Payement addPayement(Payement payement) {
        return paymentRepository.save(payement);
    }

    public List<Payement> getAllPayement() {
        Gwte gwte = gwteRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow();
        return paymentRepository.findAllByAttestationPresence_ContratStage_GwteAndPaied(gwte, false).orElseThrow();
    }

    public List<Payement> getAllPayementByEtat(String etat) {
        return paymentRepository.findAllByAttestationPresence_EtatAttestationPresence(EtatAttestationPresence.valueOf(etat)).orElseThrow();
    }

    public Optional<Payement> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    @Transactional
    public Payement updatePayment(Long id, Payement payement) {
        System.out.println(payement.getAttestationPresence().getEtatAttestationPresence() + " " + id);
        var updatedAttestation = attestationPresenceService.updateAttestation(
            payement.getAttestationPresence().getId(),
            payement.getAttestationPresence()
        );
        return paymentRepository
            .findById(id)
            .map(payement1 -> {
                payement1.setPaied(payement.isPaied());
                payement1.setAttestationPresence(updatedAttestation);
                payement1.setDatePaye(payement.getDatePaye());
                return paymentRepository.save(payement1);
            })
            .orElseThrow();
    }
    //    @Transactional
    //    public Payement rejeterAttestationPresence(Payement payement){
    //        var updatedAttestation = attestationPresenceService.updateAttestation(payement.getAttestationPresence().getId(),payement.getAttestationPresence());
    //        Message message = new Message();
    //        message.setGwte(updatedAttestation.getContratStage().getGwte());
    //        message.setManager(updatedAttestation.getContratStage().getStagiaire().getManager());
    //
    //    }
}
