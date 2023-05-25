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
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Seongchan
 */
@Entity
@Data
@NoArgsConstructor
public class Category {
    
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Integer cindex;
    
    @Column
    private String categoryName;
            
    @Column
    private String useUserID;
    
    @Builder
    public Category(Integer cindex, String categoryName, String useUserID) {
        this.cindex = cindex;
        this.categoryName = categoryName;
        this.useUserID = useUserID;
    }
    
    
}
