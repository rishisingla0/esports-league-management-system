package com.esports.api.service;

import com.esports.api.entity.Game;
import com.esports.api.repository.GameRepository;
import org.springframework.stereotype.Service;
import com.esports.api.dto.CreateGameRequest;
import java.util.Locale;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Optional<Game> getGameById(Long id) {
        return gameRepository.findById(id);
    }

    public Game createGame(CreateGameRequest request) {
        Game game = new Game();

        game.setGameName(request.gameName().trim());
        game.setGenre(request.genre().trim());
        game.setDeveloper(request.developer().trim());
        game.setReleaseDate(request.releaseDate());

        return gameRepository.save(game);
    }

}
