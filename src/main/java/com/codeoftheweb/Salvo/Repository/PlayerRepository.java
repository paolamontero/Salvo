package com.codeoftheweb.Salvo.Repository;


        import com.codeoftheweb.Salvo.models.Player;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.data.rest.core.annotation.RepositoryRestResource;
        import java.util.List;

@RepositoryRestResource
public interface PlayerRepository extends JpaRepository<Player, Long> {
        List<Player> findByUserName(String userName);
}

