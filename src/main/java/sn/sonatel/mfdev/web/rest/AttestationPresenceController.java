package sn.sonatel.mfdev.web.rest;

import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.sonatel.mfdev.domain.AttestationPresence;
import sn.sonatel.mfdev.service.AttestationPresenceService;
import sn.sonatel.mfdev.service.PdfGenerateService;
import sn.sonatel.mfdev.service.PdfGenerateServiceImpl;
import sn.sonatel.mfdev.service.helps.DateFormater;

@RestController
@RequestMapping("/api/attestations")
public class AttestationPresenceController {

    private final AttestationPresenceService attestationPresenceService;
    private final PdfGenerateServiceImpl pdfGenerateService;

    public AttestationPresenceController(AttestationPresenceService attestationPresenceService, PdfGenerateServiceImpl pdfGenerateService) {
        this.attestationPresenceService = attestationPresenceService;
        this.pdfGenerateService = pdfGenerateService;
    }

    @PostMapping
    public ResponseEntity<AttestationPresence> save(@RequestBody AttestationPresence attestationPresence) {
        AttestationPresence savedAttestationPresence = attestationPresenceService.save(attestationPresence);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAttestationPresence);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        attestationPresenceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttestationPresence> findById(@PathVariable Long id) {
        Optional<AttestationPresence> attestationPresence = attestationPresenceService.findById(id);
        return attestationPresence.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AttestationPresence>> findAll() {
        List<AttestationPresence> attestations = attestationPresenceService.findAll();
        return ResponseEntity.ok(attestations);
    }

    @GetMapping("/stagiaire/{id}")
    public ResponseEntity<List<AttestationPresence>> findAllByStagiaireId(@PathVariable Long id) {
        List<AttestationPresence> attestations = attestationPresenceService.findAllStagiaire(id);
        return ResponseEntity.ok(attestations);
    }

    @PostMapping("/download")
    public ResponseEntity<byte[]> getAttestationPresence(@RequestBody AttestationPresence attestationPresence) {
        Map<String, Object> data = new HashMap<>();
        String[] services = attestationPresence.getContratStage().getStagiaire().getManager().getStructure().getNomStructure().split("/");
        data.put(
            "manager",
            attestationPresence.getContratStage().getStagiaire().getManager().getFirstName() +
            " " +
            attestationPresence.getContratStage().getStagiaire().getManager().getLastName()
        );
        data.put("service", services[2]);
        data.put(
            "stagiaire",
            attestationPresence.getContratStage().getStagiaire().getPrenom() +
            " " +
            attestationPresence.getContratStage().getStagiaire().getNom()
        );
        data.put("matricule", attestationPresence.getContratStage().getStagiaire().getMatricule());
        data.put("dateDebut", DateFormater.formateDate(attestationPresence.getDateDebut()));
        data.put("dateFin", DateFormater.formateDate(attestationPresence.getDateFin()));
        data.put("attestation", DateFormater.formateDate(new Date()));
        data.put("direction", services[0]);
        data.put("departement", services[1]);
        String m_mme = attestationPresence.getContratStage().getStagiaire().getGenre().equals("M") ? "M. " : "Mne ";
        data.put("m_mme", m_mme);
        byte[] content = pdfGenerateService.returnPdfFile("attestation_presence", data);

        return ResponseEntity.status(HttpStatus.CREATED).body(content);
    }
}
