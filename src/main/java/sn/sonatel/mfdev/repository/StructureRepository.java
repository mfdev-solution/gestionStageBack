package sn.sonatel.mfdev.repository;

import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.sonatel.mfdev.domain.Structure;

@Repository
public interface StructureRepository extends JpaRepository<Structure, Long> {
    @Override
    Optional<Structure> findById(Long aLong);

    Optional<Structure> findByEmail(String email);
    Optional<Structure> findByNomStructure(String nomStructure);
}
