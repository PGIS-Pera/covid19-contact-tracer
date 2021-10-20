package lk.covid19.contact_tracer.asset.user_details.controller;


import lk.covid19.contact_tracer.asset.common_asset.model.enums.Gender;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.Title;
import lk.covid19.contact_tracer.asset.report.service.ReportService;
import lk.covid19.contact_tracer.asset.user.entity.User;
import lk.covid19.contact_tracer.asset.user.service.UserService;
import lk.covid19.contact_tracer.asset.user_details.entity.UserDetails;
import lk.covid19.contact_tracer.asset.user_details_files.entity.UserDetailsFiles;
import lk.covid19.contact_tracer.asset.user_details_files.service.UserDetailsFilesService;
import lk.covid19.contact_tracer.asset.user_details.service.UsersDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequestMapping( "/userDetails" )
@RequiredArgsConstructor
public class UserDetailsController {
  private final UsersDetailsService usersDetailsService;
  private final UserDetailsFilesService userDetailsFilesService;
  private final ReportService reportService;
  private final UserService userService;

  // Common things for an userDetails add and update
  private String commonThings(Model model) {
    model.addAttribute("title", Title.values());
    model.addAttribute("gender", Gender.values());
    return "userDetails/addUserDetails";
  }

  //When scr called file will send to
  @GetMapping( "/file/{filename}" )
  public ResponseEntity< byte[] > downloadFile(@PathVariable( "filename" ) String filename) {
    UserDetailsFiles file = userDetailsFilesService.findByNewID(filename);
    return ResponseEntity
        .ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
        .body(file.getPic());
  }

  //Send all userDetails data
  @RequestMapping
  public String findAll(Model model) {
    model.addAttribute("userDetailses", usersDetailsService.findAll());
    model.addAttribute("contendHeader", "User Details Registration");
    return "userDetails/userDetails";
  }

  //Send on userDetails details
  @GetMapping( value = "/{id}" )
  public String view(@PathVariable( "id" ) Integer id, Model model) {
    UserDetails userDetails = usersDetailsService.findById(id);
    model.addAttribute("userDetail", userDetails);
    model.addAttribute("file", userDetailsFilesService.userDetailsFileDownloadLinks(userDetails));
    User user = userService.findById(userDetails.getUser().getId());
    if ( user != null ) {
      model.addAttribute("userDetailsReport", reportService.userVsReport(user.getName()));
    } else {
      model.addAttribute("userDetailsReport", null);
    }

    return "userDetails/userDetails-detail";
  }

  //Send userDetails data edit
  @GetMapping( value = "/edit/{id}" )
  public String editForm(@PathVariable( "id" ) Integer id, Model model) {
    UserDetails userDetails = usersDetailsService.findById(id);
    model.addAttribute("userDetails", userDetails);
    model.addAttribute("addStatus", false);
    model.addAttribute("file", userDetailsFilesService.userDetailsFileDownloadLinks(userDetails));
    return commonThings(model);
  }

  //Send an userDetails add form
  @GetMapping( value = {"/add"} )
  public String addForm(Model model) {
    model.addAttribute("addStatus", true);
    model.addAttribute("userDetails", new UserDetails());
    return commonThings(model);
  }

  //Employee add and update
  @PostMapping( value = {"/save", "/update"} )
  public String add(@Valid @ModelAttribute UserDetails userDetails, BindingResult result, Model model
                   ) {
    if ( result.hasErrors() ) {
      model.addAttribute("addStatus", true);
      model.addAttribute("userDetails", userDetails);
      return commonThings(model);
    }
    //after save userDetails files and save userDetails
    UserDetails userDetailsDb = usersDetailsService.persist(userDetails);

    try {
      //save userDetails images file
      if ( userDetails.getFile() != null ) {
        UserDetailsFiles userDetailsFiles =
            userDetailsFilesService.findByUserDetails(userDetailsDb);
        if ( userDetailsFiles != null ) {
          // update new contents
          userDetailsFiles.setPic(userDetails.getFile().getBytes());
          // Save all to database
        } else {
          userDetailsFiles = new UserDetailsFiles(userDetails.getFile().getOriginalFilename(),
                                                  userDetails.getFile().getContentType(),
                                                  userDetails.getFile().getBytes(),
                                                  userDetails.getNic().concat("-" + LocalDateTime.now()),
                                                  UUID.randomUUID().toString().concat("userDetails"));
          userDetailsFiles.setUserDetails(userDetails);
        }
        userDetailsFilesService.persist(userDetailsFiles);
      }
      return "redirect:/userDetails";

    } catch ( Exception e ) {
      ObjectError error = new ObjectError("userDetails",
                                          "There is already in the system. \n Error happened because of Image. \n " +
                                              "System message -->" + e);
      result.addError(error);
      model.addAttribute("addStatus", true);
      model.addAttribute("userDetails", userDetails);
      return commonThings(model);
    }
  }

  @GetMapping( value = "/remove/{id}" )
  public String remove(@PathVariable Integer id) {
    usersDetailsService.delete(id);
    return "redirect:/userDetails";
  }

  //To search userDetails any giving userDetails parameter
  @GetMapping( value = "/search" )
  public String search(Model model, UserDetails userDetails) {
    model.addAttribute("userDetails", usersDetailsService.search(userDetails));
    return "userDetails/userDetails-detail";
  }

}
