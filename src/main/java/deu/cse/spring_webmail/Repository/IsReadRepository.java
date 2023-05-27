/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.Repository;

import deu.cse.spring_webmail.entity.IsRead;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Seongchan
 */
public interface IsReadRepository extends JpaRepository<IsRead, Integer>{
    
}
