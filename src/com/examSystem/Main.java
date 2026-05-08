package com.examSystem;

import com.examSystem.ui.ExamController;

/**
 * ╔═══════════════════════════════════════════════════╗
 * ║        Online Examination System - Main Entry     ║
 * ║                                                   ║
 * ║  Features:                                        ║
 * ║  ✅ Student Login & Registration                  ║
 * ║  ✅ Multiple Choice Questions                     ║
 * ║  ✅ Automatic Score Calculation                   ║
 * ║  ✅ Detailed Result Card with Grade               ║
 * ║  ✅ Performance Bar Chart                         ║
 * ║  ✅ Answer Review                                 ║
 * ╚═══════════════════════════════════════════════════╝
 *
 * Demo Accounts:
 *   STU001 / alice123
 *   STU002 / bob456
 *   STU003 / carol789
 *
 * Available Exams:
 *   EXAM001 - Java Programming (10 questions)
 *   EXAM002 - Mathematics     (8 questions)
 *   EXAM003 - General Knowledge (6 questions)
 */
public class Main {

    public static void main(String[] args) {
        ExamController controller = new ExamController();
        controller.launch();
    }
}
