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
public class AssignementRequest {

    private Long idStagiaire;
    private Long idManager;
}
