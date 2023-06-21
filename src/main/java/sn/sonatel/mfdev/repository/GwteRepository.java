package sn.sonatel.mfdev.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;
import sn.sonatel.mfdev.domain.Gwte;

@Repository
public interface GwteRepository extends JpaRepository<Gwte, Long> {
    List<Gwte> findAll();

    Optional<Gwte> findOneByLogin(String login);

    Optional<Gwte> findOneByEmail(String email);

    Optional<Gwte> findOneById(String email);
}
