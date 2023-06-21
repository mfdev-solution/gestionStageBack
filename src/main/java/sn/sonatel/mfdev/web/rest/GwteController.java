package sn.sonatel.mfdev.web.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.sonatel.mfdev.config.ApplicationProperties;
import sn.sonatel.mfdev.domain.Gwte;
import sn.sonatel.mfdev.domain.Manager;
import sn.sonatel.mfdev.domain.Stagiaire;
import sn.sonatel.mfdev.repository.StagiaireRepository;
import sn.sonatel.mfdev.repository.UserRepository;
import sn.sonatel.mfdev.security.AuthoritiesConstants;
import sn.sonatel.mfdev.service.GwteService;
import sn.sonatel.mfdev.service.MailServiceTest;
import sn.sonatel.mfdev.service.PdfGenerateServiceImpl;
import sn.sonatel.mfdev.service.UserService;
import sn.sonatel.mfdev.service.dto.AdminUserDTO;
import sn.sonatel.mfdev.service.dto.AssignementRequest;

@RestController
@RequestMapping("/api/gwtes")
public class GwteController {

    private final StagiaireRepository stagiaireRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final GwteService gwteService;
    private final PdfGenerateServiceImpl pdfGenerateService;
    private final MailServiceTest mailServiceTest;

    public GwteController(
        StagiaireRepository stagiaireRepository,
        UserRepository userRepository,
        UserService userService,
        GwteService gwteService,
        MailServiceTest mailServiceTest,
        PdfGenerateServiceImpl pdfGenerateService,
        MailServiceTest mailServiceTest1
    ) {
        this.stagiaireRepository = stagiaireRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.gwteService = gwteService;
        this.pdfGenerateService = pdfGenerateService;
        this.mailServiceTest = mailServiceTest1;
    }

    @PostMapping("/asign-intern-manager")
    public ResponseEntity<Stagiaire> assignerStagiaireManager(@RequestBody AssignementRequest assignementRequest) {
        Manager manager = (Manager) userRepository
            .findById(assignementRequest.getIdManager())
            .orElseThrow(() -> new EntityNotFoundException("Manager with id " + assignementRequest.getIdManager() + " not found"));
        Stagiaire stagiaire = stagiaireRepository
            .findById(assignementRequest.getIdStagiaire())
            .orElseThrow(() -> new EntityNotFoundException("Manager with id " + assignementRequest.getIdStagiaire() + " not found"));

        return ResponseEntity.ok(gwteService.assignerStagiareManager(stagiaire, manager));
    }

    @PostMapping("/contrat")
    public String genererContrat(@RequestBody Stagiaire stagiaire) {
        Map<String, Object> data = new HashMap<>();
        data.put("intern", stagiaire);
        var bool = pdfGenerateService.generatePdfFile("contrat", data, stagiaire.getNom() + "_" + stagiaire.getPrenom() + ".pdf");
        return bool ? "File successfuly created " : "Failled creating file";
    }

    @GetMapping("/send-mail")
    public String sendMail() throws MessagingException, IOException {
        Map<String, Object> data = new HashMap<>();
        Stagiaire stagiaire = new Stagiaire();
        stagiaire.setTypeStage("pedagogique");
        stagiaire.setPrenom("Mouhamed");
        data.put("intern", stagiaire);
        //        ,"gestion.stage.sonatel1@gmail.com""vhbnjsmfqidmczbh"
        try {
            mailServiceTest.sendSimpleMail(
                "amedmaty1999@gmail.com",
                "cejnmzrrwjpqolce",
                "ACCORD DE STAGE",
                "accord_stage",
                "lasolution.mfdev@gmail.com",
                data
            );

            return "email envoye avec succes";
        } catch (Exception exception) {
            exception.getMessage();
            return "email non envoye";
        }
    }

    @GetMapping
    public ResponseEntity<List<Gwte>> getAllGwtes() {
        return ResponseEntity.status(HttpStatus.OK).body(gwteService.getAllGwtes());
    }

    @GetMapping("/{login}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.GWTE + "\")")
    public ResponseEntity<AdminUserDTO> getGwte(@PathVariable("login") String login) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserWithAuthoritiesByLogin(login).map(AdminUserDTO::new).get());
    }
}
