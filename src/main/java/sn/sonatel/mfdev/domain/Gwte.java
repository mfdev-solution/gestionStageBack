package sn.sonatel.mfdev.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("GWTE")
public class Gwte extends User {

    @OneToMany(mappedBy = "gwte")
    private List<ContratStage> contratStageCollection;

    @OneToMany(mappedBy = "gwte")
    private Collection<Message> messages;
}
