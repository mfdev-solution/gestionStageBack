package sn.sonatel.mfdev.config;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sn.sonatel.mfdev.domain.Authority;
import sn.sonatel.mfdev.domain.User;
import sn.sonatel.mfdev.repository.AuthorityRepository;
import sn.sonatel.mfdev.repository.UserRepository;
import sn.sonatel.mfdev.security.AuthoritiesConstants;
import sn.sonatel.mfdev.service.MailService;
import sn.sonatel.mfdev.service.mapper.UserMapper;
import tech.jhipster.security.RandomUtil;

@Component
@Slf4j
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final ApplicationProperties applicationProperties;

    private final UserRepository userRepository;

    private final MailService emailService;

    private final AuthorityRepository authorityRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    private static final List<String> ALL_AUTHORITIES = Arrays.asList(
        AuthoritiesConstants.ADMIN,
        AuthoritiesConstants.USER,
        AuthoritiesConstants.ANONYMOUS,
        AuthoritiesConstants.MANAGER,
        AuthoritiesConstants.GWTE
    );

    public InitialDataLoader(
        ApplicationProperties applicationProperties,
        UserRepository userRepository,
        MailService emailService,
        AuthorityRepository authorityRepository,
        PasswordEncoder passwordEncoder,
        UserMapper userMapper
    ) {
        this.applicationProperties = applicationProperties;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Initialisation des données");

        List<Authority> authorities = authorityRepository.findAll();

        for (String aut : ALL_AUTHORITIES) {
            if (authorities.stream().noneMatch(a -> a.getName().equals(aut))) {
                Authority authority = authorityRepository.save(new Authority(aut));
                authorities.add(authority);
            }
        }

        String email = applicationProperties.getAdminEmail().toLowerCase();
        String login = applicationProperties.getAdminLogin().toLowerCase();
        //        String password = applicationProperties.getAdminPassword();

        if (userRepository.findOneByLogin(login).isEmpty()) {
            try {
                User u = new User();
                u.setEmail(email);
                u.setLogin(login);
                u.setLangKey("fr");
                u.setActivated(true);
                u.setResetDate(Instant.now());
                String encryptedPassword = passwordEncoder.encode("passer");
                u.setPassword(encryptedPassword);
                u.setCreatedBy(Constants.SYSTEM);
                u.setAuthorities(authorities.stream().filter(a -> a.getName().equals("ROLE_ADMIN")).collect(Collectors.toSet()));
                u.setFirstName("admin");
                u.setLastName("admin");

                u.setResetKey(RandomUtil.generateActivationKey());
                u = userRepository.save(u);
                emailService.sendCreationEmail(u);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
}
