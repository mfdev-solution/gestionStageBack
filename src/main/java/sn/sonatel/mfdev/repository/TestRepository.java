package sn.sonatel.mfdev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.sonatel.mfdev.domain.Test;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {}
