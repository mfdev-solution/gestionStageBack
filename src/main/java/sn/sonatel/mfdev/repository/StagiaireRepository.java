package sn.sonatel.mfdev.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sn.sonatel.mfdev.domain.Etat;
import sn.sonatel.mfdev.domain.Manager;
import sn.sonatel.mfdev.domain.Stagiaire;

public interface StagiaireRepository extends JpaRepository<Stagiaire, Long> {
    Optional<Stagiaire> findByEmail(String email);

    @Query("SELECT s FROM Stagiaire s WHERE s.etat = :etat")
    List<Stagiaire> findAllByEtatOrderByIdDesc(@Param("etat") Etat etat);

    @Query("SELECT s FROM Stagiaire s WHERE s.etat = :etat and s.matricule != null")
    List<Stagiaire> findAllByEtatAndMatriculeOrderByIdDesc(@Param("etat") Etat etat);

    Integer countByEtat(Etat etat);
    Optional<List<Stagiaire>> findAllByManager(Manager manager);
    Optional<List<Stagiaire>> findAllByEtatAndManager(Etat etat, Manager manager);
    Optional<Stagiaire> findOneByCni(String cni);
}
