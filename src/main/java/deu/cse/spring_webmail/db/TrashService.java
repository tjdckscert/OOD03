package deu.cse.spring_webmail.db;

import deu.cse.spring_webmail.dto.TrashDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;

@Service
@Slf4j
public class TrashService {

    @Autowired
    private TrashRepository trashRepository;

    public List<TrashDTO> getTrashListByToAddress(String userId) {
        List<Trash> trashList = trashRepository.findByToAddress(userId);
        List<TrashDTO> trashDTOList = new ArrayList<>();
        for (Trash trash : trashList) {
            TrashDTO trashDTO = new TrashDTO(
                    trash.getId(),
                    trash.getMsgId(),
                    trash.getFromAddress(),
                    trash.getSubject(),
                    trash.getSentDate()
            );
            trashDTOList.add(trashDTO);
        }
        return trashDTOList;
    }

    /**
     * 휴지통에서 메일 완전 삭제 수행
     *
     * @param trashId 휴지통 메일 id
     */
    @Transactional
    public void deleteMail(Long trashId) {
        trashRepository.deleteById(trashId);
    }
}
