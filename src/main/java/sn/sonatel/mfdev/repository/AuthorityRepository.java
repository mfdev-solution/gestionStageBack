package sn.sonatel.mfdev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.sonatel.mfdev.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
