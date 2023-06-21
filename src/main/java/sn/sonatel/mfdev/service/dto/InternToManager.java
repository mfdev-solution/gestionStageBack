package sn.sonatel.mfdev.service.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InternToManager {

    private Long manager;
    private Long structure;
    private Long remuneration;
    private Date dateDebutStage;
    private Date dateFinStage;
    private String action;
}
