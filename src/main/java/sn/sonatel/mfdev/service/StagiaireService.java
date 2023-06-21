package sn.sonatel.mfdev.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.mfdev.domain.Etat;
import sn.sonatel.mfdev.domain.EtatDemandeInterne;
import sn.sonatel.mfdev.domain.Manager;
import sn.sonatel.mfdev.domain.Stagiaire;
import sn.sonatel.mfdev.repository.DemandeExterneRepository;
import sn.sonatel.mfdev.repository.ManagerRepostory;
import sn.sonatel.mfdev.repository.StagiaireRepository;
import sn.sonatel.mfdev.security.SecurityUtils;

@Transactional
@Service
public class StagiaireService {

    private final StagiaireRepository stagiaireRepository;
    private final ManagerRepostory managerRepostory;
    private final DemandeExterneRepository demandeExterneRepository;

    public StagiaireService(
        StagiaireRepository stagiaireRepository,
        ManagerRepostory managerRepostory,
        DemandeExterneRepository demandeExterneRepository
    ) {
        this.stagiaireRepository = stagiaireRepository;
        this.managerRepostory = managerRepostory;
        this.demandeExterneRepository = demandeExterneRepository;
    }

    public Stagiaire save(@Valid Stagiaire stagiaire) {
        //        stagiaireRepository.findById(stagiaire.getId())
        //            .ifPresent(stagiaire1 -> {
        //                var existedIntern =  removeNonActivatedStagiaire(stagiaire1);
        //                if (!existedIntern){
        //                    throw new InternAlreadyUsed("Le stagiaire exste deja");
        //
        //                }
        //            });
        //        stagiaireRepository.findByEmail(stagiaire.getEmail())
        //            .ifPresent(stagiaire1 -> {
        //                var existedIntern =  removeNonActivatedStagiaire(stagiaire1);
        //                if (!existedIntern){
        //                    throw new InternAlreadyUsed("Un stagiaire ayant cette adresse email exste deja");
        //                }
        //            });

        return stagiaireRepository.save(stagiaire);
    }

    public Stagiaire findOneById(Long id) {
        return stagiaireRepository.findById(id).orElse(null);
    }

    public Stagiaire findOneByEmail(String email) {
        return stagiaireRepository.findByEmail(email).orElse(null);
    }

    public List<Stagiaire> findAll() {
        return stagiaireRepository.findAll();
    }

    public boolean deleteStagiaire(Long id) {
        var stagiaireOptional = stagiaireRepository.findById(id);
        if (stagiaireOptional.isPresent()) {
            stagiaireRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Stagiaire updateStagiaire(Long id, Stagiaire stagiaire) {
        return stagiaireRepository
            .findById(id)
            .map(stagiaire1 -> {
                stagiaire1.setNom(stagiaire.getNom() != null ? stagiaire.getNom() : stagiaire1.getNom());
                stagiaire1.setPrenom(stagiaire.getPrenom() != null ? stagiaire.getPrenom() : stagiaire1.getPrenom());
                stagiaire1.setEmail(stagiaire.getEmail() != null ? stagiaire.getEmail() : stagiaire1.getEmail());
                stagiaire1.setNumeroTelephone(
                    stagiaire.getNumeroTelephone() != null ? stagiaire.getNumeroTelephone() : stagiaire1.getNumeroTelephone()
                );
                stagiaire1.setDateNaissance(
                    stagiaire.getDateNaissance() != null ? stagiaire.getDateNaissance() : stagiaire1.getDateNaissance()
                );
                stagiaire1.setLieuNaissance(
                    stagiaire.getLieuNaissance() != null ? stagiaire.getLieuNaissance() : stagiaire1.getLieuNaissance()
                );
                stagiaire1.setCni(stagiaire.getCni() != null ? stagiaire.getCni() : stagiaire1.getCni());
                stagiaire1.setAdresse(stagiaire.getAdresse() != null ? stagiaire.getAdresse() : stagiaire1.getAdresse());
                stagiaire1.setNationalite(stagiaire.getNationalite() != null ? stagiaire.getNationalite() : stagiaire1.getNationalite());
                stagiaire1.setFormationEnCours(
                    stagiaire.getFormationEnCours() != null ? stagiaire.getFormationEnCours() : stagiaire1.getFormationEnCours()
                );
                stagiaire1.setEcole(stagiaire.getEcole() != null ? stagiaire.getEcole() : stagiaire1.getEcole());
                stagiaire1.setMatricule(stagiaire.getMatricule() != null ? stagiaire.getMatricule() : stagiaire1.getMatricule());
                stagiaire1.setNiveauEtude(stagiaire.getNiveauEtude() != null ? stagiaire.getNiveauEtude() : stagiaire1.getNiveauEtude());
                stagiaire1.setDiplomeObtenu(
                    stagiaire.getDiplomeObtenu() != null ? stagiaire.getDiplomeObtenu() : stagiaire1.getDiplomeObtenu()
                );
                stagiaire1.setNumeroTelUrgence(
                    stagiaire.getNumeroTelUrgence() != null ? stagiaire.getNumeroTelUrgence() : stagiaire1.getNumeroTelUrgence()
                );
                stagiaire1.setSituationMatrimonial(
                    stagiaire.getSituationMatrimonial() != null ? stagiaire.getSituationMatrimonial() : stagiaire1.getSituationMatrimonial()
                );
                stagiaire1.setEtat(stagiaire.getEtat() != null ? stagiaire.getEtat() : stagiaire1.getEtat());
                stagiaire1.setIsActivated(stagiaire.getIsActivated() != null ? stagiaire.getIsActivated() : stagiaire1.getIsActivated());
                stagiaire1.setManager(stagiaire.getManager() != null ? stagiaire.getManager() : stagiaire1.getManager());
                stagiaire1.setStructure(stagiaire.getStructure() != null ? stagiaire.getStructure() : stagiaire1.getStructure());
                stagiaire1.setTypeStage(stagiaire.getTypeStage() != null ? stagiaire.getTypeStage() : stagiaire1.getTypeStage());
                //                stagiaire1.setDateFinStage(stagiaire.getDateFinStage()!=null?stagiaire.getDateFinStage():stagiaire1.getDateFinStage());
                //                stagiaire1.setDateDebutSatage(stagiaire.getDateDebutSatage()!=null?stagiaire.getDateDebutSatage():stagiaire1.getDateDebutSatage());
                return stagiaireRepository.save(stagiaire1);
            })
            .orElseThrow(() -> new EntityNotFoundException("Stagiaire with id " + id + " not found"));
    }

    public Stagiaire updateStagiaire_v1(Long id, Stagiaire stagiaire) {
        return stagiaireRepository
            .findById(id)
            .map(stagiaire1 -> {
                stagiaire1.setNom(stagiaire.getNom());
                stagiaire1.setPrenom(stagiaire.getPrenom());
                stagiaire1.setEmail(stagiaire.getEmail());
                stagiaire1.setNumeroTelephone(stagiaire.getNumeroTelephone());
                stagiaire1.setDateNaissance(stagiaire.getDateNaissance());
                stagiaire1.setLieuNaissance(stagiaire.getLieuNaissance());
                stagiaire1.setCni(stagiaire.getCni());
                stagiaire1.setAdresse(stagiaire.getAdresse());
                stagiaire1.setNationalite(stagiaire.getNationalite());
                stagiaire1.setFormationEnCours(stagiaire.getFormationEnCours());
                stagiaire1.setEcole(stagiaire.getEcole());
                stagiaire1.setMatricule(stagiaire.getMatricule());
                stagiaire1.setNiveauEtude(stagiaire.getNiveauEtude());
                stagiaire1.setDiplomeObtenu(stagiaire.getDiplomeObtenu());
                stagiaire1.setNumeroTelUrgence(stagiaire.getNumeroTelUrgence());
                stagiaire1.setSituationMatrimonial(stagiaire.getSituationMatrimonial());
                stagiaire1.setEtat(stagiaire.getEtat());
                stagiaire1.setIsActivated(stagiaire.getIsActivated());
                stagiaire1.setManager(stagiaire.getManager());
                stagiaire1.setStructure(stagiaire.getStructure());
                stagiaire1.setTypeStage(stagiaire.getTypeStage());
                return stagiaireRepository.save(stagiaire1);
            })
            .orElseThrow(() -> new EntityNotFoundException("Stagiaire with id " + id + " not found"));
    }

    private boolean removeNonActivatedStagiaire(Stagiaire existedStagiaire) {
        if (existedStagiaire.getIsActivated()) {
            return false;
        }
        stagiaireRepository.delete(existedStagiaire);
        stagiaireRepository.flush();
        //        this.clearUserCaches(existedStructure);
        return true;
    }

    public List<Stagiaire> getAllStagiaireByEtat(Etat etat) {
        return stagiaireRepository.findAllByEtatOrderByIdDesc(etat);
    }

    public List<Stagiaire> getAllStagiaireByEtat(Etat etat, String matricule) {
        return stagiaireRepository.findAllByEtatAndMatriculeOrderByIdDesc(etat);
    }

    public Map<String, Integer> getStatsEtat() {
        Map<String, Integer> statsEtat = new HashMap<>();
        statsEtat.put("enCours", stagiaireRepository.countByEtat(Etat.enCours));
        statsEtat.put("accepte", stagiaireRepository.countByEtat(Etat.accepte));
        statsEtat.put("rejete", stagiaireRepository.countByEtat(Etat.rejete));
        statsEtat.put("enProposition", stagiaireRepository.countByEtat(Etat.enProposition));
        statsEtat.put("complet", stagiaireRepository.countByEtat(Etat.complet));
        return statsEtat;
    }

    public Map<String, Integer> getDemandeInterneStats() {
        Map<String, Integer> stringIntegerMap = new HashMap<>();
        stringIntegerMap.put("enCours", demandeExterneRepository.countByEtatDemandeInterne(EtatDemandeInterne.enCours));
        stringIntegerMap.put("acceptee", demandeExterneRepository.countByEtatDemandeInterne(EtatDemandeInterne.acceptee));
        stringIntegerMap.put("rejetee", demandeExterneRepository.countByEtatDemandeInterne(EtatDemandeInterne.rejetee));
        return stringIntegerMap;
    }

    public Map<String, Integer> getDemandeInterneStatsByManager() {
        Manager manager = managerRepostory.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow();
        Map<String, Integer> stringIntegerMap = new HashMap<>();
        stringIntegerMap.put("enCours", demandeExterneRepository.countByEtatDemandeInterneAndManager(EtatDemandeInterne.enCours, manager));
        stringIntegerMap.put(
            "acceptee",
            demandeExterneRepository.countByEtatDemandeInterneAndManager(EtatDemandeInterne.acceptee, manager)
        );
        stringIntegerMap.put("rejetee", demandeExterneRepository.countByEtatDemandeInterneAndManager(EtatDemandeInterne.rejetee, manager));

        return stringIntegerMap;
    }

    public List<Stagiaire> getAllStagiaireByStateAcceptByManager(Long id) {
        Manager manager = managerRepostory.findById(id).orElse(null);
        if (manager != null) {
            return stagiaireRepository.findAllByEtatAndManager(Etat.accepte, manager).orElseThrow();
        }
        return null;
    }

    public List<Stagiaire> getAllStagiaireByEtatEnPropostionAndManager(Long idManager) {
        Manager manager = managerRepostory.findById(idManager).orElse(null);
        if (manager != null) {
            return stagiaireRepository.findAllByEtatAndManager(Etat.enProposition, manager).orElse(null);
        }
        return null;
    }

    public List<Stagiaire> getAllStagiareByManager(Long id) {
        Manager manager = managerRepostory.findById(id).orElse(null);
        if (manager != null) return stagiaireRepository.findAllByManager(manager).orElse(null);
        return null;
    }

    public Stagiaire getAStagiaireByCni(String cni) {
        return stagiaireRepository.findOneByCni(cni).orElse(null);
    }
}
