package se.leovegas.wallet.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.leovegas.wallet.model.Player;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {
}
