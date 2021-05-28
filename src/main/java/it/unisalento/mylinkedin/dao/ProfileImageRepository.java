package it.unisalento.mylinkedin.dao;

import it.unisalento.mylinkedin.domain.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileImageRepository extends JpaRepository <ProfileImage, Integer>{
}
