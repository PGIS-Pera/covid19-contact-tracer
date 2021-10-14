package lk.covid19.contact_tracer.asset.user.controller;

import lk.covid19.contact_tracer.asset.role.service.RoleService;
import lk.covid19.contact_tracer.asset.user.entity.User;
import lk.covid19.contact_tracer.asset.user.service.UserService;
import lk.covid19.contact_tracer.asset.user_details.entity.UserDetails;
import lk.covid19.contact_tracer.asset.user_details.service.UsersDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping( "/user" )
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;
  private final RoleService roleService;
  private final UsersDetailsService usersDetailsService;


  @GetMapping
  public String userPage(Model model) {
    model.addAttribute("users", userService.findAll());
    return "user/user";
  }

  @GetMapping( value = "/{id}" )
  public String userView(@PathVariable( "id" ) Integer id, Model model) {
    model.addAttribute("userDetail", userService.findById(id));
    return "user/user-detail";
  }

  private String commonCode(Model model) {
    model.addAttribute("employeeDetailShow", true);
    model.addAttribute("employeeNotFoundShow", false);
    model.addAttribute("roles", roleService.findAll());

    return "user/addUser";
  }

  @GetMapping( value = "/edit/{id}" )
  public String editUserFrom(@PathVariable( "id" ) Integer id, Model model) {
    model.addAttribute("user", userService.findById(id));
    model.addAttribute("addStatus", false);
    return commonCode(model);
  }

  @GetMapping( value = "/add" )
  public String userAddFrom(Model model) {
    model.addAttribute("addStatus", true);
    model.addAttribute("employeeDetailShow", false);
    model.addAttribute("employee", new UserDetails());
    return "user/addUser";
  }

  //Send a searched employee to add working place
  @PostMapping( value = "/workingPlace" )
  public String addUserEmployeeDetails(@ModelAttribute( "userDetails" ) UserDetails userDetails, Model model) {

    List< UserDetails > userDetailList = usersDetailsService.search(userDetails)
        .stream()
        .filter(userService::findByEmployee)
        .collect(Collectors.toList());

    if ( userDetailList.size() == 1 ) {
      model.addAttribute("user", new User());
      model.addAttribute("userDetailses", userDetailList.get(0));
      model.addAttribute("addStatus", true);
      return commonCode(model);
    }
    model.addAttribute("addStatus", true);
    model.addAttribute("userDetailses", new UserDetails());
    model.addAttribute("employeeDetailShow", false);
    model.addAttribute("employeeNotFoundShow", true);
    model.addAttribute("employeeNotFound", "There is not employee in the system according to the provided details" +
        " or that employee already be a user in the system" +
        " \n Could you please search again !!");

    return "user/addUser";
  }

  // Above method support to send data to front end - All List, update, edit
  //Bellow method support to do back end function save, delete, update, search

  @PostMapping( value = {"/add", "/update"} )
  public String addUser(@Valid @ModelAttribute User user, BindingResult result, Model model,
                        HttpServletRequest request) {

    if ( userService.findUserByEmployee(user.getUserDetails()) != null && !request.getRequestURI().equals("/user/update") ) {
      ObjectError error = new ObjectError("userDetails", "This user already defined as a user");
      result.addError(error);
    }
    if ( result.hasErrors() ) {
      model.addAttribute("addStatus", false);
      model.addAttribute("user", user);
      return commonCode(model);
    }
    if ( user.getId() != null ) {
      User dbUser = userService.findById(user.getId());
      dbUser.setEnabled(true);
      dbUser.setPassword(user.getPassword());
      dbUser.setUserDetails(user.getUserDetails());
      dbUser.setRoles(user.getRoles());
      userService.persist(dbUser);
      return "redirect:/user";
    }

    user.setRoles(user.getRoles());
    user.setEnabled(true);
    userService.persist(user);
    return "redirect:/user";
  }


  @GetMapping( value = "/remove/{id}" )
  public String removeUser(@PathVariable Integer id) {
    //     user can not be deleted but status was set to blocks
    userService.delete(id);
    return "redirect:/user";
  }

  @GetMapping( value = "/search" )
  public String search(Model model, User user) {
    model.addAttribute("userDetail", userService.search(user));
    return "user/user-detail";
  }
}
