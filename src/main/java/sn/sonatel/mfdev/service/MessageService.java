package sn.sonatel.mfdev.service;

import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import sn.sonatel.mfdev.domain.Gwte;
import sn.sonatel.mfdev.domain.Manager;
import sn.sonatel.mfdev.domain.Message;
import sn.sonatel.mfdev.repository.GwteRepository;
import sn.sonatel.mfdev.repository.ManagerRepostory;
import sn.sonatel.mfdev.repository.MessageRepository;
import sn.sonatel.mfdev.security.SecurityUtils;
import sn.sonatel.mfdev.service.dto.MessageDto;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final GwteRepository gwteRepository;
    private final ManagerRepostory managerRepostory;
    private final AttestationPresenceService attestationPresenceService;

    public MessageService(
        MessageRepository messageRepository,
        GwteRepository gwteRepository,
        ManagerRepostory managerRepostory,
        AttestationPresenceService attestationPresenceService
    ) {
        this.messageRepository = messageRepository;
        this.gwteRepository = gwteRepository;
        this.managerRepostory = managerRepostory;
        this.attestationPresenceService = attestationPresenceService;
    }

    public Message addAMessage(Message message) {
        message.setSenderLogin(SecurityUtils.getCurrentUserLogin().orElseThrow());
        message.setRead(false);
        message.setDateSent(new Date());
        return messageRepository.save(message);
    }

    public List<Message> getAllMessagesByManager() {
        Manager manager = managerRepostory.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow();

        return messageRepository.findAllByManager(manager).orElseThrow();
    }

    public List<Message> getAllMessageByGwte() {
        Gwte gwte = gwteRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow();
        return messageRepository.findAllByGwte(gwte).orElseThrow();
    }

    public Message updateMessage(Long id, Message message) {
        return messageRepository
            .findById(id)
            .map(message1 -> {
                message1.setGwte(message.getGwte());
                message1.setManager(message.getManager());
                message1.setRead(message.isRead());
                message1.setContenu(message.getContenu());
                message1.setDateSent(message.getDateSent());
                return messageRepository.save(message1);
            })
            .orElseThrow();
    }

    @Transactional
    public Message messageModificationAttestationPresenceGwte(MessageDto messageDto) {
        var attestationPresence = attestationPresenceService.updateAttestation(
            messageDto.getAttestationPresence().getId(),
            messageDto.getAttestationPresence()
        );
        Message newMessage = new Message();
        newMessage.setManager(attestationPresence.getContratStage().getStagiaire().getManager());
        newMessage.setGwte(attestationPresence.getContratStage().getGwte());
        newMessage.setContenu(messageDto.getContenu());
        return this.addAMessage(newMessage);
    }
}
