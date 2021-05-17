package it.unisalento.mylinkedin.dao;

import it.unisalento.mylinkedin.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository <Message, Integer>{

    @Query("select m from Message m where  m.conversation = :null and (m.idReceiver =:idUser) or (m.user =:idUser)")
    List<Message> findByUserId(@Param("idUser") int idUser);
}
