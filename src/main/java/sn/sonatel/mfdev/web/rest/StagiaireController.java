package sn.sonatel.mfdev.web.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;
import javax.validation.Valid;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sn.sonatel.mfdev.domain.*;
import sn.sonatel.mfdev.repository.ContratStageRepository;
import sn.sonatel.mfdev.repository.GwteRepository;
import sn.sonatel.mfdev.security.SecurityUtils;
import sn.sonatel.mfdev.service.*;
import sn.sonatel.mfdev.service.dto.InternToManager;
import sn.sonatel.mfdev.service.helps.DateFormater;

@RestController
@RequestMapping("/api/stagiaires")
public class StagiaireController {

    private final StagiaireService stagiaireService;
    private final ManagerService managerService;
    private final StructureService structureService;
    private final MailServiceTest mailServiceTest;
    private final ContratStageRepository contratStageRepository;
    private final UserResource userResource;
    private final PdfGenerateServiceImpl pdfGenerateService;
    private final GwteRepository gwteRepository;

    @Autowired
    public StagiaireController(
        StagiaireService stagiaireService,
        ManagerService managerService,
        StructureService structureService,
        MailServiceTest mailServiceTest,
        ContratStageRepository contratStageRepository,
        UserResource userResource,
        PdfGenerateServiceImpl pdfGenerateService,
        GwteRepository gwteRepository
    ) {
        this.stagiaireService = stagiaireService;
        this.managerService = managerService;
        this.structureService = structureService;
        this.mailServiceTest = mailServiceTest;
        this.contratStageRepository = contratStageRepository;
        this.userResource = userResource;
        this.pdfGenerateService = pdfGenerateService;
        this.gwteRepository = gwteRepository;
    }

    @PostMapping
    public ResponseEntity<Stagiaire> createStagiaire(@Valid @RequestBody Stagiaire stagiaire) {
        Stagiaire result = stagiaireService.save(stagiaire);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/stats-etat")
    public ResponseEntity<Map<String, Integer>> getStateEtat() {
        return ResponseEntity.status(HttpStatus.OK).body(stagiaireService.getStatsEtat());
    }

    @GetMapping("/demande-interne-status")
    public ResponseEntity<?> getDemandeInterneStatus() {
        return ResponseEntity.status(HttpStatus.OK).body(stagiaireService.getDemandeInterneStats());
    }

    @GetMapping("/demande-interne-status-manager")
    public ResponseEntity<?> getDemandeInterneManager() {
        return ResponseEntity.status(HttpStatus.OK).body(stagiaireService.getDemandeInterneStatsByManager());
    }

    @Transactional
    @PutMapping("/gwte/{id}")
    public ResponseEntity<ContratStage> updateStagiaire(@Valid @PathVariable("id") Long id, @RequestBody InternToManager internToManager) {
        Manager manager = managerService.getManagerById(internToManager.getManager());
        Structure structure = structureService.getStructureById(internToManager.getStructure()).get();
        Stagiaire stagiaire = new Stagiaire();
        stagiaire.setManager(manager);
        stagiaire.setStructure(structure);
        stagiaire.setEtat(internToManager.getAction().equals("proposer") ? Etat.enProposition : Etat.accepte);
        Stagiaire result = stagiaireService.updateStagiaire(id, stagiaire);
        Gwte gwte = gwteRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow();

        var existedContrat = contratStageRepository.findByStagiaire(result);
        var contrat = new ContratStage();
        if (existedContrat.isPresent()) {
            var newContrat = existedContrat.get();
            newContrat.setDateDebut(internToManager.getDateDebutStage());
            newContrat.setDateFin(internToManager.getDateFinStage());
            newContrat.setRemuneration(internToManager.getRemuneration());
            newContrat.setGwte(gwte);
            contrat = contratStageRepository.save(newContrat);
        } else {
            ContratStage contratStage = new ContratStage();
            contratStage.setStagiaire(result);
            contratStage.setDateDebut(internToManager.getDateDebutStage());
            contratStage.setDateFin(internToManager.getDateFinStage());
            contratStage.setRemuneration(internToManager.getRemuneration());
            contratStage.setGwte(gwte);
            contratStage.setIsSigned(false);
            contrat = contratStageRepository.save(contratStage);
        }

        if (internToManager.getAction().equals("attribuer")) {
            sentMail(result, contrat);
        }
        return ResponseEntity.status(HttpStatus.OK).body(contrat);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stagiaire> updateStagiaire(@Valid @PathVariable("id") Long id, @RequestBody Stagiaire stagiaire) {
        Stagiaire result = stagiaireService.updateStagiaire(id, stagiaire);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("/v1/{id}")
    public ResponseEntity<Stagiaire> updateStagiairev1(@Valid @PathVariable("id") Long id, @RequestBody Stagiaire stagiaire) {
        Stagiaire result = stagiaireService.updateStagiaire_v1(id, stagiaire);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("/mail/{id}")
    public ResponseEntity<Stagiaire> updateStagiaireMail(@Valid @PathVariable("id") Long id, @RequestBody Stagiaire stagiaire)
        throws MessagingException, IOException {
        Stagiaire result = stagiaireService.updateStagiaire(id, stagiaire);
        mailServiceTest.sendmail("amedmaty1999@gmail.com", "cejnmzrrwjpqolce", result.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping
    public ResponseEntity<List<Stagiaire>> getAllStagiaires() {
        List<Stagiaire> result = stagiaireService.findAll();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/etat/{etat}")
    public ResponseEntity<List<Stagiaire>> getAllStagiaresByEtat(@PathVariable("etat") String etat) {
        try {
            Etat state = Etat.valueOf(etat);
            return ResponseEntity.status(HttpStatus.OK).body(stagiaireService.getAllStagiaireByEtat(state));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stagiaire> getStagiaireById(@PathVariable("id") Long id) {
        Stagiaire result = stagiaireService.findOneById(id);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStagiaire(@PathVariable("id") Long id) {
        if (stagiaireService.deleteStagiaire(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/manager/{id}")
    public ResponseEntity<?> getAllStagiairesByManager(@PathVariable("id") Long id) {
        List<Stagiaire> stagiaires = stagiaireService.getAllStagiareByManager(id);

        if (stagiaires != null) {
            return ResponseEntity.status(HttpStatus.OK).body(stagiaires);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/dowload/excel/{etat}")
    public ResponseEntity<?> downloadInternListToExcel(@PathVariable("etat") String etat) throws IOException {
        List<Stagiaire> stagiaireList = stagiaireService.getAllStagiaireByEtat(Etat.valueOf(etat));
        byte[] excelByte = pdfGenerateService.exportToExcel(stagiaireList);
        if (excelByte != null) {
            return ResponseEntity.status(HttpStatus.OK).body(excelByte);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping("/upload/validation-file")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            // Read the Excel file
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getCell(0).getStringCellValue().toLowerCase().equals("nom")) continue;
                Stagiaire stagiaire = stagiaireService.getAStagiaireByCni(row.getCell(2).getStringCellValue());
                Manager manager = managerService.getAManagerByMatricule(row.getCell(25).getStringCellValue());
                stagiaire.setManager(manager);
                stagiaire.setStructure(manager.getStructure());
                stagiaire.setEtat(Etat.accepte);
                Stagiaire updatedStagiaire = stagiaireService.updateStagiaire(stagiaire.getId(), stagiaire);
                Gwte currentGwte = gwteRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow();
                var existedContract = contratStageRepository.findByStagiaire(updatedStagiaire);
                var contrat = new ContratStage();
                if (existedContract.isPresent()) {
                    var newContrat = existedContract.get();
                    newContrat.setDateDebut(row.getCell(17).getDateCellValue());
                    newContrat.setDateFin(row.getCell(18).getDateCellValue());
                    newContrat.setRemuneration((long) row.getCell(19).getNumericCellValue());
                    contrat = contratStageRepository.save(newContrat);
                } else {
                    ContratStage newSontratStage = new ContratStage();
                    newSontratStage.setStagiaire(updatedStagiaire);
                    newSontratStage.setDateDebut(DateFormater.formateExcelDate(row.getCell(17).getDateCellValue()));
                    newSontratStage.setDateFin(DateFormater.formateExcelDate(row.getCell(18).getDateCellValue()));
                    newSontratStage.setRemuneration((long) row.getCell(19).getNumericCellValue());
                    newSontratStage.setGwte(currentGwte);
                    newSontratStage.setIsSigned(false);
                    contrat = contratStageRepository.save(newSontratStage);
                }

                sentMail(updatedStagiaire, contrat);
            }
            workbook.close();
            return ResponseEntity.ok("File uploaded and data imported successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during file upload.");
        }
    }

    private void sentMail(Stagiaire result, ContratStage contrat) {
        Map<String, Object> data = new HashMap<>();
        data.put("intern", result);
        data.put("mois", DateFormater.getMonth(contrat.getDateDebut()));
        data.put("duree", DateFormater.calculDuree(contrat.getDateDebut(), contrat.getDateFin()));
        try {
            mailServiceTest.sendSimpleMail(
                "amedmaty1999@gmail.com",
                "cejnmzrrwjpqolce",
                "ACCORD DE STAGE",
                "accord_stage",
                result.getEmail(),
                data
            );
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
