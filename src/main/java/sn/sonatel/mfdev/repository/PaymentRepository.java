package sn.sonatel.mfdev.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import sn.sonatel.mfdev.domain.EtatAttestationPresence;
import sn.sonatel.mfdev.domain.Gwte;
import sn.sonatel.mfdev.domain.Payement;

public interface PaymentRepository extends JpaRepository<Payement, Long> {
    Optional<List<Payement>> findAllByAttestationPresence_ContratStage_GwteAndPaied(Gwte gwte, boolean paied);
    Optional<List<Payement>> findAllByAttestationPresence_ContratStage_GwteAndPaiedNot(Gwte gwte, boolean paied);

    Optional<List<Payement>> findAllByAttestationPresence_EtatAttestationPresence(EtatAttestationPresence etatAttestationPresence);
}
