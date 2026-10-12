package com.example.auth.controller;
import com.example.auth.dto.*;
import com.example.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;
@Controller public class AuthController {
private final AuthService service;
public AuthController(AuthService service){this.service=service;}
@GetMapping("/") public String home(){return "redirect:/dashboard";}
@GetMapping("/login") public String login(){return "login";}
@GetMapping("/register") public String registerPage(Model m){m.addAttribute("form",new RegisterForm());return "register";}
@PostMapping("/register") public String register(@Valid @ModelAttribute("form") RegisterForm form,BindingResult errors,Model m,RedirectAttributes flash){if(!form.password.equals(form.confirmPassword))errors.rejectValue("confirmPassword","mismatch","Passwords do not match");if(errors.hasErrors())return "register";try{service.register(form);flash.addFlashAttribute("success","Account created. Please sign in.");return "redirect:/login";}catch(IllegalArgumentException ex){m.addAttribute("error",ex.getMessage());return "register";}}
@GetMapping("/forgot") public String forgot(){return "forgot";}
@PostMapping("/forgot") public String forgotSubmit(@RequestParam String email,Model m){try{service.requestReset(email);}catch(Exception ex){/* Avoid disclosing account existence; log failures in production. */}m.addAttribute("success","If this email is registered, a reset link will be sent.");return "forgot";}
@GetMapping("/reset") public String resetPage(@RequestParam String token,Model m){if(!service.validToken(token)){m.addAttribute("error","Invalid or expired reset link");return "forgot";}m.addAttribute("token",token);m.addAttribute("form",new PasswordForm());return "reset";}
@PostMapping("/reset") public String resetSubmit(@RequestParam String token,@Valid @ModelAttribute("form") PasswordForm form,BindingResult errors,Model m,RedirectAttributes flash){m.addAttribute("token",token);if(!form.password.equals(form.confirmPassword))errors.rejectValue("confirmPassword","mismatch","Passwords do not match");if(errors.hasErrors())return "reset";try{service.reset(token,form);flash.addFlashAttribute("success","Password updated. Please log in.");return "redirect:/login";}catch(IllegalArgumentException ex){m.addAttribute("error",ex.getMessage());return "reset";}}
@GetMapping("/dashboard") public String dashboard(Principal principal,Model m){m.addAttribute("email",principal.getName());return "dashboard";}
}
