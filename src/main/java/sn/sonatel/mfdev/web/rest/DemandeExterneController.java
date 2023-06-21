package sn.sonatel.mfdev.web.rest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.sonatel.mfdev.domain.DemandeExterne;
import sn.sonatel.mfdev.service.DemandeExterneService;

@RestController
@RequestMapping("/api/demande-externe")
public class DemandeExterneController {

    private final DemandeExterneService demandeExterneService;

    @Autowired
    public DemandeExterneController(DemandeExterneService demandeExterneService) {
        this.demandeExterneService = demandeExterneService;
    }

    @GetMapping
    public ResponseEntity<List<DemandeExterne>> getAllDemandeExternes() {
        List<DemandeExterne> demandeExternes = demandeExterneService.getAllDemandesExternes();
        return new ResponseEntity<>(demandeExternes, HttpStatus.OK);
    }

    @GetMapping("/manager")
    public ResponseEntity<?> getAllDemandeExterneByManager() {
        var listDemandeExternes = demandeExterneService.getAllDemandeExterneByManager();
        if (listDemandeExternes != null) {
            return ResponseEntity.status(HttpStatus.OK).body(listDemandeExternes);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DemandeExterne> getDemandeExterneById(@PathVariable Long id) {
        DemandeExterne demandeExterne = demandeExterneService.getDemandeExterneById(id).get();
        return new ResponseEntity<>(demandeExterne, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DemandeExterne> addDemandeExterne(@RequestBody DemandeExterne demandeExterne) {
        DemandeExterne newDemandeExterne = demandeExterneService.createDemandeExterne(demandeExterne);
        return new ResponseEntity<>(newDemandeExterne, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DemandeExterne> updateDemandeExterne(@PathVariable("id") Long id, @RequestBody DemandeExterne demandeExterne) {
        DemandeExterne updatedDemandeExterne = demandeExterneService.updateDemandeExterne(id, demandeExterne);
        return new ResponseEntity<>(updatedDemandeExterne, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDemandeExterne(@PathVariable Long id) {
        demandeExterneService.deleteDemandeExterne(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
