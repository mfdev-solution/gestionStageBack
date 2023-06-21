package sn.sonatel.mfdev.service;

import java.util.List;
import java.util.Optional;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.mfdev.domain.Structure;
import sn.sonatel.mfdev.repository.StructureRepository;
import sn.sonatel.mfdev.service.Exceptions.StructureAlreadyUsed;

//@NoArgsConstructor
@Transactional
@Service
public class StructureService {

    private final StructureRepository structureRepository;

    public StructureService(StructureRepository structureRepository) {
        this.structureRepository = structureRepository;
    }

    /**
     *
     * @return
     */
    public List<Structure> getAllStructures() {
        return structureRepository.findAll();
    }

    /**
     *
     * @param id
     * @return
     */
    public Optional<Structure> getStructureById(Long id) {
        return structureRepository.findById(id);
    }

    /**
     *
     * @param structure
     * @return
     */
    public Structure saveStructure(Structure structure) {
        structureRepository
            .findByEmail(structure.getEmail().toLowerCase())
            .ifPresent(existedStructure -> {
                boolean removed = removeNonActivatedStructure(existedStructure);
                if (!removed) {
                    throw new StructureAlreadyUsed("Un utilisateur utilisant cet email exste deja");
                }
            });
        structureRepository
            .findByNomStructure(structure.getNomStructure())
            .ifPresent(existinsgStructure -> {
                boolean removed = removeNonActivatedStructure(existinsgStructure);
                if (!removed) {
                    throw new StructureAlreadyUsed("Un utilisateur ayant ce nom d'utilisteur exste deja");
                }
            });
        return structureRepository.save(structure);
    }

    /**
     *
     * @param id
     * @param structure
     * @return
     */
    public Structure updateStructure(Long id, Structure structure) {
        var structureOptional = structureRepository.findById(id);
        if (structureOptional.isPresent()) {
            var structure1 = structureOptional.get();
            structure1.setTelephone(structure.getTelephone() != null ? structure.getTelephone() : structure1.getTelephone());
            structure1.setNomStructure(structure.getNomStructure() != null ? structure.getNomStructure() : structure1.getNomStructure());
            structure1.setAdresse(structure.getAdresse() != null ? structure.getAdresse() : structure1.getAdresse());
            structure1.setEmail(structure.getEmail() != null ? structure.getEmail() : structure1.getEmail());
            structure1.setIsActivated(structure.getIsActivated() != null ? structure.getIsActivated() : structure1.getIsActivated());
            return structureRepository.save(structure1);
        }
        return null;
    }

    /**
     *
     * @param id
     * @return
     */
    public boolean deleteStructureById(Long id) {
        var structureOptional = structureRepository.findById(id);
        if (structureOptional.isPresent()) {
            structureRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     *
     * @param existedStructure
     * @return
     */
    private boolean removeNonActivatedStructure(Structure existedStructure) {
        if (existedStructure.getIsActivated()) {
            return false;
        }
        structureRepository.delete(existedStructure);
        structureRepository.flush();
        //        this.clearUserCaches(existedStructure);
        return true;
    }
}
