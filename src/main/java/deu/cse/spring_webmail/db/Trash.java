package deu.cse.spring_webmail.db;

import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "trash")
public class Trash {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "msg_id")
    private Integer msgId;

    @Column(name = "to_address")
    private String toAddress;

    @Column(name = "from_address")
    private String fromAddress;

    @Column(name = "cc_address")
    private String ccAddress;

    @Column(name = "subject")
    private String subject;

    @Column(name = "sent_date")
    private String sentDate;

    @Column(name = "body")
    private String body;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "download_temp_dir")
    private String downloadTempDir = "C:/temp/download/";

    // 생성자, getter/setter, toString 등 필요한 코드 생략
}
