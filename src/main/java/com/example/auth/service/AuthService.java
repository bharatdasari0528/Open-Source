package com.example.auth.service;
import com.example.auth.dto.*;
import com.example.auth.entity.*;
import com.example.auth.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.*;
import java.time.Instant;
import java.util.HexFormat;
@Service public class AuthService {
private final UserRepository users;private final ResetTokenRepository tokens;private final PasswordEncoder encoder;private final MailService mail;
@Value("${app.base-url}") private String baseUrl;
public AuthService(UserRepository users,ResetTokenRepository tokens,PasswordEncoder encoder,MailService mail){this.users=users;this.tokens=tokens;this.encoder=encoder;this.mail=mail;}
@Transactional public void register(RegisterForm form){if(!form.password.equals(form.confirmPassword))throw new IllegalArgumentException("Passwords do not match");String email=form.email.trim().toLowerCase();if(users.existsByEmail(email))throw new IllegalArgumentException("Email already registered");User u=new User();u.name=form.name.trim();u.email=email;u.password=encoder.encode(form.password);users.save(u);}
private String hash(String input){try{byte[] bytes=MessageDigest.getInstance("SHA-256").digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));return HexFormat.of().formatHex(bytes);}catch(NoSuchAlgorithmException e){throw new IllegalStateException(e);}}
@Transactional public void requestReset(String email){users.findByEmail(email.trim().toLowerCase()).ifPresent(user->{tokens.deleteByUserId(user.id);byte[] bytes=new byte[32];new SecureRandom().nextBytes(bytes);String raw=HexFormat.of().formatHex(bytes);ResetToken token=new ResetToken();token.user=user;token.tokenHash=hash(raw);token.expiresAt=Instant.now().plusSeconds(900);tokens.saveAndFlush(token);mail.sendReset(user.email,baseUrl+"/reset?token="+raw);});}
@Transactional(readOnly=true) public boolean validToken(String raw){return raw!=null&&tokens.findByTokenHash(hash(raw)).filter(t->t.expiresAt.isAfter(Instant.now())).isPresent();}
@Transactional public void reset(String raw,PasswordForm form){if(!form.password.equals(form.confirmPassword))throw new IllegalArgumentException("Passwords do not match");ResetToken token=tokens.findByTokenHash(hash(raw)).filter(t->t.expiresAt.isAfter(Instant.now())).orElseThrow(()->new IllegalArgumentException("Invalid or expired reset link"));token.user.password=encoder.encode(form.password);users.save(token.user);tokens.deleteByUserId(token.user.id);}
}
