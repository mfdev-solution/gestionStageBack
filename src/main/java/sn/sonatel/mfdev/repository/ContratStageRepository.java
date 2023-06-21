package sn.sonatel.mfdev.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sn.sonatel.mfdev.domain.ContratStage;
import sn.sonatel.mfdev.domain.Etat;
import sn.sonatel.mfdev.domain.Gwte;
import sn.sonatel.mfdev.domain.Stagiaire;

@Repository
public interface ContratStageRepository extends JpaRepository<ContratStage, Long> {
    @Query("SELECT s FROM ContratStage s WHERE s.stagiaire.manager!=null ")
    List<ContratStage> getAllContratStageManagerNotNull();

    Optional<ContratStage> findByStagiaire(Stagiaire stagiaire);

    Optional<List<ContratStage>> findAllByGwteAndStagiaire_Etat(Gwte gwte, Etat etat);
}
