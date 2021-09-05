package lk.covid19.contact_tracer.asset.common_asset.controller;


import lk.covid19.contact_tracer.asset.common_asset.model.PasswordChange;
import lk.covid19.contact_tracer.asset.user.entity.User;
import lk.covid19.contact_tracer.asset.user.service.UserService;
import lk.covid19.contact_tracer.asset.user_details.entity.UserDetails;
import lk.covid19.contact_tracer.asset.user_details_files.service.UserDetailsFilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ProfileController {
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final UserDetailsFilesService userDetailsFilesService;


  @GetMapping( value = "/profile" )
  public String userProfile(Model model, Principal principal) {
    UserDetails userDetails = userService.findByUserName(principal.getName()).getUserDetails();
    model.addAttribute("userDetail", userDetails);
    model.addAttribute("file", userDetailsFilesService.userDetailsFileDownloadLinks(userDetails));
    return "userDetails/userDetails-detail";
  }

  @GetMapping( value = "/passwordChange" )
  public String passwordChangeForm(Model model) {
    model.addAttribute("pswChange", new PasswordChange());
    return "login/passwordChange";
  }

  @PostMapping( value = "/passwordChange" )
  public String passwordChange(@Valid @ModelAttribute PasswordChange passwordChange,
                               BindingResult result, RedirectAttributes redirectAttributes) {
    User user =
        userService.findById(userService.findByUserIdByUserName(SecurityContextHolder.getContext().getAuthentication().getName()));

    if ( passwordEncoder.matches(passwordChange.getOldPassword(), user.getPassword()) && !result.hasErrors() && passwordChange.getNewPassword().equals(passwordChange.getNewPasswordConform()) ) {

      user.setPassword(passwordChange.getNewPassword());
      userService.persist(user);

      redirectAttributes.addFlashAttribute("message", "Congratulations .!! Success password is changed");
      redirectAttributes.addFlashAttribute("alertClass", "alert-success");
      return "redirect:/home";

    }
    redirectAttributes.addFlashAttribute("message", "Failed");
    redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
    return "redirect:/passwordChange";

  }
}