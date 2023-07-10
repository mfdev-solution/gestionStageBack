package sn.sonatel.mfdev.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import sn.sonatel.mfdev.domain.Gwte;
import sn.sonatel.mfdev.domain.Manager;
import sn.sonatel.mfdev.domain.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<List<Message>> findAllByManager(Manager manager);
    Optional<List<Message>> findAllByGwte(Gwte gwte);
}
