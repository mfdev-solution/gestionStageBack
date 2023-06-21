package sn.sonatel.mfdev.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import sn.sonatel.mfdev.domain.ContratStage;
import sn.sonatel.mfdev.domain.Etat;
import sn.sonatel.mfdev.domain.Gwte;
import sn.sonatel.mfdev.repository.ContratStageRepository;
import sn.sonatel.mfdev.repository.GwteRepository;
import sn.sonatel.mfdev.security.SecurityUtils;

@Service
public class ContratStageService {

    public final ContratStageRepository contratStageRepository;
    public final GwteRepository gwteRepository;

    public ContratStageService(ContratStageRepository contratStageRepository, GwteRepository gwteRepository) {
        this.contratStageRepository = contratStageRepository;
        this.gwteRepository = gwteRepository;
    }

    public byte[] downloadFile(Long idCrontrat) {
        ContratStage contratStage = contratStageRepository.findById(idCrontrat).orElse(null);
        if (contratStage != null) {
            try {
                return Files.readAllBytes(new File(contratStage.getUrl()).toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    public List<ContratStage> getAllContratStageEnPrositionByGwte() {
        Gwte gwte = gwteRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow();
        return contratStageRepository.findAllByGwteAndStagiaire_Etat(gwte, Etat.enProposition).orElseThrow();
    }

    public ContratStage updateContratStage(Long id, ContratStage contratStage) {
        return contratStageRepository
            .findById(id)
            .map(contratStage2 -> {
                contratStage2.setGwte(contratStage.getGwte());
                contratStage2.setUrl(contratStage.getUrl());
                contratStage2.setRemuneration(contratStage.getRemuneration());
                contratStage2.setDateFin(contratStage.getDateFin());
                contratStage2.setGenerated(contratStage.getGenerated());
                contratStage2.setNote(contratStage.getNote());
                contratStage2.setLibele(contratStage.getLibele());
                contratStage2.setDateDebut(contratStage.getDateDebut());
                contratStage2.setIsSigned(contratStage.getIsSigned());
                contratStage2.setIsActivated(contratStage.getIsActivated());
                contratStage2.setAttestationPresences(contratStage.getAttestationPresences());
                if (contratStage.getStagiaire().getManager() == null) contratStage.getStagiaire().setStructure(null);
                contratStage2.setStagiaire(contratStage.getStagiaire());

                return contratStageRepository.save(contratStage2);
            })
            .orElseThrow(() -> new EntityNotFoundException("Contrat stage dont id " + id + " non trouve"));
    }
}
