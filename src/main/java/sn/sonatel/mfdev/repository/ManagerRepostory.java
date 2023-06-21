package sn.sonatel.mfdev.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.sonatel.mfdev.domain.Manager;
import sn.sonatel.mfdev.domain.Structure;
import sn.sonatel.mfdev.domain.User;

@Repository
public interface ManagerRepostory extends JpaRepository<Manager, Long> {
    Optional<Manager> findOneByLogin(String login);

    Optional<Object> findByEmail(String email);

    Optional<User> findOneWithAuthoritiesByLogin(String login);
    List<Manager> getAllByStructure(Structure structure);
    Optional<Manager> findByMatricule(String matricule);
}
