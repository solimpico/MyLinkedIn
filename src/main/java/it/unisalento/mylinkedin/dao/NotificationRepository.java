package it.unisalento.mylinkedin.dao;

import it.unisalento.mylinkedin.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository <Notification, Integer>{

    @Query("select n from Notification n where n.user.id = :userId")
    List<Notification> getNotificationByUserId(@Param("userId") int userId);

    @Transactional
    @Modifying
    @Query("delete from Notification n where n.user.id = :userId")
    void deleteAllNotificationByUserId(@Param("userId") int userId);
}