package lk.covid19.contact_tracer.asset.person.controller;


import lk.covid19.contact_tracer.asset.attempt.entity.Attempt;
import lk.covid19.contact_tracer.asset.common_asset.model.Pager;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.Gender;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.TwoDate;
import lk.covid19.contact_tracer.asset.person.entity.Person;
import lk.covid19.contact_tracer.asset.person.entity.enums.PersonStatus;
import lk.covid19.contact_tracer.asset.person.service.PersonService;
import lk.covid19.contact_tracer.util.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
@RequestMapping( "/person" )
@RequiredArgsConstructor
public class PersonController {

  private static final int BUTTONS_TO_SHOW = 5;
  private static final int INITIAL_PAGE = 0;
  private static final int INITIAL_PAGE_SIZE = 5;
  private static final int[] PAGE_SIZES = {5, 10, 20};

  private final PersonService personService;
  private final CommonService commonService;

  @GetMapping
  public ModelAndView showPersonsPage(@RequestParam( "pageSize" ) Optional< Integer > pageSize,
                                      @RequestParam( "page" ) Optional< Integer > page) {
    ModelAndView modelAndView = new ModelAndView("person/person");
    int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
    int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

    Page< Person > persons = personService.findAllPageable(PageRequest.of(evalPage, evalPageSize));
    Pager pager = new Pager(persons.getTotalPages(), persons.getNumber(), BUTTONS_TO_SHOW);

    modelAndView.addObject("persons", persons);
    modelAndView.addObject("selectedPageSize", evalPageSize);
    modelAndView.addObject("pageSizes", PAGE_SIZES);
    modelAndView.addObject("pager", pager);
    modelAndView.addObject("searchUrl", MvcUriComponentsBuilder
        .fromMethodName(PersonController.class, "search", new Person())
        .toUriString());
    return modelAndView;
  }

  private void commonForAll(Model model) {
    model.addAttribute("gender", Gender.values());
    model.addAttribute("deadLives", PersonStatus.values());
  }

  private String commonThings(Model model) {
    commonForAll(model);
    model.addAttribute("patientNic",
                       MvcUriComponentsBuilder
                           .fromMethodName(PersonController.class, "findByNic", "")
                           .toUriString());
    return "person/addPatient";
  }

  @GetMapping( "/{id}" )
  public String patientView(@PathVariable( "id" ) Integer id, Model model) {
    Person person = personService.findById(id);
    model.addAttribute("patientDetail", person);
    model.addAttribute("addStatus", false);
    return "person/person-detail";
  }

  @GetMapping( "/edit/{id}" )
  public String editPatientForm(@PathVariable( "id" ) Integer id, Model model) {
    Person person = personService.findById(id);
    model.addAttribute("person", person);
    model.addAttribute("addStatus", false);
    return commonThings(model);
  }

  @GetMapping( "/add" )
  public String patientAddForm(Model model) {
    model.addAttribute("addStatus", true);
    model.addAttribute("person", new Person());
    return commonThings(model);
  }

  @PostMapping( value = {"/save", "/update"} )
  public String addPatient(@Valid @ModelAttribute Person person, BindingResult result, Model model) {

    if ( result.hasErrors() ) {
      model.addAttribute("addStatus", true);
      model.addAttribute("person", person);
      return commonThings(model);
    }
    try {
      personService.persist(person);
    } catch ( Exception e ) {
      ObjectError error = new ObjectError("person",
                                          "Please make sure that resolve following error \n. <br> System message -->" + e.getCause().getCause().getMessage());
      result.addError(error);
      model.addAttribute("addStatus", true);
      model.addAttribute("person", person);
      return commonThings(model);
    }


    return "redirect:/person";
  }

  @GetMapping( "/remove/{id}" )
  public String removePatient(@PathVariable Integer id) {
    personService.delete(id);
    return "redirect:/person";
  }

  @GetMapping( "/getPatient/{nic}" )
  @ResponseBody
  public Person findByNic(@PathVariable String nic) {
    return personService.findByNic(nic);
  }

  @GetMapping( "/search" )
  public String search(Model model) {
    commonForAll(model);
    model.addAttribute("person", new Person());
    return "person/patientSearch";
  }


  @PostMapping( "/searchDate" )
  public String searchDate(Model model, TwoDate twoDate) {
    String message = "This report is belong start at " + twoDate.getStartDate() + " end at " + twoDate.getEndDate();
    model.addAttribute("persons", personService.findByAttemptIdentifiedDateRange(twoDate.getStartDate(),
                                                                                 twoDate.getEndDate()));
    model.addAttribute("message", message);
    commonForAll(model);
    model.addAttribute("person", new Person());
    return "person/patientSearch";
  }

  @PostMapping(value = "/search")
  @ResponseBody
  public List< Person > search( Person person) {
    return personService.search(person);
  }

}
