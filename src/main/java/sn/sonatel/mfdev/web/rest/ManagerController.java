package sn.sonatel.mfdev.web.rest;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.sonatel.mfdev.domain.*;
import sn.sonatel.mfdev.repository.ContratStageRepository;
import sn.sonatel.mfdev.repository.PaymentRepository;
import sn.sonatel.mfdev.repository.UserRepository;
import sn.sonatel.mfdev.security.AuthoritiesConstants;
import sn.sonatel.mfdev.service.AttestationPresenceService;
import sn.sonatel.mfdev.service.ManagerService;
import sn.sonatel.mfdev.service.StagiaireService;
import sn.sonatel.mfdev.service.UserService;
import sn.sonatel.mfdev.service.dto.AdminUserDTO;
import sn.sonatel.mfdev.service.dto.AttestationPresenceDto;

@RestController
@RequestMapping("/api/managers")
public class ManagerController {

    private final ManagerService managerService;
    private final StagiaireService stagiaireService;

    private final UserRepository userRepository;
    private final UserService userService;
    private final AttestationPresenceService attestationPresenceService;
    private final ContratStageRepository contratStageRepository;
    private final PaymentRepository paymentRepository;

    public ManagerController(
        ManagerService managerService,
        StagiaireService stagiaireService,
        UserRepository userRepository,
        UserService userService,
        AttestationPresenceService attestationPresenceService,
        ContratStageRepository contratStageRepository,
        PaymentRepository paymentRepository
    ) {
        this.managerService = managerService;
        this.stagiaireService = stagiaireService;
        this.userRepository = userRepository;
        this.userService = userService;
        this.attestationPresenceService = attestationPresenceService;
        this.contratStageRepository = contratStageRepository;
        this.paymentRepository = paymentRepository;
    }

    @GetMapping
    public ResponseEntity<List<Manager>> getAllManagers() {
        return ResponseEntity.status(HttpStatus.OK).body(managerService.getAllManagers());
    }

    @GetMapping("/{login}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MANAGER + "\")")
    public ResponseEntity<AdminUserDTO> getManager(@PathVariable("login") String login) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserWithAuthoritiesByLogin(login).map(AdminUserDTO::new).get());
    }

    @GetMapping("/structure/{id}")
    public ResponseEntity<List<Manager>> getAllManagersByStrucure(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(managerService.getAllManagersByStructure(id));
    }

    @GetMapping("/stagiaires-list/{id}")
    public ResponseEntity<?> getAllStagiaires(@PathVariable("id") Long id) {
        var statagiairesList = stagiaireService.getAllStagiaireByStateAcceptByManager(id);
        if (statagiairesList != null) {
            return ResponseEntity.status(HttpStatus.OK).body(statagiairesList);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/proposition/{id}")
    public ResponseEntity<?> getAllStagiaireByManagerAndState(@PathVariable("id") Long idManager) {
        List<Stagiaire> stagiaireList = stagiaireService.getAllStagiaireByEtatEnPropostionAndManager(idManager);
        if (stagiaireList != null) {
            return ResponseEntity.status(HttpStatus.OK).body(stagiaireList);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping("/attestation-presence")
    public ResponseEntity<AttestationPresence> createAttestationPresence(@RequestBody AttestationPresenceDto attestationPresenceDto) {
        //        System.out.println(attestationPresenceDto.getStagiaire());
        ContratStage contratStage = contratStageRepository.findByStagiaire(attestationPresenceDto.getStagiaire()).orElse(null);
        if (contratStage != null) {
            AttestationPresence attestationPresence = new AttestationPresence();
            attestationPresence.setContratStage(contratStage);
            attestationPresence.setDateDebut(attestationPresenceDto.getDateDebut());
            attestationPresence.setDateFin(attestationPresenceDto.getDateFin());
            var result = attestationPresenceService.save(attestationPresence);
            if (result != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(result);
            }
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("/attestation-presence")
    public ResponseEntity<?> getAllAttestationPresence() {
        return ResponseEntity.status(HttpStatus.OK).body(attestationPresenceService.findAll());
    }

    @GetMapping("/attestation-presence/{idStagire}")
    public ResponseEntity<?> getAllAttestationPresenceByStagire(@PathVariable("idStagire") Long idStagiaire) {
        var listAttestations = attestationPresenceService.findAllStagiaire(idStagiaire);
        return ResponseEntity.status(HttpStatus.OK).body(listAttestations);
    }
}
