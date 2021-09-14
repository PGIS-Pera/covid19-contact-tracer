package lk.covid19.contact_tracer.asset.turn.controller;


import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lk.covid19.contact_tracer.asset.common_asset.model.Pager;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.Province;
import lk.covid19.contact_tracer.asset.district.controller.DistrictController;
import lk.covid19.contact_tracer.asset.district.service.DistrictService;
import lk.covid19.contact_tracer.asset.ds_office.controller.DsOfficeController;
import lk.covid19.contact_tracer.asset.ds_office.service.DsOfficeService;
import lk.covid19.contact_tracer.asset.turn.entity.Turn;
import lk.covid19.contact_tracer.asset.turn.service.TurnService;
import lk.covid19.contact_tracer.util.service.CommonService;
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
  private final DistrictService districtService;
  private final DsOfficeService dsOfficeService;
  private final CommonService commonService;

  private String commonThing(Model model, Boolean booleanValue, Turn turnObject) {
    model.addAttribute("addStatus", booleanValue);
    model.addAttribute("turn", turnObject);
    model.addAttribute("provinces", Province.values());
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
  public ModelAndView showPersonsPage(@RequestParam( "pageSize" ) Optional< Integer > pageSize,
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
    return modelAndView;
  }

  @GetMapping( "/add" )
  public String form(Model model) {
    return commonThing(model, false, new Turn());
  }

  @GetMapping( "/{id}" )
  public String findById(@PathVariable Integer id, Model model) {
    model.addAttribute("turnDetail", turnService.findById(id));
    return "turn/turn-detail";
  }

  @GetMapping( "/edit/{id}" )
  public String edit(@PathVariable Integer id, Model model) {
    model.addAttribute("districts", districtService.findAll());
    model.addAttribute("agOffices", dsOfficeService.findAll());
    return commonThing(model, true, turnService.findById(id));
  }

  @PostMapping( value = {"/save", "/update"} )
  public String persist(@Valid @ModelAttribute Turn turn, BindingResult bindingResult,
                        RedirectAttributes redirectAttributes, Model model) {
    if ( bindingResult.hasErrors() ) {
      return commonThing(model, false, turn);
    }
    redirectAttributes.addFlashAttribute("turnDetail", turnService.persist(turn));
    return "redirect:/turn";
  }

  @GetMapping( "/delete/{id}" )
  public String delete(@PathVariable Integer id) {
    turnService.delete(id);
    return "redirect:/turn";
  }

  @PostMapping( value = "/search" )
  @ResponseBody
  public MappingJacksonValue search(Turn turn) {

    List< Turn > turns = turnService.search(turn);
    turns.forEach(System.out::println);

    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(turns);

    SimpleBeanPropertyFilter simpleBeanPropertyFilterOne = SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "name", "number");

    SimpleBeanPropertyFilter simpleBeanPropertyFilterTwo = SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "name");

    FilterProvider filter = new SimpleFilterProvider()
        .addFilter("Turn", simpleBeanPropertyFilterOne)
        .addFilter("DsOffice", simpleBeanPropertyFilterTwo);
    mappingJacksonValue.setFilters(filter);

    return mappingJacksonValue;
  }

/*  @GetMapping( value = "/searchOne" )
  @ResponseBody
  public MappingJacksonValue searchOne(@RequestParam( "name" ) String name) {

    Turn turn = Turn.builder().name(name).build();
    List< Turn > turns = turnService.search(turn);

    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(turns);

    SimpleBeanPropertyFilter simpleBeanPropertyFilterOne = SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "name", "number");

    FilterProvider filter = new SimpleFilterProvider()
        .addFilter("Turn", simpleBeanPropertyFilterOne);

    mappingJacksonValue.setFilters(filter);

    return mappingJacksonValue;
  }*/
}
