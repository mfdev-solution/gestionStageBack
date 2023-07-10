package sn.sonatel.mfdev.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sn.sonatel.mfdev.domain.AttestationPresence;
import sn.sonatel.mfdev.domain.Message;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    private String contenu;
    private AttestationPresence attestationPresence;
}
