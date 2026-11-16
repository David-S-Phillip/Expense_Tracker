package za.co.gabos.web.routes;

import io.javalin.Javalin;
import za.co.gabos.persistance.Database;
import za.co.gabos.persistance.userPersistance.UserDAO;
import za.co.gabos.persistance.userPersistance.UserDAODb;
import za.co.gabos.web.controllers.ExpensesController;
import za.co.gabos.web.controllers.LoginController;

public class Routes {
    public static final String LOGIN_PAGE = "/login";
    public static final String EXPENSES_PAGE = "/expense";

    public static void configure(Javalin app, Database db){
        UserDAO userDao = new UserDAODb(db);
        LoginController loginController = new LoginController(userDao);
        ExpensesController expensesController = new ExpensesController();

        app.get("/", ctx -> ctx.redirect(LOGIN_PAGE));
        app.get(LOGIN_PAGE, loginController::view);
        app.post(LOGIN_PAGE, loginController::login);

        app.get(EXPENSES_PAGE, expensesController::view);
    }

    // 1. Existing callers (like old tests or App.java) still work untouched:
    public static void configure(Javalin app) {
        configure(app, new Database("jdbc:sqlite:expense_tracker.db"));
    }

//    public static void configure(Javalin app) {
//        app.get("/", ctx -> ctx.redirect(LOGIN_PAGE));
//        app.get(LOGIN_PAGE, loginController.view());
//        app.post(LOGIN_PAGE, loginController.login());
//    }
}
