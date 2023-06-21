package sn.sonatel.mfdev.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import sn.sonatel.mfdev.domain.AttestationPresence;

public interface AttestationPresenceRepository extends JpaRepository<AttestationPresence, Long> {
    List<AttestationPresence> findAllByContratStage_Stagiaire_Manager_Login(String login);
    List<AttestationPresence> findAllByContratStage_Stagiaire_Id(Long idStagiaire);
}
