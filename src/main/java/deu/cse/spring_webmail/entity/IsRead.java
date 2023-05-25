/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Seongchan
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class IsRead {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Integer readindex;
    
    @Column(nullable = false, columnDefinition = "longtext")
    private String messageBody;
    
    @Column
    private String sender;

    @Column
    private String reciver;

    @Column
    private Boolean isRead;
    
    @Builder
    public IsRead(Integer readindex, String messageBody, String sender, String reciver, Boolean isRead) {
        this.readindex = readindex;
        this.messageBody = messageBody;
        this.sender = sender;
        this.reciver = reciver;
        this.isRead = isRead;
    }

}
