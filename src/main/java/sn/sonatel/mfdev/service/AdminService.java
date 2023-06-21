package sn.sonatel.mfdev.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import sn.sonatel.mfdev.domain.Admin;
import sn.sonatel.mfdev.domain.Structure;
import sn.sonatel.mfdev.repository.AdminRepository;
import sn.sonatel.mfdev.service.dto.AdminUserDTO;
import sn.sonatel.mfdev.service.mapper.UserMapper;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final StructureService structureService;
    private final UserService userService;
    private final UserMapper userMapper;

    public AdminService(
        AdminRepository adminRepository,
        StructureService structureService,
        UserService userService,
        UserMapper userMapper
    ) {
        this.adminRepository = adminRepository;
        this.structureService = structureService;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /**
     *
     * @return tous les administrateurs
     */
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    /**
     * creer un administrateur apartir de la base de donnees
     * @param adminDTO
     * @return Admin
     */
    public Admin saveAdmin(AdminUserDTO adminDTO) {
        Admin admin = new Admin();

        Structure structure = structureService.getStructureById(3L).get();
        //        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        admin.setStructure(structure);
        return (Admin) userService.createUser(adminDTO, admin);
    }

    /**
     * Rechercher un admin par son email
     * @param email
     * @return Admin
     */
    public Admin getAnAdminByEmail(String email) {
        return adminRepository.findOneByEmail(email).orElse(null);
    }

    /**
     * Rechercher un admin par son login
     * @param login
     * @return Admin
     */
    public Admin getAnAdminByLogin(String login) {
        return adminRepository.findOneByLogin(login).orElse(null);
    }

    /**
     * Rechercher un admin par son identifiant
     *
     * @param id
     * @return Admin
     */
    public Admin getAnAdmin(Long id) {
        return adminRepository.findById(id).orElse(null);
    }

    /**
     * modifier les informations d'un administrateur
     * @param adminUserDTO
     * @return
     */
    public Admin updateAdmin(AdminUserDTO adminUserDTO) {
        //        UserMapper userMapper;
        return userService.updateUser(adminUserDTO).map(userDTO -> userMapper.userDTOToAdmin(userDTO, new Admin())).orElse(null);
    }

    /**
     * suprimer completement un administrateur dans la base de donnees
     * @param login
     */

    public void deleteAdmin(String login) {
        adminRepository
            .findOneByLogin(login)
            .ifPresent(admin -> {
                adminRepository.delete(admin);
            });
    }
}
