package lk.covid19.contact_tracer.asset.common_asset.controller;

import lk.covid19.contact_tracer.asset.common_asset.model.enums.Gender;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.Title;
import lk.covid19.contact_tracer.asset.role.entity.Role;
import lk.covid19.contact_tracer.asset.role.service.RoleService;
import lk.covid19.contact_tracer.asset.user.entity.User;
import lk.covid19.contact_tracer.asset.user.service.UserService;
import lk.covid19.contact_tracer.asset.user_details.entity.UserDetails;
import lk.covid19.contact_tracer.asset.user_details.service.UsersDetailsService;
import lk.covid19.contact_tracer.util.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ApplicationCreateController {
  private final RoleService roleService;
  private final UserService userService;
  private final UsersDetailsService usersDetailsService;
  private final CommonService commonService;


  @GetMapping( "/select/user" )
  public String saveUser(Model model) {
    //roles list start
    String[] roles = {"ADMIN", "HEALTH", "POLICE", "ARMY", "NAVY", "PRESIDENT", "COVID_CENTER"};
    for ( String s : roles ) {
      Role role = new Role();
      role.setRoleName(s);
      roleService.persist(role);
    }

//Employee
    UserDetails userDetails = new UserDetails();
    userDetails.setNumber("LKCC" + commonService.numberAutoGen(null).toString());
    userDetails.setName("Admin User");
    userDetails.setCallingName("Admin");
    userDetails.setName("908670000V");
    userDetails.setMobileOne("0750000000");
    userDetails.setTitle(Title.DR);
    userDetails.setGender(Gender.MALE);
    userDetails.setDateOfBirth(LocalDate.now().minusYears(18));
    UserDetails userDetailsDb = usersDetailsService.persist(userDetails);


    //admin user one
    User user = new User();
    user.setUserDetails(userDetailsDb);
    user.setUsername("admin");
    user.setPassword("admin");
    String message = "Username:- " + user.getUsername() + "\n Password:- " + user.getPassword();
    user.setEnabled(true);
    user.setRoles(roleService.findAll()
                      .stream()
                      .filter(role -> role.getRoleName().equals("ADMIN"))
                      .collect(Collectors.toList()));
    userService.persist(user);

    model.addAttribute("message", message);
    model.addAttribute("href", "login");
    model.addAttribute("btn_href", "login");
    return "message/message";
  }

  @GetMapping( "/select" )
  public String district(Model model) {
    model.addAttribute("message", "Successfully created province, district, divisional secretory and grmaniladhari " +
        "division");
    model.addAttribute("href", "select/user");
    model.addAttribute("btn_href", "Create New User");
    return "message/message";
  }

}
