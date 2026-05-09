package onlineexam;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ExamWebApp {
    public static void main(String[] args) throws IOException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 9090;
        ExamService examService = new ExamService();
        SessionStore sessions = new SessionStore();
        ExamController controller = new ExamController(examService, sessions);

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", controller::handle);
        server.setExecutor(null);
        server.start();

        System.out.println("Online Exam System is running.");
        System.out.println("Preview URL: http://localhost:" + port + "/");
    }
}

class ExamController {
    private final ExamService examService;
    private final SessionStore sessions;

    public ExamController(ExamService examService, SessionStore sessions) {
        this.examService = examService;
        this.sessions = sessions;
    }

    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        if ("GET".equals(method) && "/".equals(path)) {
            showLogin(exchange, "");
            return;
        }
        if ("POST".equals(method) && "/login".equals(path)) {
            login(exchange);
            return;
        }
        if ("GET".equals(method) && "/exam".equals(path)) {
            showExam(exchange);
            return;
        }
        if ("POST".equals(method) && "/submit".equals(path)) {
            submitExam(exchange);
            return;
        }
        if ("POST".equals(method) && "/logout".equals(path)) {
            logout(exchange);
            return;
        }
        send(exchange, 404, Layout.page("Not Found", Layout.notFound()));
    }

    private void showLogin(HttpExchange exchange, String error) throws IOException {
        send(exchange, 200, Layout.page("Student Login", Layout.login(error)));
    }

    private void login(HttpExchange exchange) throws IOException {
        Map<String, String> form = readForm(exchange);
        String username = form.getOrDefault("username", "").trim();
        String password = form.getOrDefault("password", "").trim();

        Optional<Student> student = examService.authenticate(username, password);
        if (student.isEmpty()) {
            showLogin(exchange, "Invalid login. Try student1 / pass123 or student2 / pass123.");
            return;
        }

        String token = sessions.create(student.get());
        Headers headers = exchange.getResponseHeaders();
        headers.add("Set-Cookie", "SESSION=" + token + "; HttpOnly; Path=/");
        redirect(exchange, "/exam");
    }

    private void showExam(HttpExchange exchange) throws IOException {
        Optional<Student> student = currentStudent(exchange);
        if (student.isEmpty()) {
            redirect(exchange, "/");
            return;
        }
        send(exchange, 200, Layout.page("Online Exam", Layout.exam(student.get(), examService.getQuestions())));
    }

    private void submitExam(HttpExchange exchange) throws IOException {
        Optional<Student> student = currentStudent(exchange);
        if (student.isEmpty()) {
            redirect(exchange, "/");
            return;
        }
        Map<String, String> form = readForm(exchange);
        ExamResult result = examService.evaluate(student.get(), form);
        send(exchange, 200, Layout.page("Final Result", Layout.result(result, examService.getQuestions())));
    }

    private void logout(HttpExchange exchange) throws IOException {
        readSessionId(exchange).ifPresent(sessions::remove);
        exchange.getResponseHeaders().add("Set-Cookie", "SESSION=deleted; Max-Age=0; Path=/");
        redirect(exchange, "/");
    }

    private Optional<Student> currentStudent(HttpExchange exchange) {
        return readSessionId(exchange).flatMap(sessions::find);
    }

    private Optional<String> readSessionId(HttpExchange exchange) {
        List<String> cookies = exchange.getRequestHeaders().get("Cookie");
        if (cookies == null) {
            return Optional.empty();
        }
        for (String cookieHeader : cookies) {
            String[] parts = cookieHeader.split(";");
            for (String part : parts) {
                String[] pair = part.trim().split("=", 2);
                if (pair.length == 2 && "SESSION".equals(pair[0])) {
                    return Optional.of(pair[1]);
                }
            }
        }
        return Optional.empty();
    }

    private Map<String, String> readForm(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> form = new HashMap<>();
        if (body.isBlank()) {
            return form;
        }
        for (String pair : body.split("&")) {
            String[] data = pair.split("=", 2);
            String key = decode(data[0]);
            String value = data.length > 1 ? decode(data[1]) : "";
            form.put(key, value);
        }
        return form;
    }

    private String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private void redirect(HttpExchange exchange, String location) throws IOException {
        exchange.getResponseHeaders().add("Location", location);
        exchange.sendResponseHeaders(302, -1);
        exchange.close();
    }

    private void send(HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        Headers headers = exchange.getResponseHeaders();
        headers.add("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream output = exchange.getResponseBody()) {
            output.write(bytes);
        }
    }
}

class ExamService {
    private final List<Student> students;
    private final List<Question> questions;

    public ExamService() {
        students = List.of(
                new Student("student1", "pass123", "Aarav Kumar"),
                new Student("student2", "pass123", "Diya Sharma")
        );

        questions = List.of(
                new Question(1, "Which Java keyword creates a subclass?", List.of("extends", "implements", "inherits", "instanceof"), 0),
                new Question(2, "Which method starts a normal Java application?", List.of("start()", "main()", "run()", "init()"), 1),
                new Question(3, "Which collection stores unique elements?", List.of("ArrayList", "LinkedList", "HashSet", "Queue"), 2),
                new Question(4, "What is a constructor used for?", List.of("Destroying objects", "Initializing objects", "Importing packages", "Handling exceptions"), 1),
                new Question(5, "Which access modifier is most restrictive?", List.of("public", "protected", "default", "private"), 3)
        );
    }

    public Optional<Student> authenticate(String username, String password) {
        return students.stream()
                .filter(student -> student.username().equals(username) && student.password().equals(password))
                .findFirst();
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public ExamResult evaluate(Student student, Map<String, String> answers) {
        int score = 0;
        List<AnswerReview> reviews = new ArrayList<>();

        for (Question question : questions) {
            String submitted = answers.get("q" + question.id());
            int chosenIndex = parseChoice(submitted);
            boolean correct = chosenIndex == question.correctOptionIndex();
            if (correct) {
                score++;
            }
            reviews.add(new AnswerReview(question, chosenIndex, correct));
        }

        return new ExamResult(student, score, questions.size(), reviews, LocalDateTime.now());
    }

    private int parseChoice(String submitted) {
        if (submitted == null) {
            return -1;
        }
        try {
            return Integer.parseInt(submitted);
        } catch (NumberFormatException ignored) {
            return -1;
        }
    }
}

class SessionStore {
    private final Map<String, Student> sessions = new ConcurrentHashMap<>();

    public String create(Student student) {
        String token = UUID.randomUUID().toString();
        sessions.put(token, student);
        return token;
    }

    public Optional<Student> find(String token) {
        return Optional.ofNullable(sessions.get(token));
    }

    public void remove(String token) {
        sessions.remove(token);
    }
}

record Student(String username, String password, String fullName) {
}

record Question(int id, String text, List<String> options, int correctOptionIndex) {
}

record AnswerReview(Question question, int selectedOptionIndex, boolean correct) {
}

record ExamResult(Student student, int score, int totalQuestions, List<AnswerReview> reviews, LocalDateTime submittedAt) {
    public double percentage() {
        return (score * 100.0) / totalQuestions;
    }

    public String grade() {
        double percentage = percentage();
        if (percentage >= 80) {
            return "Excellent";
        }
        if (percentage >= 60) {
            return "Good";
        }
        if (percentage >= 40) {
            return "Needs Practice";
        }
        return "Retake Recommended";
    }
}

class Layout {
    private Layout() {
    }

    public static String page(String title, String content) {
        return """
                <!doctype html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>%s</title>
                    <style>
                        * { box-sizing: border-box; }
                        body {
                            margin: 0;
                            min-height: 100vh;
                            font-family: Arial, Helvetica, sans-serif;
                            color: #17202a;
                            background: linear-gradient(135deg, #f8fbff 0%%, #eef7f1 45%%, #fff7eb 100%%);
                        }
                        .shell { width: min(1040px, calc(100%% - 32px)); margin: 0 auto; padding: 28px 0 42px; }
                        .topbar {
                            display: flex;
                            justify-content: space-between;
                            align-items: center;
                            gap: 16px;
                            margin-bottom: 24px;
                        }
                        .brand { display: flex; align-items: center; gap: 12px; font-weight: 800; font-size: 20px; }
                        .mark {
                            width: 42px;
                            height: 42px;
                            border-radius: 8px;
                            display: grid;
                            place-items: center;
                            background: #123c69;
                            color: white;
                            font-weight: 900;
                        }
                        .panel {
                            background: rgba(255, 255, 255, 0.92);
                            border: 1px solid rgba(18, 60, 105, 0.12);
                            border-radius: 8px;
                            box-shadow: 0 20px 60px rgba(31, 48, 75, 0.12);
                        }
                        .hero {
                            display: grid;
                            grid-template-columns: 1fr 0.8fr;
                            min-height: 520px;
                            overflow: hidden;
                        }
                        .intro { padding: 52px; background: #123c69; color: white; }
                        .intro h1, .exam-head h1, .result-hero h1 {
                            margin: 0;
                            font-size: clamp(32px, 5vw, 56px);
                            line-height: 1.02;
                            letter-spacing: 0;
                        }
                        .intro p { color: #d9ebff; font-size: 18px; line-height: 1.6; max-width: 560px; }
                        .stat-row { display: grid; grid-template-columns: repeat(3, 1fr); gap: 12px; margin-top: 36px; }
                        .stat {
                            padding: 14px;
                            border-radius: 8px;
                            background: rgba(255,255,255,0.12);
                            border: 1px solid rgba(255,255,255,0.16);
                        }
                        .stat strong { display: block; font-size: 24px; }
                        .login-box { padding: 52px; display: flex; flex-direction: column; justify-content: center; }
                        label { display: block; margin: 18px 0 8px; font-weight: 700; color: #243447; }
                        input[type="text"], input[type="password"] {
                            width: 100%%;
                            border: 1px solid #cbd6e2;
                            border-radius: 8px;
                            padding: 14px 15px;
                            font-size: 16px;
                            background: #fbfdff;
                        }
                        button {
                            border: 0;
                            border-radius: 8px;
                            padding: 14px 18px;
                            background: #1f7a5c;
                            color: white;
                            font-weight: 800;
                            font-size: 16px;
                            cursor: pointer;
                        }
                        button:hover { background: #176247; }
                        .ghost { background: #eef4f8; color: #123c69; }
                        .ghost:hover { background: #dceaf2; }
                        .error {
                            margin-top: 16px;
                            padding: 12px;
                            border-radius: 8px;
                            background: #fff1f0;
                            color: #a12b22;
                            border: 1px solid #ffd1cc;
                        }
                        .hint { color: #617387; font-size: 14px; line-height: 1.5; margin-top: 16px; }
                        .exam-head {
                            padding: 30px;
                            background: #123c69;
                            color: white;
                            border-radius: 8px;
                            display: flex;
                            justify-content: space-between;
                            gap: 20px;
                            align-items: center;
                        }
                        .exam-head p { margin: 10px 0 0; color: #d9ebff; }
                        .question {
                            padding: 24px;
                            margin-top: 18px;
                        }
                        .question h2 { margin: 0 0 18px; font-size: 20px; }
                        .option {
                            display: flex;
                            align-items: center;
                            gap: 10px;
                            padding: 12px;
                            border: 1px solid #d7e1ea;
                            border-radius: 8px;
                            margin: 10px 0;
                            background: #fbfdff;
                        }
                        .actions { margin-top: 22px; display: flex; justify-content: flex-end; gap: 12px; }
                        .result-hero {
                            padding: 34px;
                            background: #123c69;
                            color: white;
                            border-radius: 8px;
                            display: grid;
                            grid-template-columns: 1fr auto;
                            align-items: center;
                            gap: 20px;
                        }
                        .score-badge {
                            width: 160px;
                            height: 160px;
                            border-radius: 50%%;
                            display: grid;
                            place-items: center;
                            text-align: center;
                            background: #ffffff;
                            color: #123c69;
                            font-weight: 900;
                            box-shadow: inset 0 0 0 10px #e0f2eb;
                        }
                        .score-badge strong { display: block; font-size: 42px; }
                        .review {
                            padding: 18px 22px;
                            margin-top: 14px;
                        }
                        .correct { border-left: 5px solid #1f7a5c; }
                        .wrong { border-left: 5px solid #c9483a; }
                        .review p { margin: 8px 0; color: #405163; }
                        @media (max-width: 760px) {
                            .hero, .result-hero { grid-template-columns: 1fr; }
                            .intro, .login-box { padding: 28px; }
                            .stat-row { grid-template-columns: 1fr; }
                            .exam-head { align-items: flex-start; flex-direction: column; }
                            .score-badge { width: 132px; height: 132px; }
                        }
                    </style>
                </head>
                <body>
                    <main class="shell">
                        <div class="topbar">
                            <div class="brand"><div class="mark">OE</div><span>Online Exam System</span></div>
                        </div>
                        %s
                    </main>
                </body>
                </html>
                """.formatted(escape(title), content);
    }

    public static String login(String error) {
        String errorHtml = error.isBlank() ? "" : "<div class=\"error\">" + escape(error) + "</div>";
        return """
                <section class="panel hero">
                    <div class="intro">
                        <h1>Java Online Exam</h1>
                        <p>Log in, answer multiple-choice questions, and get your score instantly with a clean result review.</p>
                        <div class="stat-row">
                            <div class="stat"><strong>5</strong><span>Questions</span></div>
                            <div class="stat"><strong>Auto</strong><span>Scoring</span></div>
                            <div class="stat"><strong>Live</strong><span>Result</span></div>
                        </div>
                    </div>
                    <form class="login-box" method="post" action="/login">
                        <h2>Student Login</h2>
                        <label for="username">Username</label>
                        <input id="username" name="username" type="text" autocomplete="username" required>
                        <label for="password">Password</label>
                        <input id="password" name="password" type="password" autocomplete="current-password" required>
                        <div class="actions"><button type="submit">Start Exam</button></div>
                        %s
                        <p class="hint">Demo accounts: student1 / pass123, student2 / pass123</p>
                    </form>
                </section>
                """.formatted(errorHtml);
    }

    public static String exam(Student student, List<Question> questions) {
        StringBuilder html = new StringBuilder();
        html.append("""
                <section class="exam-head">
                    <div>
                        <h1>Exam Paper</h1>
                        <p>Welcome, %s. Choose one answer for each question.</p>
                    </div>
                    <form method="post" action="/logout"><button class="ghost" type="submit">Logout</button></form>
                </section>
                <form method="post" action="/submit">
                """.formatted(escape(student.fullName())));

        for (Question question : questions) {
            html.append("<section class=\"panel question\">");
            html.append("<h2>").append(question.id()).append(". ").append(escape(question.text())).append("</h2>");
            for (int i = 0; i < question.options().size(); i++) {
                html.append("""
                        <label class="option">
                            <input type="radio" name="q%d" value="%d" required>
                            <span>%s</span>
                        </label>
                        """.formatted(question.id(), i, escape(question.options().get(i))));
            }
            html.append("</section>");
        }

        html.append("""
                    <div class="actions"><button type="submit">Submit Exam</button></div>
                </form>
                """);
        return html.toString();
    }

    public static String result(ExamResult result, List<Question> questions) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
        StringBuilder html = new StringBuilder();
        html.append("""
                <section class="result-hero">
                    <div>
                        <h1>Final Result</h1>
                        <p>%s completed the exam on %s.</p>
                        <p><strong>Status:</strong> %s</p>
                    </div>
                    <div class="score-badge"><div><strong>%d/%d</strong><span>%.0f%%</span></div></div>
                </section>
                """.formatted(
                escape(result.student().fullName()),
                escape(result.submittedAt().format(formatter)),
                escape(result.grade()),
                result.score(),
                result.totalQuestions(),
                result.percentage()
        ));

        for (AnswerReview review : result.reviews()) {
            Question question = review.question();
            String selected = review.selectedOptionIndex() >= 0
                    ? question.options().get(review.selectedOptionIndex())
                    : "Not answered";
            String correct = question.options().get(question.correctOptionIndex());
            html.append("""
                    <section class="panel review %s">
                        <strong>%d. %s</strong>
                        <p>Your answer: %s</p>
                        <p>Correct answer: %s</p>
                    </section>
                    """.formatted(
                    review.correct() ? "correct" : "wrong",
                    question.id(),
                    escape(question.text()),
                    escape(selected),
                    escape(correct)
            ));
        }

        html.append("""
                <form class="actions" method="get" action="/exam">
                    <button type="submit">Retake Exam</button>
                </form>
                """);
        return html.toString();
    }

    public static String notFound() {
        return """
                <section class="panel question">
                    <h1>Page Not Found</h1>
                    <p>The page you requested does not exist.</p>
                    <form method="get" action="/"><button type="submit">Go Home</button></form>
                </section>
                """;
    }

    private static String escape(String value) {
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
