package za.co.gabos.web.server;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import za.co.gabos.web.routes.Routes;

public class Server {
    private final Javalin app;

    public Server() {
        this.app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.fileRenderer(new JavalinThymeleaf());
        });
        Routes.configure(this.app);
    }

    public void start(int port) {
        this.app.start(port);
    }

    public void stop() {
        this.app.stop();
    }

    public static void main(String[] args) {
        new Server().start(5050);
    }
}
