package lk.covid19.contact_tracer.asset.location_interact.controller;


import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lk.covid19.contact_tracer.asset.common_asset.model.Pager;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.Province;
import lk.covid19.contact_tracer.asset.district.controller.DistrictController;
import lk.covid19.contact_tracer.asset.ds_office.controller.DsOfficeController;
import lk.covid19.contact_tracer.asset.grama_niladhari.controller.GramaNiladhariController;
import lk.covid19.contact_tracer.asset.grama_niladhari.service.GramaNiladhariService;
import lk.covid19.contact_tracer.asset.location_interact.entity.LocationInteract;
import lk.covid19.contact_tracer.asset.location_interact.service.LocationInteractService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping( "/locationInteract" )
@RequiredArgsConstructor
public class LocationInteractController {


  private static final int BUTTONS_TO_SHOW = 5;
  private static final int INITIAL_PAGE = 0;
  private static final int INITIAL_PAGE_SIZE = 5;
  private static final int[] PAGE_SIZES = {5, 10, 20};
  private final LocationInteractService locationInteractService;
  private final GramaNiladhariService gramaNiladhariService;

  private String commonThing(Model model, Boolean booleanValue, LocationInteract locationInteractObject) {
    model.addAttribute("addStatus", booleanValue);
    model.addAttribute("locationInteract", locationInteractObject);
    model.addAttribute("gramaNiladhariSearchUrl", MvcUriComponentsBuilder
        .fromMethodName(GramaNiladhariController.class, "searchOne", "")
        .toUriString());
    return "locationInteract/addLocationInteract";
  }

  @GetMapping
  public ModelAndView showPersonsPage(@RequestParam( "pageSize" ) Optional< Integer > pageSize,
                                      @RequestParam( "page" ) Optional< Integer > page) {
    ModelAndView modelAndView = new ModelAndView("locationInteract/locationInteract");
    int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
    int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

    Page< LocationInteract > locationInteract = locationInteractService.findAllPageable(PageRequest.of(evalPage,
                                                                                                       evalPageSize));
    Pager pager = new Pager(locationInteract.getTotalPages(), locationInteract.getNumber(), BUTTONS_TO_SHOW);
    modelAndView.addObject("locationInteracts", locationInteract);
    modelAndView.addObject("selectedPageSize", evalPageSize);
    modelAndView.addObject("pageSizes", PAGE_SIZES);
    modelAndView.addObject("pager", pager);
    modelAndView.addObject("searchUrl", MvcUriComponentsBuilder
        .fromMethodName(LocationInteractController.class, "search", new LocationInteract())
        .toUriString());
    return modelAndView;
  }

  @GetMapping( "/add" )
  public String form(Model model) {
    return commonThing(model, true, new LocationInteract());
  }

  @GetMapping( "/{id}" )
  public String findById(@PathVariable Integer id, Model model) {
    model.addAttribute("locationInteractDetail", locationInteractService.findById(id));
    return "locationInteract/locationInteract-detail";
  }

  @GetMapping( "/edit/{id}" )
  public String edit(@PathVariable Integer id, Model model) {
    return commonThing(model, false, locationInteractService.findById(id));
  }

  @PostMapping( value = {"/save", "/update"} )
  public String persist(@Valid @ModelAttribute LocationInteract locationInteract, BindingResult bindingResult,
                        RedirectAttributes redirectAttributes, Model model) {
    if ( bindingResult.hasErrors() ) {
      return commonThing(model, false, locationInteract);
    }
    redirectAttributes.addFlashAttribute("locationInteractDetail", locationInteractService.persist(locationInteract));
    return "redirect:/locationInteract";
  }

  @GetMapping( "/delete/{id}" )
  public String delete(@PathVariable Integer id, Model model) {
    locationInteractService.delete(id);
    return "redirect:/locationInteract";
  }

  @PostMapping( value = "/search" )
  @ResponseBody
  public MappingJacksonValue search(@RequestParam( "name" ) String name) {
    LocationInteract locationInteract = new LocationInteract();
    locationInteract.setName(name);

    List< LocationInteract > locationInteracts = locationInteractService.search(locationInteract);
    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(locationInteracts);

    return getMappingJacksonValue(mappingJacksonValue);
  }

  @PostMapping( value = "/attempt/new" )
  @ResponseBody
  public MappingJacksonValue turnNew(@RequestParam( "name" ) String name, @RequestParam( "id" ) Integer id) {
    LocationInteract locationInteract = new LocationInteract();
    locationInteract.setName(name);
    locationInteract.setGramaNiladhari(gramaNiladhariService.findById(id));

    LocationInteract locationInteractDb = locationInteractService.findByGramaNiladhariAndName(locationInteract);

    MappingJacksonValue mappingJacksonValue;
    if ( locationInteractDb != null ) {
      mappingJacksonValue = new MappingJacksonValue(locationInteractDb);
    } else {
      mappingJacksonValue =
          new MappingJacksonValue(locationInteractService.persist(locationInteract));
    }
    return getMappingJacksonValue(mappingJacksonValue);
  }

  private MappingJacksonValue getMappingJacksonValue(MappingJacksonValue mappingJacksonValue) {
    SimpleBeanPropertyFilter simpleBeanPropertyFilterOne = SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "name", "number");

    SimpleBeanPropertyFilter simpleBeanPropertyFilterTwo = SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "name");

    FilterProvider filter = new SimpleFilterProvider()
        .addFilter("LocationInteract", simpleBeanPropertyFilterTwo)
        .addFilter("GramaNiladhari", simpleBeanPropertyFilterOne)
        .addFilter("DsOffice", simpleBeanPropertyFilterTwo)
        .addFilter("District", simpleBeanPropertyFilterTwo);
    mappingJacksonValue.setFilters(filter);

    return mappingJacksonValue;
  }
}
