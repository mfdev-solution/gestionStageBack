package sn.sonatel.mfdev.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.sonatel.mfdev.domain.Message;
import sn.sonatel.mfdev.service.MessageService;
import sn.sonatel.mfdev.service.dto.MessageDto;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/manager")
    public ResponseEntity<?> getAllMessagesByManager() {
        return ResponseEntity.status(HttpStatus.OK).body(messageService.getAllMessagesByManager());
    }

    @GetMapping("/gwte")
    public ResponseEntity<?> getAllMessagesByGwte() {
        return ResponseEntity.status(HttpStatus.OK).body(messageService.getAllMessageByGwte());
    }

    @PostMapping
    public ResponseEntity<?> addMessage(@RequestBody Message message) {
        return ResponseEntity.status(HttpStatus.OK).body(messageService.addAMessage(message));
    }

    @PostMapping("/modication-atp-message")
    public ResponseEntity<?> modifierAttestationPresenegeAvecMessage(@RequestBody MessageDto messageDto) {
        System.out.println(messageDto.getAttestationPresence());
        var message = messageService.messageModificationAttestationPresenceGwte(messageDto);
        if (message != null) {
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
