package sn.sonatel.mfdev.web.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import sn.sonatel.mfdev.config.ApplicationProperties;
import sn.sonatel.mfdev.domain.ContratStage;
import sn.sonatel.mfdev.repository.ContratStageRepository;
import sn.sonatel.mfdev.service.ContratStageService;
import sn.sonatel.mfdev.service.PdfGenerateServiceImpl;
import sn.sonatel.mfdev.service.StagiaireService;
import sn.sonatel.mfdev.service.helps.DateFormater;

@RestController
@RequestMapping("/api/contrats")
public class ContratStageController {

    private final ContratStageRepository contratStageRepository;
    private final PdfGenerateServiceImpl pdfGenerateService;
    private final ApplicationProperties applicationProperties;
    private final ContratStageService contratStageService;
    private final StagiaireService stagiaireService;

    public ContratStageController(
        ContratStageRepository contratStageRepository,
        PdfGenerateServiceImpl pdfGenerateService,
        ApplicationProperties applicationProperties,
        ContratStageService contratStageService,
        StagiaireService stagiaireService
    ) {
        this.contratStageRepository = contratStageRepository;
        this.pdfGenerateService = pdfGenerateService;
        this.applicationProperties = applicationProperties;
        this.contratStageService = contratStageService;
        this.stagiaireService = stagiaireService;
    }

    @GetMapping
    public ResponseEntity<List<ContratStage>> getAllContratStage() {
        return ResponseEntity.status(HttpStatus.OK).body(contratStageRepository.getAllContratStageManagerNotNull());
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateContract(@RequestBody ContratStage contratStage) {
        Map<String, Object> data = new HashMap<>();
        data.put("intern", contratStage.getStagiaire());
        data.put("structure", contratStage.getStagiaire().getManager().getStructure());
        contratStage.setDateFin(contratStage.getDateFin());
        String dateDbut = DateFormater.formateDate(contratStage.getDateDebut());
        String dateFin = DateFormater.formateDate(contratStage.getDateFin());
        String dateNaissance = DateFormater.formateDate(contratStage.getStagiaire().getDateNaissance());
        data.put("dateDebut", dateDbut);
        data.put("dateFin", dateFin);
        data.put("dateNaissance", dateNaissance);
        data.put("contrat", contratStage);
        String nomFichier = contratStage.getStagiaire().getNom() + "_" + contratStage.getStagiaire().getPrenom() + ".pdf";
        var bool = pdfGenerateService.generatePdfFile("contrat", data, nomFichier);
        contratStageRepository
            .findById(contratStage.getId())
            .map(contratStage1 -> {
                contratStage1.setGenerated(true);
                contratStage1.setUrl(applicationProperties.getDirectoty() + nomFichier);
                return contratStageRepository.save(contratStage1);
            })
            .orElse(null);
        return ResponseEntity.status(HttpStatus.OK).body(bool ? "Le contrat est genere avec succes " : "Une erreure s'est produite");
    }

    @GetMapping("/generate-v1/{id}")
    public ResponseEntity<byte[]> generateContractv1(@PathVariable("id") Long id) {
        ContratStage contratStage = contratStageRepository.findById(id).get();
        Map<String, Object> data = new HashMap<>();
        data.put("intern", contratStage.getStagiaire());
        data.put("structure", contratStage.getStagiaire().getManager().getStructure());
        contratStage.setDateFin(contratStage.getDateFin());
        String dateDbut = DateFormater.formateDate(contratStage.getDateDebut());
        String dateFin = DateFormater.formateDate(contratStage.getDateFin());
        String dateNaissance = DateFormater.formateDate(contratStage.getStagiaire().getDateNaissance());
        data.put("dateDebut", dateDbut);
        data.put("dateFin", dateFin);
        data.put("dateNaissance", dateNaissance);
        data.put("contrat", contratStage);
        byte[] content = pdfGenerateService.returnPdfFile("contrat", data);
        return ResponseEntity.status(HttpStatus.CREATED).body(content);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadContract(@PathVariable("id") Long id) {
        byte[] fiteData = contratStageService.downloadFile(id);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(fiteData);
    }

    @GetMapping("/suivi-demande-interne")
    public ResponseEntity<?> getAllContratStageEnPrositionByGwte() {
        var propositionList = contratStageService.getAllContratStageEnPrositionByGwte();
        if (!propositionList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(propositionList);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> updateContratStage(@PathVariable("id") Long id, @RequestBody ContratStage contratStage) {
        var stagiaire = stagiaireService.updateStagiaire_v1(contratStage.getStagiaire().getId(), contratStage.getStagiaire());
        contratStage.setStagiaire(stagiaire);
        var contrat = contratStageService.updateContratStage(id, contratStage);

        if (contrat != null) return ResponseEntity.status(HttpStatus.CREATED).body(contrat);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
