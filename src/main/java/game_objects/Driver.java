package game_objects;

import game_objects.Game;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Driver {

    private final ExecutorService executor;

    public Driver () {
        this.executor = Executors.newCachedThreadPool();
    }

    public void addGame(Game game) {
        this.executor.submit(game);
    }

    public void shutdown() {
        this.executor.shutdown();
    }

}
