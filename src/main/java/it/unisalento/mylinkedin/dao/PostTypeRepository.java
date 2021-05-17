package it.unisalento.mylinkedin.dao;

import it.unisalento.mylinkedin.domain.PostType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTypeRepository extends JpaRepository<PostType, Integer> {

    PostType findByType(String type);
}
