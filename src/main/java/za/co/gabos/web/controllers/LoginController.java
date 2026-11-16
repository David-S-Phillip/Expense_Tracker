package za.co.gabos.web.controllers;
import io.javalin.http.Handler;
import za.co.gabos.persistance.userPersistance.UserDAO;
import za.co.gabos.persistance.userPersistance.UserDAODb;
import io.javalin.http.Context;
import za.co.gabos.user.User;

import java.util.Map;
import java.util.Optional;

public class LoginController {
    private final UserDAO userDAO;

    public LoginController(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    public void view(Context ctx) {
        ctx.render("login.html", Map.of());
    }

    public void login(Context ctx){
        String email = ctx.formParam("email");
        Optional<User> user = userDAO.findByEmail(email);

        if (user.isEmpty()){
            ctx.render("login.html", Map.of("error", "User not found"));
            return;
        }

        ctx.sessionAttribute("user", user.get());
        ctx.redirect("/expense");
    }


}
