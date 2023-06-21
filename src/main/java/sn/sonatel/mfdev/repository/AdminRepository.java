package sn.sonatel.mfdev.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import sn.sonatel.mfdev.domain.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    @Override
    Optional<Admin> findById(Long aLong);

    Optional<Admin> findOneByEmail(String email);

    Optional<Admin> findOneByLogin(String login);
}
