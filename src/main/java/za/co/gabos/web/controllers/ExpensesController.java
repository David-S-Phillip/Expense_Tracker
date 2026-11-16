package za.co.gabos.web.controllers;

import io.javalin.http.Context;
import za.co.gabos.user.User;

import java.util.Map;

public class ExpensesController {
    public void view(Context ctx) {
        User currentUser = ctx.sessionAttribute("user");

        if (currentUser == null) {
            ctx.redirect("/login");
            return;
        }

        ctx.render("expense.html", Map.of("email", currentUser.getEmail()));
    }




}
