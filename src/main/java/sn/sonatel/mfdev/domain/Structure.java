package sn.sonatel.mfdev.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "structure")
public class Structure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String nomStructure;
    private String adresse;
    private String email;
    private Integer telephone;

    @Column(name = "isActivated", columnDefinition = "boolean default true")
    private Boolean isActivated;

    @JsonIgnore
    @OneToMany(mappedBy = "structure")
    private List<Stagiaire> stagiaireCollection = new ArrayList<>();

    @OneToMany(mappedBy = "structure")
    private List<User> userList = new ArrayList<>();
}
