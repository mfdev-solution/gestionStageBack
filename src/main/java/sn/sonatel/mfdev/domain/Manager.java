package sn.sonatel.mfdev.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sn.sonatel.mfdev.config.Constants;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("MANAGER")
public class Manager extends User {

    //    private String stagiare;
    @OneToMany(mappedBy = "manager")
    private Collection<Stagiaire> stagiaireCollection;

    public Manager(
        Long id,
        @NotNull @Pattern(regexp = Constants.LOGIN_REGEX) @Size(min = 1, max = 50) String login,
        @NotNull @Size(min = 60, max = 60) String password,
        @Size(max = 50) String firstName,
        @Size(max = 50) String lastName,
        @Email @Size(min = 5, max = 254) String email,
        @NotNull boolean activated,
        String langKey,
        @Size(max = 256) String imageUrl,
        @Size(max = 20) String activationKey,
        @Size(max = 20) String resetKey,
        Instant resetDate,
        String matricule,
        Long phoneNumber,
        Structure structure,
        Set<Authority> authorities
    ) {
        super(
            id,
            login,
            password,
            firstName,
            lastName,
            email,
            activated,
            langKey,
            imageUrl,
            activationKey,
            resetKey,
            resetDate,
            matricule,
            phoneNumber,
            structure,
            authorities
        );
    }

    @OneToMany(mappedBy = "manager")
    private Collection<DemandeExterne> demandeExterneCollection;
}
