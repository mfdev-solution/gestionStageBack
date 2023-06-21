package sn.sonatel.mfdev.service;

import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sn.sonatel.mfdev.domain.Manager;
import sn.sonatel.mfdev.domain.Structure;
import sn.sonatel.mfdev.domain.User;
import sn.sonatel.mfdev.repository.AuthorityRepository;
import sn.sonatel.mfdev.repository.ManagerRepostory;
import sn.sonatel.mfdev.repository.StructureRepository;
import sn.sonatel.mfdev.repository.UserRepository;
import sn.sonatel.mfdev.service.dto.AdminUserDTO;
import sn.sonatel.mfdev.service.mapper.UserMapper;

@Service
public class ManagerService {

    private final UserRepository userrepository;
    private final AuthorityRepository authorityRepository;
    private final UserService userService;
    private final UserMapper userMapper;
    private final StructureService structureService;
    private final PasswordEncoder passwordEncoder;
    private final ManagerRepostory managerRepostory;
    private final StructureRepository structureRepository;

    public ManagerService(
        UserRepository userrepository,
        AuthorityRepository authorityRepository,
        UserService userService,
        UserMapper userMapper,
        StructureService structureService,
        PasswordEncoder passwordEncoder,
        ManagerRepostory managerRepostory,
        StructureRepository structureRepository
    ) {
        this.userrepository = userrepository;
        this.authorityRepository = authorityRepository;
        this.userService = userService;
        this.userMapper = userMapper;
        this.structureService = structureService;
        this.passwordEncoder = passwordEncoder;
        this.managerRepostory = managerRepostory;
        this.structureRepository = structureRepository;
    }

    /**
     * recuperer tous la liste de tous les mangers
     * @return List of Managers
     */
    public List<Manager> getAllManagers() {
        return managerRepostory.findAll();
    }

    /**
     * ajouter un manager dans la base de donnees
     * @param managerDTO
     * @return a Manager
     */
    public Manager saveManager(AdminUserDTO managerDTO) {
        Manager manager = new Manager();
        Structure structure = structureService.getStructureById(4L).get();
        //        manager.setPassword(passwordEncoder.encode(manager.getPassword()));

        manager.setStructure(structure);
        return (Manager) userService.createUser(managerDTO, manager);
    }

    /**
     * rechercher un manager par identifiant
     * @param id
     * @return a manager
     */
    public Manager getManagerById(Long id) {
        //        Optional<User> managerOptional = userrepository.findById(id);
        return managerRepostory.findById(id).orElse(null);
    }

    /**
     * Rechercher un admin par son email
     * @param email
     * @return Manager
     */
    public User getManagerByEmail(String email) {
        //        Optional<User> managerOptional = userrepository.findById(id);
        return managerRepostory.findOneByLogin(email).orElse(null);
    }

    /**
     * Rechercher un admin par son login
     * @param login
     * @return Manager
     */
    public Optional<User> getManagerByLogin(String login) {
        //        Optional<User> managerOptional = userrepository.findOneByLogin(login);
        var manager = managerRepostory.findOneWithAuthoritiesByLogin(login).orElse(null);

        //        System.out.println(manager.getAuthorities());
        return Optional.of(manager);
    }

    /**
     * suprimer un manager par son login
     * @param login
     */
    public void deleteManager(String login) {
        managerRepostory
            .findOneByLogin(login)
            .ifPresent(manager -> {
                managerRepostory.delete((Manager) manager);
            });
    }

    /**
     * modifier un manager :
     * @param adminUserDTO
     * @return
     */
    public Manager updateManger(AdminUserDTO adminUserDTO) {
        var updatedUser = userService.updateUser(adminUserDTO);
        return updatedUser.map(userDTO -> userMapper.userDTOToManager(userDTO, new Manager())).orElse(null);
    }

    public List<Manager> getAllManagersByStructure(Long id) {
        Structure structure = (structureRepository.findById(id)).orElse(null);
        return managerRepostory.getAllByStructure(structure);
    }

    public Manager getAManagerByMatricule(String matricule) {
        return managerRepostory.findByMatricule(matricule).orElse(null);
    }
    /*
    public List<Manager> getAllManagers() {
        return userrepository.findAll();
    }

    public void deleteManager(Long id) {
        userrepository.deleteById(id);
    }

    public void assignStagiaireToManager(Long managerId, Stagiaire stagiaire) {
        Optional<Manager> managerOptional = userrepository.findById(managerId);
        if (managerOptional.isPresent()) {
            Manager manager = managerOptional.get();
            List<Stagiaire> stagiaireList = manager.getStagiaireCollection();
            stagiaireList.add(stagiaire);
            manager.setStagiaireCollection(stagiaireList);
            userrepository.save(manager);
        }
    }*/
}
