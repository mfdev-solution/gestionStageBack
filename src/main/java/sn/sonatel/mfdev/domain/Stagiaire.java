package sn.sonatel.mfdev.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stagiaire")
public class Stagiaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String nom;
    private String prenom;
    private String email;
    private Integer numeroTelephone;
    private Date dateNaissance;
    private String lieuNaissance;
    private String cni;
    private String adresse;
    private String nationalite;
    private String formationEnCours;
    private String ecole;
    private String matricule;
    private String niveauEtude;
    private String diplomeObtenu;
    private Integer numeroTelUrgence;
    private String situationMatrimonial;
    private String genre;
    private String typeStage;

    //    private Date dateDebutSatage;
    //    private Date dateFinStage;
    //    private boolean contractAviable;
    @Enumerated(EnumType.STRING)
    private Etat etat = Etat.enCours;

    private Boolean isActivated = true;

    @ManyToOne
    private Manager manager;

    @JsonIgnore
    @ManyToOne
    private Structure structure;

    @OneToMany(mappedBy = "stagiaire")
    private List<ContratStage> contratStageList; //contratStage
}
