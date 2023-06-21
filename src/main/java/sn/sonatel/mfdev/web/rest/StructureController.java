package sn.sonatel.mfdev.web.rest;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import sn.sonatel.mfdev.domain.Structure;
import sn.sonatel.mfdev.service.StructureService;

@RestController
@RequestMapping("/api/structure")
public class StructureController {

    private final StructureService structureService;

    @Autowired
    public StructureController(StructureService structureService) {
        this.structureService = structureService;
    }

    @GetMapping
    public ResponseEntity<List<Structure>> getAllStructures() {
        List<Structure> structures = structureService.getAllStructures();
        return new ResponseEntity<>(structures, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Structure> getStructureById(@PathVariable("id") Long id) {
        Optional<Structure> structure = structureService.getStructureById(id);
        return structure
            .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Structure> createStructure(@RequestBody Structure structure) {
        structure.setIsActivated(true);
        Structure savedStructure = structureService.saveStructure(structure);

        return new ResponseEntity<>(savedStructure, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Structure> updateStructure(@PathVariable("id") Long id, @RequestBody Structure structure) {
        //
        Structure updatedStructure = structureService.updateStructure(id, structure);
        if (updatedStructure != null) return new ResponseEntity<>(updatedStructure, HttpStatus.OK);
        //
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        //
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStructure(@PathVariable("id") Long id) {
        Optional<Structure> existingStructure = structureService.getStructureById(id);
        if (existingStructure.isPresent()) {
            if (structureService.deleteStructureById(id)) return new ResponseEntity<>(
                HttpStatus.NO_CONTENT
            ); else throw new UsernameNotFoundException("L'utilisateur n'existe pas ");
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
