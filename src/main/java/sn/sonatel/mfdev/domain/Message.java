package sn.sonatel.mfdev.domain;

import java.util.Date;
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
@Table
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contenu;
    private Date dateSent;
    private boolean isRead = false;
    private String senderLogin;

    @ManyToOne
    private Manager manager;

    @ManyToOne
    private Gwte gwte;
}
