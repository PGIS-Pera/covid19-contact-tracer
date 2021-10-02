package lk.covid19.contact_tracer.asset.common_asset.controller;

import lk.covid19.contact_tracer.asset.report.service.ReportService;
import lk.covid19.contact_tracer.asset.user.service.UserService;
import lk.covid19.contact_tracer.util.service.DateTimeAgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UiController {

  private final UserService userService;
  private final DateTimeAgeService dateTimeAgeService;
  private final ReportService reportService;


  @GetMapping( value = {"/", "/index"} )
  public String index() {
    return "index";
  }

  @GetMapping( value = {"/home", "/mainWindow"} )
  public String getHome(Model model) {
    model.addAttribute("allReportDetail", reportService.allCurrent());
    return "mainWindow";
  }

  @GetMapping( value = {"/login"} )
  public String getLogin() {
    return "login/login";
  }

  @GetMapping( value = {"/login/error10"} )
  public String getLogin10(Model model) {
    model.addAttribute("err", "You already entered wrong credential more than 10 times. \n Please meet the system" +
        " admin");
    return "login/login";
  }

  @GetMapping( value = {"/login/noUser"} )
  public String getLoginNoUser(Model model) {
    model.addAttribute("err", "There is no user according to the user name. \n Please try again !!");
    return "login/login";
  }

  @GetMapping( value = {"/unicodeTamil"} )
  public String getUnicodeTamil() {
    return "fragments/unicodeTamil";
  }

  @GetMapping( value = {"/unicodeSinhala"} )
  public String getUnicodeSinhala() {
    return "fragments/unicodeSinhala";
  }
}