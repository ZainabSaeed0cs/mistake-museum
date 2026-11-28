
package com.mistakemuseum.mistake_museum.controller;

import com.mistakemuseum.mistake_museum.model.Student;
import com.mistakemuseum.mistake_museum.service.AccountDeletionService;
import com.mistakemuseum.mistake_museum.service.StudentService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AccountController {

    private final StudentService studentService;
    private final AccountDeletionService deletionService;

    public AccountController(StudentService studentService,
                             AccountDeletionService deletionService) {
        this.studentService = studentService;
        this.deletionService = deletionService;
    }

    @PostMapping("/account/delete")
    public String deleteFromPage(Authentication authentication,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        deleteEverything(authentication, request, response);
        return "redirect:/?deleted=true";
    }

    @DeleteMapping("/api/me")
    @ResponseBody
    public ResponseEntity<Void> deleteFromApi(Authentication authentication,
                                              HttpServletRequest request,
                                              HttpServletResponse response) {
        deleteEverything(authentication, request, response);
        return ResponseEntity.noContent().build();
    }

    private void deleteEverything(Authentication authentication,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        Student student = studentService.getCurrentStudent(authentication, request, response);
        deletionService.deleteStudentData(student.getId());

        new SecurityContextLogoutHandler().logout(request, response, authentication);

        Cookie cookie = new Cookie("studentId", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
