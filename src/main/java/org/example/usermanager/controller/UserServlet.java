package org.example.usermanager.controller;

import org.example.usermanager.DAO.IUserDAO;
import org.example.usermanager.DAO.UserDAO;
import org.example.usermanager.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "UserServlet", urlPatterns = "/users")
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IUserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }
        try {
            switch (action) {
                case "create":
                    insertUser(req, resp);
                    break;
                case "edit":
                    updateUser(req, resp);
                    break;
            }
        } catch (SQLException ex){
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null){
            action = "";
        }
        try {
            switch (action){
                case "create":
                    showNewForm(req, resp);
                    break;
                case "edit":
                    showEditForm(req, resp);
                    break;
                case "delete":
                    deleteUser(req, resp);
                    break;
                default:
                    listUser(req, resp);
                    break;
            }
        } catch (SQLException ex){
            throw new ServletException(ex);
        }
    }

    private void listUser(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {
        List<User> listUser = userDAO.selectAllUsers();
        req.setAttribute("listUser", listUser);
        RequestDispatcher dispatcher = req.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(req, resp);
    }

    private void showNewForm(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("user/create.jsp");
        requestDispatcher.forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        User existingUser = userDAO.selectUser(id);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("user/edit.jsp");
        req.setAttribute("user", existingUser);
        requestDispatcher.forward(req, resp);
    }

    private void insertUser(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException{
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String country = req.getParameter("country");
        User newuser = new User(name, email, country);
        userDAO.insertUser(newuser);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("user/create.jsp");
        requestDispatcher.forward(req, resp);
    }

    private void updateUser(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String country = req.getParameter("country");

        User book = new User(id, name, email, country);
        userDAO.updateUser(book);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("user/edit.jsp");
        requestDispatcher.forward(req, resp);
    }
    private void deleteUser(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        userDAO.deleteUser(id);

        List<User> listUser = userDAO.selectAllUsers();
        req.setAttribute("listUser", listUser);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("user/list.jsp");
        requestDispatcher.forward(req, resp);
    }
}
