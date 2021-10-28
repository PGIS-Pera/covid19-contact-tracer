package lk.covid19.contact_tracer.asset.user_password.controller;


import lk.covid19.contact_tracer.asset.role.service.RoleService;
import lk.covid19.contact_tracer.asset.user.entity.User;
import lk.covid19.contact_tracer.asset.user.service.UserService;
import lk.covid19.contact_tracer.asset.user_details.entity.UserDetails;
import lk.covid19.contact_tracer.asset.user_details.service.UsersDetailsService;
import lk.covid19.contact_tracer.asset.user_password.entity.ConformationToken;
import lk.covid19.contact_tracer.asset.user_password.service.ConformationTokenService;
import lk.covid19.contact_tracer.util.service.CommonService;
import lk.covid19.contact_tracer.util.service.DateTimeAgeService;
import lk.covid19.contact_tracer.util.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ConformationTokenController {
  private final ConformationTokenService conformationTokenService;
  private final EmailService emailService;
  private final UserService userService;
  private final RoleService roleService;
  private final CommonService commonService;
  private final DateTimeAgeService dateTimeAgeService;
  private final UsersDetailsService usersDetailsService;


  @GetMapping( value = "/forgottenPassword" )
  private String sendEmailForm(Model model, HttpServletRequest request) {
    model.addAttribute("newOrOld", "old");
    return "user/register";
  }

  @PostMapping( value = {"/register"} )
  private String sendTokenToEmail(@RequestParam( "email" ) String email, @RequestParam( "newOrOld" ) String newOrOld,
                                  Model model, HttpServletRequest request) {
    if ( !commonService.isValidEmail(email) ) {
      model.addAttribute("newOrOld", newOrOld);
      model.addAttribute("message", "Please enter valid email.");
      return "user/register";
    }
    //check if there is any user on system
    UserDetails userDetails = usersDetailsService.findByEmail(email);
    if ( userDetails == null ) {
      model.addAttribute("message", "Do not try to mess up with me. you are not an user in our system please contact " +
          "admin.");
      return "user/register";
    }
    User user = userService.findByUserName(userDetails.getUser().getUsername());
    //before create the token need to check there is user on current email
    // if not create token else send forgotten password form to fill
    if ( user != null && newOrOld == null ) {
      model.addAttribute("message", "There is an user on system please forgotten password reset option.");
      return "user/register";
    }
    //check if there is a valid token
    ConformationToken conformationToken = conformationTokenService.findByEmail(email);

    if ( conformationToken != null && dateTimeAgeService.getDateTimeDurationInHours(LocalDateTime.now(),
                                                                                    conformationToken.getEndDate()) < 25L ) {

      model.addAttribute("newOrOld", newOrOld);
      model.addAttribute("message", "There is valid token fot this email " + email + " on the system. \n Please check" +
          " your email.");
      return "user/register";
    }
    if ( conformationToken != null && dateTimeAgeService.getDateTimeDurationInHours(LocalDateTime.now(),
                                                                                    conformationToken.getEndDate()) > 24L ) {

      conformationToken.setCreateDate(LocalDateTime.now());
      conformationToken.setEndDate(LocalDateTime.now().plusDays(1));
      String url = request.getRequestURL().toString();
      emailService.sendEmail(email, "Email Verification (Covid19 Prevention Center) - Not reply",
                             "Please click below link to active your account \n\t".concat(url + "/" + newOrOld +
                                                                                              "/token/" + conformationTokenService.createToken(conformationToken).getToken()).concat("\n  this link is valid only one day. "));
      model.addAttribute("newOrOld", newOrOld);
      model.addAttribute("successMessage", "Please check your email \n Your entered email is \t ".concat(email));
      return "user/successMessage";
    }

    ConformationToken newConformationToken = new ConformationToken(email);
    String url = request.getRequestURL().toString();
    emailService.sendEmail(email, "Covid19 Prevention Center",
                           "Please click below link to active your account \n\t".concat(url + "/" + newOrOld +
                                                                                            "/token/" + conformationTokenService.createToken(newConformationToken).getToken()).concat("\n  this link is valid only one day. "));
    model.addAttribute("successMessage", "Please check your email \n Your entered email is \t ".concat(email));
    model.addAttribute("newOrOld", newOrOld);
    return "user/successMessage";
  }

  @GetMapping( value = {"/register/{newOrOld}/token/{token}"} )
  public String passwordEnterPage(@PathVariable( "newOrOld" ) String newOrOld, @PathVariable( "token" ) String token,
                                  Model model) {

    ConformationToken conformationToken = conformationTokenService.findByToken(token);
    if ( conformationToken != null && LocalDateTime.now().isBefore(conformationToken.getEndDate()) ) {
      model.addAttribute("token", conformationToken.getToken());
      return "user/password";
    }
    conformationTokenService.deleteByConformationToken(conformationToken);
    model.addAttribute("message", "There is no valid token.");
    return "user/register";
  }

  @PostMapping( "/register/user" )
  public String newUser(@RequestParam( "token" ) String token, @RequestParam( "password" ) String password,
                        @RequestParam( "reEnterPassword" ) String reEnterPassword, Model model) {
//token
    ConformationToken conformationToken = conformationTokenService.findByToken(token);
    if ( conformationToken == null ) {
      model.addAttribute("message", "Your token is not valid \n re-register.");
      return "redirect:/register";
    }
    if ( !password.equals(reEnterPassword) ) {
      model.addAttribute("token", token);
      model.addAttribute("message", "Entered password is missed match \n please re enter.");
    }
    //taken user according to token email
    User userDB = userService.findByUserName(conformationToken.getEmail());
    //check password matched or not or not in user db

    if ( userDB != null && password.equals(reEnterPassword) ) {
      userDB.setPassword(password);
      userService.persist(userDB);
    }
    conformationTokenService.deleteByConformationToken(conformationToken);
    return "redirect:/login";
  }

}
