package br.com.fiap.BrainUpQuiz.repository;

import br.com.fiap.BrainUpQuiz.model.Player;
import br.com.fiap.BrainUpQuiz.model.Quiz;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class QuizRepository {

    private final Quiz quiz = new Quiz();
    private final Map<UUID, Player> players = new ConcurrentHashMap<>();

    public Quiz getQuiz() {
        return quiz;
    }

    public Player addPlayer(String name) {
        Player player = new Player(UUID.randomUUID(), name, true);
        quiz.getPlayers().add(player);
        players.put(player.getId(), player);
        return player;
    }

    public Player removePlayer(UUID id) {
        Player player = players.get(id);
        if (player != null) {
            player.setActive(false);
        }
        return player;
    }
}
