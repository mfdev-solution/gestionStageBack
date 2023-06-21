package sn.sonatel.mfdev.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.*;
import sn.sonatel.mfdev.config.Constants;
import sn.sonatel.mfdev.domain.Authority;
import sn.sonatel.mfdev.domain.User;

/**
 * A DTO representing a user, with his authorities.
 */
@Data
//@MappedSuperclass
public class AdminUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    @Size(max = 50)
    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    @Email
    @Size(min = 5, max = 254)
    private String email;

    @Size(max = 256)
    @Size(max = 256)
    private String imageUrl;

    private boolean activated = false;

    //    @Size(min = 2, max = 10)
    private String langKey = "FR";

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    @Column(name = "activation_key", length = 20)
    private String matricule;

    private Long phoneNumber;
    private Long structure;

    private Set<String> authorities;

    public AdminUserDTO() {
        // Empty constructor needed for Jackson.
    }

    public AdminUserDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.activated = user.isActivated();
        this.imageUrl = user.getImageUrl();
        this.langKey = user.getLangKey();
        this.createdBy = user.getCreatedBy();
        this.createdDate = user.getCreatedDate();
        this.lastModifiedBy = user.getLastModifiedBy();
        this.lastModifiedDate = user.getLastModifiedDate();
        this.phoneNumber = user.getPhoneNumber();
        this.matricule = user.getMatricule();
        this.structure = user.getStructure().getId();
        this.authorities = user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet());
    }

    public AdminUserDTO(
        Long id,
        @NotBlank @Pattern(regexp = Constants.LOGIN_REGEX) @Size(min = 1, max = 50) String login,
        @Size(max = 50) String firstName,
        @Size(max = 50) String lastName,
        @Email @Size(min = 5, max = 254) String email,
        @Size(max = 256) String imageUrl,
        boolean activated,
        String langKey,
        String createdBy,
        Instant createdDate,
        String lastModifiedBy,
        Instant lastModifiedDate,
        String matricule,
        Long phoneNumber,
        Set<String> authorities
    ) {
        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.imageUrl = imageUrl;
        this.activated = activated;
        this.langKey = langKey;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
        this.matricule = matricule;
        this.phoneNumber = phoneNumber;
        this.authorities = authorities;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof AdminUserDTO;
    }
}
