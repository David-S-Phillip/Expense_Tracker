package za.co.gabos.web.routes;

import io.javalin.Javalin;
import za.co.gabos.web.controllers.LoginController;

public class Routes {
    public static final String LOGIN_PAGE = "/login";

    public static void configure(Javalin app) {
        app.get("/", ctx -> ctx.redirect(LOGIN_PAGE));
        app.get(LOGIN_PAGE, LoginController.view);
    }
}
