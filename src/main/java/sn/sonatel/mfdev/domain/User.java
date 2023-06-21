package sn.sonatel.mfdev.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.sonatel.mfdev.config.Constants;

/**
 * A user.
 */

@Getter
@Setter
@Entity(name = "jhi_user")
//@Table
//@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_user")
public class User extends AbstractAuditingEntity<Long> implements Serializable {

    protected static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    protected String login;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password_hash", length = 60, nullable = false)
    protected String password;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    protected String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    protected String lastName;

    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
    protected String email;

    @NotNull
    @Column(nullable = false)
    protected boolean activated = false;

    //    @Size(min = 2, max = 10)
    @Column(name = "lang_key", length = 10)
    protected String langKey = "FR";

    @Size(max = 256)
    @Column(name = "image_url", length = 256)
    protected String imageUrl;

    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    @JsonIgnore
    protected String activationKey;

    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    @JsonIgnore
    protected String resetKey;

    @Column(name = "reset_date")
    protected Instant resetDate = null;

    @Column(name = "matricule", length = 30)
    protected String matricule;

    protected Long phoneNumber;

    @ManyToOne
    @JoinColumn(name = "structure_id")
    protected Structure structure;

    //    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "jhi_user_authority",
        joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") },
        inverseJoinColumns = { @JoinColumn(name = "authority_name", referencedColumnName = "name") }
    )
    @BatchSize(size = 20)
    protected Set<Authority> authorities = new HashSet<>();
    //    public void setLangKey(String key){
    //        System.out.println("langKey setted");
    //        this.langKey = key;
    //    }

}
