/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.Repository;

import deu.cse.spring_webmail.entity.Inbox;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Seongchan
 */
@Repository
public interface InboxRepository extends JpaRepository<Inbox, Integer> {
//    @Query("select c from Category c where useUserID = :useriextends JpaRepository<Inbox, Integer>d")
//    public Optional<List<Inbox>> getMails(@Param("userid")String userid);
    @Query("SELECT i FROM Inbox i WHERE i.messageBody LIKE %:title%")
    public List<Inbox> searchByMessageBodyLike(@Param("title")String messageBody);
    
    public List<Inbox> findByRecipientsOrderByLastUpdated(String recipients);
    
//    public Page<Inbox> findByRecipients(String recipients, Pageable pageable);
    
    public List<Inbox>findBySenderOrderByLastUpdated (String sender);
    
    public List<Inbox> findBySenderAndLastUpdated(String sender, String lastUpdated);
}
