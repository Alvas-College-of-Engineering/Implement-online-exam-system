package com.onlineexam.web;

import com.onlineexam.model.ExamResult;
import com.onlineexam.model.User;
import com.onlineexam.repository.QuestionRepository;
import com.onlineexam.repository.UserRepository;
import com.onlineexam.service.AuthService;
import com.onlineexam.service.ExamService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;

public class ExamServer {
    private static final String SESSION_COOKIE = "ONLINE_EXAM_SESSION";
    private final int port;
    private final HttpServer httpServer;
    private final AuthService authService;
    private final ExamService examService;
    private final SessionManager sessionManager;

    public ExamServer(int port) throws IOException {
        this.port = port;
        this.httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        this.authService = new AuthService(new UserRepository());
        this.examService = new ExamService(new QuestionRepository());
        this.sessionManager = new SessionManager();
        configureRoutes();
    }

    public void start() {
        httpServer.setExecutor(Executors.newFixedThreadPool(8));
        httpServer.start();
        System.out.println("Online Exam System is running at http://localhost:" + port);
        System.out.println("Login: student / student123");
    }

    private void configureRoutes() {
        httpServer.createContext("/", this::route);
    }

    private void route(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        if (path.equals("/styles.css")) {
            send(exchange, 200, "text/css", Html.css());
        } else if (path.equals("/") && method.equals("GET")) {
            redirectIfLoggedInOrShowLogin(exchange);
        } else if (path.equals("/login") && method.equals("POST")) {
            handleLogin(exchange);
        } else if (path.equals("/exam") && method.equals("GET")) {
            handleExam(exchange);
        } else if (path.equals("/submit") && method.equals("POST")) {
            handleSubmit(exchange);
        } else if (path.equals("/result") && method.equals("GET")) {
            handleResult(exchange);
        } else if (path.equals("/logout") && method.equals("GET")) {
            handleLogout(exchange);
        } else {
            send(exchange, 404, "text/html", Html.notFoundPage());
        }
    }

    private void redirectIfLoggedInOrShowLogin(HttpExchange exchange) throws IOException {
        if (currentUser(exchange).isPresent()) {
            redirect(exchange, "/exam");
            return;
        }
        send(exchange, 200, "text/html", Html.loginPage(null));
    }

    private void handleLogin(HttpExchange exchange) throws IOException {
        Map<String, String> form = RequestUtil.parseForm(exchange);
        Optional<User> user = authService.login(form.get("username"), form.get("password"));
        if (user.isEmpty()) {
            send(exchange, 401, "text/html", Html.loginPage("Invalid username or password."));
            return;
        }
        String token = sessionManager.create(user.get());
        exchange.getResponseHeaders().add("Set-Cookie", SESSION_COOKIE + "=" + token + "; HttpOnly; Path=/; SameSite=Lax");
        redirect(exchange, "/exam");
    }

    private void handleExam(HttpExchange exchange) throws IOException {
        Optional<User> user = currentUser(exchange);
        if (user.isEmpty()) {
            redirect(exchange, "/");
            return;
        }
        send(exchange, 200, "text/html", Html.examPage(user.get(), examService.getQuestions()));
    }

    private void handleSubmit(HttpExchange exchange) throws IOException {
        String token = RequestUtil.cookie(exchange, SESSION_COOKIE);
        Optional<User> user = sessionManager.findUser(token);
        if (user.isEmpty()) {
            redirect(exchange, "/");
            return;
        }
        Map<String, String> form = RequestUtil.parseForm(exchange);
        Map<Integer, Integer> answers = new HashMap<>();
        for (String key : form.keySet()) {
            if (key.startsWith("q")) {
                int questionId = Integer.parseInt(key.substring(1));
                int selectedOption = Integer.parseInt(form.get(key));
                answers.put(questionId, selectedOption);
            }
        }
        ExamResult result = examService.evaluate(user.get(), answers);
        sessionManager.saveResult(token, result);
        redirect(exchange, "/result");
    }

    private void handleResult(HttpExchange exchange) throws IOException {
        String token = RequestUtil.cookie(exchange, SESSION_COOKIE);
        Optional<User> user = sessionManager.findUser(token);
        if (user.isEmpty()) {
            redirect(exchange, "/");
            return;
        }
        Optional<ExamResult> result = sessionManager.findResult(token);
        if (result.isEmpty()) {
            redirect(exchange, "/exam");
            return;
        }
        send(exchange, 200, "text/html", Html.resultPage(result.get()));
    }

    private void handleLogout(HttpExchange exchange) throws IOException {
        String token = RequestUtil.cookie(exchange, SESSION_COOKIE);
        sessionManager.logout(token);
        exchange.getResponseHeaders().add("Set-Cookie", SESSION_COOKIE + "=deleted; Max-Age=0; Path=/; SameSite=Lax");
        redirect(exchange, "/");
    }

    private Optional<User> currentUser(HttpExchange exchange) {
        return sessionManager.findUser(RequestUtil.cookie(exchange, SESSION_COOKIE));
    }

    private void redirect(HttpExchange exchange, String location) throws IOException {
        exchange.getResponseHeaders().add("Location", location);
        exchange.sendResponseHeaders(302, -1);
        exchange.close();
    }

    private void send(HttpExchange exchange, int status, String contentType, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", contentType + "; charset=UTF-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(bytes);
        }
    }
}
