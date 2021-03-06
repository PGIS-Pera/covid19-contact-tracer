package lk.covid19.contact_tracer.asset.turn.controller;


import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lk.covid19.contact_tracer.asset.common_asset.model.Pager;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.Province;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.StopActive;
import lk.covid19.contact_tracer.asset.district.controller.DistrictController;
import lk.covid19.contact_tracer.asset.ds_office.controller.DsOfficeController;
import lk.covid19.contact_tracer.asset.grama_niladhari.controller.GramaNiladhariController;
import lk.covid19.contact_tracer.asset.location_interact.controller.LocationInteractController;
import lk.covid19.contact_tracer.asset.person.entity.Person;
import lk.covid19.contact_tracer.asset.person.entity.enums.PersonStatus;
import lk.covid19.contact_tracer.asset.person.service.PersonService;
import lk.covid19.contact_tracer.asset.person_location_interact_time.service.PersonLocationInteractTimeService;
import lk.covid19.contact_tracer.asset.turn.entity.Turn;
import lk.covid19.contact_tracer.asset.turn.service.TurnService;
import lk.covid19.contact_tracer.util.service.DateTimeAgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping( "/turn" )
@RequiredArgsConstructor
public class TurnController {

  private static final int BUTTONS_TO_SHOW = 5;
  private static final int INITIAL_PAGE = 0;
  private static final int INITIAL_PAGE_SIZE = 5;
  private static final int[] PAGE_SIZES = {5, 10, 20};

  private final TurnService turnService;
  private final PersonService personService;
  private final DateTimeAgeService dateTimeAgeService;

  private String commonThing(Model model, Turn turnObject) {
    model.addAttribute("addStatus", false);
    model.addAttribute("turn", turnObject);
    model.addAttribute("provinces", Province.values());
    model.addAttribute("stopActive", StopActive.values());
    model.addAttribute("districtURL",
                       MvcUriComponentsBuilder
                           .fromMethodName(DistrictController.class, "getDistrictByProvince", "")
                           .toUriString());
    model.addAttribute("dsOfficeURL",
                       MvcUriComponentsBuilder
                           .fromMethodName(DsOfficeController.class, "getDsOfficeByDistrict", "")
                           .toUriString());
    return "turn/addTurn";
  }

  @GetMapping
  public ModelAndView showTurnsPage(@RequestParam( "pageSize" ) Optional< Integer > pageSize,
                                    @RequestParam( "page" ) Optional< Integer > page) {
    ModelAndView modelAndView = new ModelAndView("turn/turn");
    int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
    int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

    Page< Turn > turn = turnService.findAllPageable(PageRequest.of(evalPage,
                                                                   evalPageSize));
    Pager pager = new Pager(turn.getTotalPages(), turn.getNumber(), BUTTONS_TO_SHOW);
    modelAndView.addObject("turns", turn);
    modelAndView.addObject("selectedPageSize", evalPageSize);
    modelAndView.addObject("pageSizes", PAGE_SIZES);
    modelAndView.addObject("pager", pager);
    modelAndView.addObject("searchUrl", MvcUriComponentsBuilder
        .fromMethodName(TurnController.class, "search", new Turn())
        .toUriString());
    modelAndView.addObject("personStatuses", PersonStatus.values());
    modelAndView.addObject("personStatus", null);
    return modelAndView;
  }

  @GetMapping( "/ps/{personStatus}" )
  public String showTurnsPageByPersonStatus(@PathVariable( "personStatus" ) PersonStatus personStatus, Model model) {
    model.addAttribute("turns", turnService.findByPersonStatus(personStatus));
    model.addAttribute("personStatus", personStatus);
    model.addAttribute("searchUrl", MvcUriComponentsBuilder
        .fromMethodName(TurnController.class, "search", new Turn())
        .toUriString());
    model.addAttribute("personStatuses", PersonStatus.values());
    return "turn/allTurn";
  }

  @PostMapping( "/personStatusChange" )
  public String personStatusChange(@RequestParam( "id" ) Integer id,
                                   @RequestParam( "personStatus" ) PersonStatus personStatus) {
    Turn turn = turnService.findById(id);
    turn.setPersonStatus(personStatus);
    turnService.persistPersonStatue(turn);

    Person person = personService.findById(turn.getPerson().getId());
    person.setPersonStatus(personStatus);
    personService.persist(person);
    return "redirect:/turn";
  }


  @GetMapping( "/add" )
  public String form(Model model) {
    return commonThing(model, new Turn());
  }

  @GetMapping( "/{id}" )
  public String findById(@PathVariable Integer id, Model model) {
    Turn turn = turnService.findById(id);
    Person person = personService.findById(turn.getPerson().getId());
    person.setAge(dateTimeAgeService.getDateDifference(person.getDateOfBirth(), LocalDate.now()));
    model.addAttribute("personDetail", person);
    model.addAttribute("turnDetail", turn);
    return "turn/turn-detail";
  }

  @GetMapping( "/edit/{id}" )
  public String edit(@PathVariable Integer id, Model model) {
    model.addAttribute("addStatus", true);
    Turn turn = turnService.findById(id);
    Person person = personService.findById(turn.getPerson().getId());
    person.setAge(dateTimeAgeService.getDateDifference(person.getDateOfBirth(), LocalDate.now()));
    model.addAttribute("personDetail", person);
    model.addAttribute("turnDetail", turn);
    model.addAttribute("personLocationInteractTimes", turn.getPersonLocationInteractTimes());
    model.addAttribute("stopActives", StopActive.values());
    model.addAttribute("gramaNiladhariSearchUrl", MvcUriComponentsBuilder
        .fromMethodName(GramaNiladhariController.class, "searchOne", "")
        .toUriString());
    model.addAttribute("locationInteractSearchUrl", MvcUriComponentsBuilder
        .fromMethodName(LocationInteractController.class, "search", "")
        .toUriString());
    model.addAttribute("locationInteractSaveUrl", MvcUriComponentsBuilder
        .fromMethodName(LocationInteractController.class, "turnNew", "", "")
        .toUriString());
    return "turn/editTurn";
  }

  @PostMapping( value = {"/save", "/update"} )
  public String persist(@Valid @ModelAttribute Turn turn, BindingResult bindingResult, Model model) {
    if ( bindingResult.hasErrors() ) {
      return commonThing(model, turn);
    }
    turnService.persist(turn);
    return "redirect:/turn";
  }

  @GetMapping( "/remove/{id}" )
  public String delete(@PathVariable Integer id) {
    turnService.delete(id);
    return "redirect:/turn";
  }

  @PostMapping( value = "/search" )
  @ResponseBody
  public MappingJacksonValue search(Person person) {
    List< Turn > turns = turnService.findByPerson(person);

    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(turns);

    SimpleBeanPropertyFilter simpleBeanPropertyFilterOne = SimpleBeanPropertyFilter
        .filterOutAllExcept("name", "code", "gramaNiladhari");

    SimpleBeanPropertyFilter simpleBeanPropertyFilterTwo = SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "identifiedDate", "person");

    SimpleBeanPropertyFilter gramaNiladhari = SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "name", "number");

    FilterProvider filter = new SimpleFilterProvider()
        .addFilter("Person", simpleBeanPropertyFilterOne)
        .addFilter("Turn", simpleBeanPropertyFilterTwo)
        .addFilter("GramaNiladhari", gramaNiladhari);

    mappingJacksonValue.setFilters(filter);

    return mappingJacksonValue;
  }

}
