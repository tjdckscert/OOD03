/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.Repository;

import deu.cse.spring_webmail.entity.Category;
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
public interface CategoryRepository extends JpaRepository<Category, Integer>{
//    @Query("select c from Category c where useUserID = :userid")
    public List<Category> findByUseUserID(String useUserID);
    
    @Query("select c.categoryName from Category c where useUserID = :userid")
    public List<String> findUserCategory(@Param("userid")String useUserID);
}
