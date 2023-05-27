/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.model;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.Getter;


/**
 *
 * @author sooye
 */

@Getter
@Setter
@Slf4j
public class AddressAgent {
    
    private Long id;
    private String username;  // userID
    private String email;  // otherID
    private String name;  // otherID 이름 설정
    private String phone;  // 폰
    
    

    public AddressAgent(String username, String email, String name, String phone) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.phone = phone;
    }
    
}
