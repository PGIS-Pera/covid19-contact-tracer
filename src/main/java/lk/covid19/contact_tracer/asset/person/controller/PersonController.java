package lk.covid19.contact_tracer.asset.person.controller;


import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lk.covid19.contact_tracer.asset.common_asset.model.Pager;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.Gender;
import lk.covid19.contact_tracer.asset.common_asset.model.TwoDate;
import lk.covid19.contact_tracer.asset.grama_niladhari.controller.GramaNiladhariController;
import lk.covid19.contact_tracer.asset.grama_niladhari.service.GramaNiladhariService;
import lk.covid19.contact_tracer.asset.location_interact.controller.LocationInteractController;
import lk.covid19.contact_tracer.asset.location_interact.entity.LocationInteract;
import lk.covid19.contact_tracer.asset.person.entity.Person;
import lk.covid19.contact_tracer.asset.person.entity.enums.PersonStatus;
import lk.covid19.contact_tracer.asset.person.service.PersonService;
import lk.covid19.contact_tracer.util.service.CommonService;
import lk.covid19.contact_tracer.util.service.DateTimeAgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


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
  private final GramaNiladhariService gramaNiladhariService;
  private final DateTimeAgeService dateTimeAgeService;

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
    model.addAttribute("personStatuses", PersonStatus.values());
    model.addAttribute("gramaNiladharis", gramaNiladhariService.findAll());
  }

  private String commonThings(Model model) {
    commonForAll(model);
    model.addAttribute("personNicSearchUrl",
                       MvcUriComponentsBuilder
                           .fromMethodName(PersonController.class, "findByNic", "")
                           .toUriString());
    model.addAttribute("gramaNiladhariSearchUrl", MvcUriComponentsBuilder
        .fromMethodName(GramaNiladhariController.class, "searchOne", "")
        .toUriString());
    return "person/addPerson";
  }

  @GetMapping( "/{id}" )
  public String personView(@PathVariable( "id" ) Integer id, Model model) {
    Person person = personService.findById(id);
    person.setAge(dateTimeAgeService.getDateDifference(person.getDateOfBirth(), LocalDate.now()));
    model.addAttribute("personDetail", person);
    model.addAttribute("addStatus", false);
    return "person/person-detail";
  }

  @GetMapping( "/edit/{id}" )
  public String editPersonForm(@PathVariable( "id" ) Integer id, Model model) {
    Person person = personService.findById(id);
    person.setAge(dateTimeAgeService.getDateDifference(person.getDateOfBirth(), LocalDate.now()));
    model.addAttribute("person", person);
    model.addAttribute("addStatus", false);
    return commonThings(model);
  }

  @GetMapping( "/add" )
  public String personAddForm(Model model) {
    model.addAttribute("addStatus", true);
    model.addAttribute("person", new Person());
    return commonThings(model);
  }

  @PostMapping( value = {"/save", "/update"} )
  public String addPerson(@Valid @ModelAttribute Person person, BindingResult result, Model model) {

    if ( result.hasErrors() ) {
      model.addAttribute("addStatus", true);
      model.addAttribute("person", person);
      return commonThings(model);
    }
    try {
      Person personDb = personService.persist(person);
      if ( person.getId() != null ) {
        return "redirect:/person";
      } else {
        return "redirect:/person/turn/" + personDb.getId();
      }
    } catch ( Exception e ) {
      ObjectError error = new ObjectError("person",
                                          "Please make sure that resolve following error \n. <br> System message -->" + e.getCause().getCause().getMessage());
      result.addError(error);
      model.addAttribute("addStatus", true);
      model.addAttribute("person", person);
      return commonThings(model);
    }
  }

  @GetMapping( "/turn/{id}" )
  public String addNewTurn(@PathVariable Integer id, Model model) {
    //todo need to manage this
    Person person = personService.findById(id);
    person.setAge(dateTimeAgeService.getDateDifference(person.getDateOfBirth(), LocalDate.now()));
    model.addAttribute("personDetail", person);
    model.addAttribute("gramaNiladhariSearchUrl", MvcUriComponentsBuilder
        .fromMethodName(GramaNiladhariController.class, "searchOne", "")
        .toUriString());
    model.addAttribute("locationInteractSearchUrl", MvcUriComponentsBuilder
        .fromMethodName(LocationInteractController.class, "search", new LocationInteract())
        .toUriString());
    model.addAttribute("locationInteractSaveUrl", MvcUriComponentsBuilder
        .fromMethodName(LocationInteractController.class, "turnNew", "", "")
        .toUriString());
    return "turn/newTurn";
  }

  @PostMapping( "/turn" )
  public String savePersonTurn(Person person) {
    personService.saveAndTurn(person);
    return "redirect:/person";
  }

  @GetMapping( "/remove/{id}" )
  public String removePerson(@PathVariable Integer id) {
    personService.delete(id);
    return "redirect:/person";
  }

  @GetMapping( "/search" )
  public String searchPage(Model model) {
    commonForAll(model);
    model.addAttribute("person", new Person());
    return "person/personSearch";
  }

  @PostMapping( "/searchDate" )
  public String searchDate(Model model, TwoDate twoDate) {
    String message = "This report is belong start at " + twoDate.getStartDate() + " end at " + twoDate.getEndDate();
    model.addAttribute("persons", personService.findByTurnIdentifiedDateRange(twoDate.getStartDate(),
                                                                              twoDate.getEndDate()));
    model.addAttribute("message", message);
    commonForAll(model);
    model.addAttribute("person", new Person());
    return "person/personSearch";
  }

  @PostMapping( value = "/search" )
  @ResponseBody
  public MappingJacksonValue search(Person person) {
    List< Person > persons = personService.search(person);
    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(persons);

    SimpleBeanPropertyFilter simpleBeanPropertyFilterOne = SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "name", "code", "nic", "mobile", "dateOfBirth");

    SimpleBeanPropertyFilter simpleBeanPropertyFilterTwo = SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "name");

    FilterProvider filter = new SimpleFilterProvider()
        .addFilter("Person", simpleBeanPropertyFilterOne)
        .addFilter("GramaNiladhari", simpleBeanPropertyFilterTwo);
    mappingJacksonValue.setFilters(filter);

    return mappingJacksonValue;
  }

  @GetMapping( "/getPerson/{nic}" )
  @ResponseBody
  public MappingJacksonValue findByNic(@RequestParam( "nic" ) String nic) {
    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(personService.findByNic(nic));

    SimpleBeanPropertyFilter simpleBeanPropertyFilterOne = SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "name", "code");
    SimpleBeanPropertyFilter simpleBeanPropertyFilterTwo = SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "name", "number");

    FilterProvider filter = new SimpleFilterProvider()
        .addFilter("Person", simpleBeanPropertyFilterOne)
        .addFilter("GramaNiladhari", simpleBeanPropertyFilterTwo);

    mappingJacksonValue.setFilters(filter);

    return mappingJacksonValue;
  }
}
