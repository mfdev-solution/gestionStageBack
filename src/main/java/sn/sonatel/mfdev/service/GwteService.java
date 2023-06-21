package sn.sonatel.mfdev.service;

import java.util.List;
import org.springframework.stereotype.Service;
import sn.sonatel.mfdev.domain.*;
import sn.sonatel.mfdev.repository.GwteRepository;
import sn.sonatel.mfdev.service.dto.AdminUserDTO;
import sn.sonatel.mfdev.service.mapper.UserMapper;

@Service
public class GwteService {

    private final StructureService structureService;
    private final UserService userService;

    private final StagiaireService stagiaireService;
    private final UserMapper userMapper;
    private final GwteRepository gwteRepository;

    public GwteService(
        StructureService structureService,
        UserService userService,
        StagiaireService stagiaireService,
        UserMapper userMapper,
        GwteRepository gwteRepository
    ) {
        this.structureService = structureService;
        this.userService = userService;
        this.stagiaireService = stagiaireService;
        this.userMapper = userMapper;
        this.gwteRepository = gwteRepository;
    }

    /**
     * assigner un stagiaire a un manager
     * @param stagiaire
     * @param manager
     * @return
     */
    public Stagiaire assignerStagiareManager(Stagiaire stagiaire, Manager manager) {
        stagiaire.setManager(manager);
        stagiaire.setEtat(Etat.enProposition);
        stagiaire.setStructure(manager.getStructure());
        return stagiaireService.updateStagiaire(stagiaire.getId(), stagiaire);
    }

    /**
     * valider le contrat de stage d'un etudiant
     * @param stagiaire
     * @return Stagiaire
     */
    public Stagiaire accorderStage(Stagiaire stagiaire) {
        stagiaire.setEtat(Etat.accepte);
        return stagiaireService.updateStagiaire(stagiaire.getId(), stagiaire);
    }

    /**
     *
     * @return la liste de tous les GWTEs
     */
    public List<Gwte> getAllGwtes() {
        return gwteRepository.findAll();
    }

    /**
     * rechercher un Gwte par son Login
     * @param login
     * @return Gwte
     */
    public Gwte getOneGwteByLogin(String login) {
        return gwteRepository.findOneByLogin(login).orElse(null);
    }

    /**
     * rechercher un Gwte par son Email
     * @param email
     * @return Gwte
     */
    public Gwte getOneGwteByEmail(String email) {
        return gwteRepository.findOneByEmail(email).orElse(null);
    }

    /**
     * rechercher un Gwte par son Email
     * @param email
     * @return Gwte
     */
    public Gwte getOneGwteById(String email) {
        return gwteRepository.findOneById(email).orElse(null);
    }

    /**
     * ajouter un GWTE
     * @param gwteDTO
     * @return GWTE
     */
    public Gwte saveGwte(AdminUserDTO gwteDTO) {
        Gwte gwte = new Gwte();
        Structure structure = structureService.getStructureById(3L).get();
        //        gwte.setPassword(passwordEncoder.encode(gwte.getPassword()));
        gwte.setStructure(structure);
        return (Gwte) userService.createUser(gwteDTO, gwte);
    }

    /**
     * Modifier les information d'un Gwte
     * @param adminUserDTO
     * @return Gwte
     */
    public Gwte updateGwte(AdminUserDTO adminUserDTO) {
        //        var updatedUser =  userService.updateUser(adminUserDTO);

        return userService.updateUser(adminUserDTO).map(userDTO -> userMapper.userDTOToGwte(userDTO, new Gwte())).orElse(null);
    }

    /**
     * Suprimer completement un Gwte dans la base de donnees
     * @param login
     */
    public void deleteGwte(String login) {
        gwteRepository
            .findOneByLogin(login)
            .ifPresent(gwte -> {
                gwteRepository.delete(gwte);
            });
    }
}
