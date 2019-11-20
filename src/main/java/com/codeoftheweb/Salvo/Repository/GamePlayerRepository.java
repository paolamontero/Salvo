package com.codeoftheweb.Salvo.Repository;

import com.codeoftheweb.Salvo.models.GamePlayer;
import com.sun.tools.javac.parser.JavacParser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface GamePlayerRepository extends JpaRepository<GamePlayer , Long> {
}
