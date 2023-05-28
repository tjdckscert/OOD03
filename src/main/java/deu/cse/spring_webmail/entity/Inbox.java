/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.entity;

import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Seongchan
 */
@Entity
@Data
@NoArgsConstructor
public class Inbox {
    @Id
    private Integer mindex;
    
    @Column(nullable = false, updatable = false, length = 100)
    private String repositoryName;
    
    @Column(nullable = false, length = 200)
    private String messageName;

    @Column(nullable = false, length = 30)
    private String messageState;

    @Column(length = 200)
    private String errorMessage;

    @Column
    private String sender;

    @Column(nullable = false, columnDefinition = "longtext")
    private String recipients;

    @Column(nullable = false)
    private String remoteHost;

    @Column(nullable = false, length = 20)
    private String remoteAddr;

    @Column(nullable = false, columnDefinition = "longtext")
    private String messageBody;

    @Column(columnDefinition = "longtext")
    private String messageAttributes;

    @Column(nullable = false)
    private OffsetDateTime lastUpdated;
    
    @Column(nullable = false)
    private Boolean isRead;
}