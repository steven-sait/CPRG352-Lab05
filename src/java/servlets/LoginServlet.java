package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import services.AccountService;
import models.User;

public class LoginServlet extends HttpServlet {
    private final AccountService ACCOUNT_SERVICE = new AccountService();

    private void displayMessage(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
        request.setAttribute("message", message);
        getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("logout") != null) {
            // invalidate session, display logout message
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("username") != null) {
                session.invalidate();
                displayMessage(request, response, "You've successfully logged out");
            }
        }

        getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // validate user pass not empty
        String username = request.getParameter("username"),
                password = request.getParameter("password");

        if (username == null || password == null || username.equals("") || password.equals("")) {
            displayMessage(request, response, "Invalid login credentials");
            return;
        }

        // pass user pass to login() AccountService class
        User user = ACCOUNT_SERVICE.Login(username, password);
        // if auth params invalid -> display error, keep text filled with previous creds, forward to login.jsp
        if (user == null) {
            request.setAttribute("username", username);
            request.setAttribute("password", password);
            displayMessage(request, response, "Invalid login credentials");
            return;
        }

        // login() return non-null val -> store user in sess var, redirect to /home
        HttpSession session = request.getSession();
        session.setAttribute("username", username);
        
        response.sendRedirect("home");
    }
}
