package sn.sonatel.mfdev.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.sonatel.mfdev.domain.DemandeExterne;
import sn.sonatel.mfdev.domain.Manager;
import sn.sonatel.mfdev.repository.DemandeExterneRepository;
import sn.sonatel.mfdev.repository.ManagerRepostory;
import sn.sonatel.mfdev.security.SecurityUtils;

@Service
public class DemandeExterneService {

    private final DemandeExterneRepository demandeExterneRepository;
    private final ManagerRepostory managerRepostory;

    @Autowired
    public DemandeExterneService(DemandeExterneRepository demandeExterneRepository, ManagerRepostory managerRepostory) {
        this.demandeExterneRepository = demandeExterneRepository;
        this.managerRepostory = managerRepostory;
    }

    public List<DemandeExterne> getAllDemandesExternes() {
        return demandeExterneRepository.findAll();
    }

    public Optional<DemandeExterne> getDemandeExterneById(Long id) {
        return demandeExterneRepository.findById(id);
    }

    public DemandeExterne createDemandeExterne(DemandeExterne demandeExterne) {
        Manager manager = managerRepostory.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow();
        demandeExterne.setManager(manager);
        return demandeExterneRepository.save(demandeExterne);
    }

    public DemandeExterne updateDemandeExterne(DemandeExterne demandeExterne) {
        return demandeExterneRepository.save(demandeExterne);
    }

    public DemandeExterne updateDemandeExterne(Long id, DemandeExterne updatedDemandeExterne) {
        Optional<DemandeExterne> existingDemandeExterne = demandeExterneRepository.findById(id);
        if (existingDemandeExterne.isPresent()) {
            DemandeExterne demandeExterne = existingDemandeExterne.get();
            // Update the fields of demandeExterne with the values from updatedDemandeExterne
            demandeExterne.setDateDebut(updatedDemandeExterne.getDateDebut());
            demandeExterne.setDateFin(updatedDemandeExterne.getDateFin());
            demandeExterne.setProfile(updatedDemandeExterne.getProfile());
            demandeExterne.setNombreStagiaire(updatedDemandeExterne.getNombreStagiaire());
            demandeExterne.setManager(updatedDemandeExterne.getManager());
            demandeExterne.setEtatDemandeInterne(updatedDemandeExterne.getEtatDemandeInterne());
            return demandeExterneRepository.save(demandeExterne);
        } else {
            // Handle case when the entity is not found
            // You can throw an exception or return null/throw a custom exception based on your requirement
            return null;
        }
    }

    public List<DemandeExterne> getAllDemandeExterneByManager() {
        //        var login = SecurityUtils.getCurrentUserLogin().orElseThrow();
        var manager = managerRepostory.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow();
        return demandeExterneRepository.findAllByManager(manager).orElseThrow();
    }

    public void deleteDemandeExterne(Long id) {
        demandeExterneRepository.deleteById(id);
    }
}
