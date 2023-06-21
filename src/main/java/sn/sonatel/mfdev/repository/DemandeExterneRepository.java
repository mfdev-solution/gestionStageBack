package sn.sonatel.mfdev.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import sn.sonatel.mfdev.domain.DemandeExterne;
import sn.sonatel.mfdev.domain.EtatDemandeInterne;
import sn.sonatel.mfdev.domain.Manager;

public interface DemandeExterneRepository extends JpaRepository<DemandeExterne, Long> {
    Optional<List<DemandeExterne>> findAllByManager(Manager manager);
    Integer countByEtatDemandeInterne(EtatDemandeInterne etatDemandeInterne);
    Integer countByEtatDemandeInterneAndManager(EtatDemandeInterne etatDemandeInterne, Manager manager);
}
