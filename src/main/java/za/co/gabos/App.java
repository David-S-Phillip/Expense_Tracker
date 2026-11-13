package za.co.gabos;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import za.co.gabos.web.routes.Routes;

public class App {
    private final Javalin server;

    public App() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
        this.server = Javalin.create(config -> {
            // 1. Serves files in src/main/resources/public/ at "/"
            config.staticFiles.add("/public");

            // 2. Connects ctx.render(...) to Thymeleaf templates in src/main/resources/templates/
            config.fileRenderer(new JavalinThymeleaf(templateEngine));
        });

        // 3. Registers all URL routes (GET /login, POST /login, etc.)
        Routes.configure(this.server);
    }

    public void start(int port) {
        this.server.start(port);
    }

    public void stop() {
        this.server.stop();
    }

    public static void main(String[] args) {
        App app = new App();
        app.start(5050);
    }
}