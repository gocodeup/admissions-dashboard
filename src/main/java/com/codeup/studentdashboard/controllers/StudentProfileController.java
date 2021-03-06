package com.codeup.studentdashboard.controllers;

//import com.codeup.studentdashboard.dao.repository.PaymentOptionsRepository;
import com.codeup.studentdashboard.dao.repository.StudentRepository;
import com.codeup.studentdashboard.models.Cohort;
import com.codeup.studentdashboard.models.Events;
import com.codeup.studentdashboard.models.Student;
import com.codeup.studentdashboard.models.converters.StudentBillboardsConverter;
import com.codeup.studentdashboard.models.converters.StudentDescribeConverter;
import com.codeup.studentdashboard.models.converters.StudentGenderConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

@Controller
public class StudentProfileController {

    private final StudentRepository studentRepository;

    public StudentProfileController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping("/users/students/{id}")
    public String studentProfile(@PathVariable String id, Model model) {

        Long idNumber = Long.parseLong(id);
        Student currentStudent = studentRepository.findById(idNumber);
        model.addAttribute("student", currentStudent);

        Cohort studentCohort = currentStudent.getCohort();
        if (studentCohort != null)
            model.addAttribute("cohort", studentCohort.toString());
        else
            model.addAttribute("cohort",  null);

        // GRAB GENDER
        String gender = new StudentGenderConverter().convertToDatabaseColumn(currentStudent.getGender());

        model.addAttribute("gender", gender);

        // GRAB BILLBOARDS
        String billboard = new StudentBillboardsConverter().convertToDatabaseColumn(currentStudent.getBillboards());

        model.addAttribute("billboard", billboard);

        // GRAB DESCRIPTION OF SELF
        String description = new StudentDescribeConverter().convertToDatabaseColumn(currentStudent.getDescribe());

        model.addAttribute("description", description);

        // GRAB EVENTS

        List<Events> events = currentStudent.getEvents();
        for (Events e : events) {
            System.out.println(e.getType().getName());
        }
        model.addAttribute("events", events);

        // 1. APPLIED
        // 2. PHONE INTERVIEW SCHEDULED
        // 3. PHONE INTERVIEW
        // 4. TOUR SCHEDULED
        // 5. TOUR
        // 6. PROBLEM SOLVING CHALLENGE
        // 7. CL REVIEW ASSIGNED
        // 8. CL REVIEW COMPLETED
        // 9. BEHAVIOR INTERVIEW SCHEDULED
        // 10. BEHAVIOR INTERVIEW
        // ---------------------------
        // 1. VERBAL ACCEPTED
        // 2. PRE-WORK ASSIGNED
        // 3. ACCEPTANCE PHONE CALL
        // 4. VERBAL ACCEPTANCE
        // 5. ENROLLMENT EMAIL SEND
        // 6. DEPOSIT PAYED
        // ----------------------------
        // 1. FINANCIAL CALL
        // 2. FINANCE ARRANGED
        // 3. STUDENT

        int beginningCount = 0;
        int middleCount = 0;
        int endCount = 0;

        List<String> e = new ArrayList<>();
            for (Events event : events) {
                e.add(event.getType().getName());
            }

            for (String s : e) {
                if (s.equals("Applied") || s.equals("Phone Interview Scheduled") || s.equals("Phone Interview") ||
                        s.equals("Tour Scheduled") || s.equals("Tour") || s.equals("Problem Solving Challenge") ||
                        s.equals("Command Line Review Assigned") || s.equals("Command Line Review Completed") ||
                        s.equals("Behavior Interview Scheduled") || s.equals("Behavior Interview")) {
                    beginningCount += 10;
                }
            }

            model.addAttribute("bProgress", beginningCount);
            model.addAttribute("barWidth", beginningCount);

        return "/users/studentProfile";
    }
}
