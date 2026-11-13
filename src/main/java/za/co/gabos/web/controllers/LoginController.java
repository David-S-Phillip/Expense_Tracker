package za.co.gabos.web.controllers;
import io.javalin.http.Handler;
import java.util.Map;

public class LoginController {

    public static final Handler view = ctx -> {
        ctx.render("login.html", Map.of());
    };


}
