package deu.cse.spring_webmail.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrashRepository extends JpaRepository<Trash, Long> {

    List<Trash> findByToAddress(String toAddress);
}
