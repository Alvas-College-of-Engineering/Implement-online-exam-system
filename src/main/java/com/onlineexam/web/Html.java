package com.onlineexam.web;

import com.onlineexam.model.ExamResult;
import com.onlineexam.model.Question;
import com.onlineexam.model.User;
import java.util.List;

public final class Html {
    private Html() {
    }

    public static String loginPage(String error) {
        String alert = error == null ? "" : "<div class='alert'>" + escape(error) + "</div>";
        return page("Login",
                "<main class='split'>"
                        + "<section class='brand-panel'>"
                        + "<div class='badge'>Java Dynamic Web Project</div>"
                        + "<h1>Online Exam System</h1>"
                        + "<p>Log in, answer multiple-choice questions, and get an instant calculated result.</p>"
                        + "<div class='stats'><span>6 Questions</span><span>Auto Score</span><span>No JS</span></div>"
                        + "</section>"
                        + "<section class='form-panel'>"
                        + "<h2>Student Login</h2>"
                        + alert
                        + "<form method='post' action='/login'>"
                        + "<label>Username<input name='username' value='student' required></label>"
                        + "<label>Password<input name='password' type='password' value='student123' required></label>"
                        + "<button type='submit'>Start Exam</button>"
                        + "</form>"
                        + "<p class='hint'>Try student / student123 or mahesh / exam123</p>"
                        + "</section>"
                        + "</main>");
    }

    public static String examPage(User user, List<Question> questions) {
        StringBuilder builder = new StringBuilder();
        builder.append("<main class='exam-shell'>");
        builder.append("<header class='topbar'><div><span class='eyebrow'>Welcome</span><h1>")
                .append(escape(user.getFullName()))
                .append("</h1></div><a class='link-button' href='/logout'>Logout</a></header>");
        builder.append("<form method='post' action='/submit' class='question-list'>");
        int number = 1;
        for (Question question : questions) {
            builder.append("<section class='question-card'>");
            builder.append("<div class='question-head'><span>Question ").append(number++).append("</span><strong>")
                    .append(escape(question.getText())).append("</strong></div>");
            builder.append("<div class='options'>");
            List<String> options = question.getOptions();
            for (int i = 0; i < options.size(); i++) {
                builder.append("<label class='option'><input type='radio' name='q")
                        .append(question.getId())
                        .append("' value='")
                        .append(i)
                        .append("' required><span>")
                        .append(escape(options.get(i)))
                        .append("</span></label>");
            }
            builder.append("</div></section>");
        }
        builder.append("<div class='submit-row'><button type='submit'>Submit Exam</button></div>");
        builder.append("</form></main>");
        return page("Exam", builder.toString());
    }

    public static String resultPage(ExamResult result) {
        int score = result.getScorePercent();
        return page("Result",
                "<main class='result-shell'>"
                        + "<section class='result-hero'>"
                        + "<span class='eyebrow'>Final Result</span>"
                        + "<h1>" + escape(result.getGrade()) + "</h1>"
                        + "<div class='score-ring'><span>" + score + "%</span></div>"
                        + "<p>" + escape(result.getUser().getFullName()) + ", your exam was submitted on " + escape(result.getSubmittedAtText()) + ".</p>"
                        + "</section>"
                        + "<section class='summary-grid'>"
                        + metric("Total Questions", result.getTotalQuestions())
                        + metric("Correct Answers", result.getCorrectAnswers())
                        + metric("Wrong Answers", result.getWrongAnswers())
                        + "</section>"
                        + "<nav class='actions'><a class='link-button primary' href='/exam'>Retake Exam</a><a class='link-button' href='/logout'>Logout</a></nav>"
                        + "</main>");
    }

    public static String notFoundPage() {
        return page("Not Found", "<main class='center'><h1>Page not found</h1><a class='link-button primary' href='/'>Go Home</a></main>");
    }

    private static String metric(String label, int value) {
        return "<article class='metric'><span>" + escape(label) + "</span><strong>" + value + "</strong></article>";
    }

    private static String page(String title, String content) {
        return "<!doctype html><html lang='en'><head><meta charset='UTF-8'>"
                + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                + "<title>" + escape(title) + " | Online Exam System</title>"
                + "<link rel='stylesheet' href='/styles.css'>"
                + "</head><body>" + content + "</body></html>";
    }

    public static String css() {
        return """
                :root{color-scheme:light;--ink:#18212f;--muted:#667085;--line:#d7dde8;--paper:#ffffff;--mist:#eef5f7;--teal:#006d77;--coral:#d95d39;--gold:#f2b84b;--green:#2f8f67}
                *{box-sizing:border-box}body{margin:0;font-family:Arial,Helvetica,sans-serif;background:linear-gradient(135deg,#f6fbfc,#fff8ef);color:var(--ink);min-height:100vh}a{color:inherit}
                .split{min-height:100vh;display:grid;grid-template-columns:1.1fr .9fr}.brand-panel{padding:72px;display:flex;flex-direction:column;justify-content:center;background:linear-gradient(150deg,#006d77,#1c4c5b);color:white;position:relative;overflow:hidden}.brand-panel:after{content:"";position:absolute;right:-120px;bottom:-120px;width:360px;height:360px;border:44px solid rgba(242,184,75,.45);border-radius:50%}.badge,.eyebrow{font-size:12px;text-transform:uppercase;letter-spacing:0;font-weight:700}.badge{width:max-content;background:rgba(255,255,255,.16);border:1px solid rgba(255,255,255,.28);padding:8px 12px;border-radius:999px}.brand-panel h1{font-size:56px;line-height:1.02;margin:22px 0 16px;max-width:620px}.brand-panel p{font-size:20px;line-height:1.6;max-width:580px;color:#e7f5f6}.stats{display:flex;gap:12px;margin-top:28px;flex-wrap:wrap}.stats span{background:rgba(255,255,255,.13);border:1px solid rgba(255,255,255,.24);border-radius:8px;padding:12px 14px;font-weight:700}
                .form-panel{align-self:center;margin:32px;max-width:440px;width:calc(100% - 64px);justify-self:center;background:var(--paper);border:1px solid var(--line);border-radius:8px;padding:34px;box-shadow:0 24px 70px rgba(24,33,47,.14)}h2{font-size:30px;margin:0 0 22px}label{display:block;font-weight:700;margin:16px 0 6px}input{width:100%;border:1px solid var(--line);border-radius:8px;padding:13px 14px;font-size:16px;margin-top:8px;background:#fbfcfe}button,.link-button{border:0;border-radius:8px;background:var(--coral);color:white;font-weight:800;font-size:16px;padding:13px 18px;text-decoration:none;display:inline-flex;justify-content:center;align-items:center;cursor:pointer}button{width:100%;margin-top:22px}.link-button{background:#243447}.link-button.primary{background:var(--teal)}.hint{color:var(--muted);font-size:14px}.alert{background:#fff2f0;color:#9f2f16;border:1px solid #ffc8bd;border-radius:8px;padding:12px;margin-bottom:12px;font-weight:700}
                .exam-shell{max-width:1040px;margin:0 auto;padding:30px 18px 56px}.topbar{display:flex;justify-content:space-between;gap:18px;align-items:center;margin-bottom:24px}.topbar h1{margin:4px 0 0;font-size:34px}.question-list{display:grid;gap:16px}.question-card{background:var(--paper);border:1px solid var(--line);border-radius:8px;padding:22px;box-shadow:0 12px 34px rgba(24,33,47,.08)}.question-head span{display:block;color:var(--teal);font-weight:800;margin-bottom:7px}.question-head strong{font-size:19px}.options{display:grid;grid-template-columns:repeat(2,minmax(0,1fr));gap:12px;margin-top:18px}.option{margin:0;border:1px solid var(--line);border-radius:8px;padding:13px;display:flex;gap:10px;align-items:center;background:#fbfcfe;cursor:pointer}.option:has(input:checked){border-color:var(--teal);background:#edf8f7}.option input{width:auto;margin:0}.submit-row{position:sticky;bottom:0;background:linear-gradient(180deg,rgba(255,255,255,0),#fff8ef 34%);padding:28px 0 0}.submit-row button{max-width:260px;display:flex;margin-left:auto}
                .result-shell{max-width:920px;margin:0 auto;padding:54px 18px}.result-hero{text-align:center;background:var(--paper);border:1px solid var(--line);border-radius:8px;padding:42px;box-shadow:0 24px 70px rgba(24,33,47,.12)}.result-hero h1{font-size:46px;margin:10px 0 20px}.score-ring{width:168px;height:168px;border-radius:50%;border:18px solid var(--green);display:grid;place-items:center;margin:0 auto 20px;background:#f5fffa}.score-ring span{font-size:38px;font-weight:900}.result-hero p{color:var(--muted);font-size:18px}.summary-grid{display:grid;grid-template-columns:repeat(3,1fr);gap:14px;margin:18px 0}.metric{background:var(--paper);border:1px solid var(--line);border-radius:8px;padding:20px;text-align:center}.metric span{display:block;color:var(--muted);font-weight:700}.metric strong{display:block;font-size:34px;margin-top:8px}.actions{display:flex;justify-content:center;gap:12px}.center{min-height:100vh;display:grid;place-content:center;text-align:center;gap:16px}
                @media (max-width:800px){.split{grid-template-columns:1fr}.brand-panel{padding:44px 24px}.brand-panel h1{font-size:40px}.form-panel{margin:20px;width:calc(100% - 40px);padding:24px}.options,.summary-grid{grid-template-columns:1fr}.topbar{align-items:flex-start}.submit-row button{max-width:none}.result-hero{padding:28px 18px}.result-hero h1{font-size:34px}}
                """;
    }

    public static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
