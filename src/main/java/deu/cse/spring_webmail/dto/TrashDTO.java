package deu.cse.spring_webmail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrashDTO {

    private Long id;
    private Integer msgId;
    private String sender; //보낸사람
    private String subject;
    private String sentDate;
}
