package lk.covid19.contact_tracer.asset.treatment_center.controller;


import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lk.covid19.contact_tracer.asset.common_asset.model.Pager;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.Province;
import lk.covid19.contact_tracer.asset.district.controller.DistrictController;
import lk.covid19.contact_tracer.asset.district.service.DistrictService;
import lk.covid19.contact_tracer.asset.ds_office.controller.DsOfficeController;
import lk.covid19.contact_tracer.asset.ds_office.service.DsOfficeService;
import lk.covid19.contact_tracer.asset.treatment_center.entity.TreatmentCenter;
import lk.covid19.contact_tracer.asset.treatment_center.service.TreatmentCenterService;
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
@RequestMapping( "/quarantineCenter" )
@RequiredArgsConstructor
public class TreatmentCenterController {
// do not mind this controller this all would have to be changed when you create new

  private static final int BUTTONS_TO_SHOW = 5;
  private static final int INITIAL_PAGE = 0;
  private static final int INITIAL_PAGE_SIZE = 5;
  private static final int[] PAGE_SIZES = {5, 10, 20};
  private final TreatmentCenterService treatmentCenterService;
  private final DistrictService districtService;
  private final DsOfficeService dsOfficeService;
  private final CommonService commonService;

  private String commonThing(Model model, Boolean booleanValue, TreatmentCenter treatmentCenterObject) {
    model.addAttribute("addStatus", booleanValue);
    model.addAttribute("quarantineCenter", treatmentCenterObject);
    model.addAttribute("provinces", Province.values());
    model.addAttribute("districtURL",
                       MvcUriComponentsBuilder
                           .fromMethodName(DistrictController.class, "getDistrictByProvince", "")
                           .toUriString());
    model.addAttribute("dsOfficeURL",
                       MvcUriComponentsBuilder
                           .fromMethodName(DsOfficeController.class, "getDsOfficeByDistrict", "")
                           .toUriString());
    return "quarantineCenter/addQuarantineCenter";
  }

  @GetMapping
  public ModelAndView showPersonsPage(@RequestParam( "pageSize" ) Optional< Integer > pageSize,
                                      @RequestParam( "page" ) Optional< Integer > page) {
    ModelAndView modelAndView = new ModelAndView("quarantineCenter/quarantineCenter");
    int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
    int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

    Page< TreatmentCenter > quarantineCenter = treatmentCenterService.findAllPageable(PageRequest.of(evalPage,
                                                                                                     evalPageSize));
    Pager pager = new Pager(quarantineCenter.getTotalPages(), quarantineCenter.getNumber(), BUTTONS_TO_SHOW);
    modelAndView.addObject("quarantineCenters", quarantineCenter);
    modelAndView.addObject("selectedPageSize", evalPageSize);
    modelAndView.addObject("pageSizes", PAGE_SIZES);
    modelAndView.addObject("pager", pager);
    modelAndView.addObject("searchUrl", MvcUriComponentsBuilder
        .fromMethodName(TreatmentCenterController.class, "search", new TreatmentCenter())
        .toUriString());
    return modelAndView;
  }

  @GetMapping( "/add" )
  public String form(Model model) {
    return commonThing(model, false, new TreatmentCenter());
  }

  @GetMapping( "/{id}" )
  public String findById(@PathVariable Integer id, Model model) {
    TreatmentCenter treatmentCenter = treatmentCenterService.findById(id);
    model.addAttribute("quarantineCenterDetail", treatmentCenter);
    // model.addAttribute("attributeAndCounts", commonService.personsAccordingToType(quarantineCenter.getPersons()));
    return "quarantineCenter/quarantineCenter-detail";
  }

  @GetMapping( "/edit/{id}" )
  public String edit(@PathVariable Integer id, Model model) {
    model.addAttribute("districts", districtService.findAll());
    model.addAttribute("agOffices", dsOfficeService.findAll());
    return commonThing(model, true, treatmentCenterService.findById(id));
  }

  @PostMapping( value = {"/save", "/update"} )
  public String persist(@Valid @ModelAttribute TreatmentCenter treatmentCenter, BindingResult bindingResult,
                        RedirectAttributes redirectAttributes, Model model) {
    if ( bindingResult.hasErrors() ) {
      return commonThing(model, false, treatmentCenter);
    }
    redirectAttributes.addFlashAttribute("quarantineCenterDetail", treatmentCenterService.persist(treatmentCenter));
    return "redirect:/quarantineCenter";
  }

  @GetMapping( "/remove/{id}" )
  public String delete(@PathVariable Integer id, Model model) {
    treatmentCenterService.delete(id);
    return "redirect:/quarantineCenter";
  }

  @PostMapping( value = "/search" )
  @ResponseBody
  public MappingJacksonValue search(TreatmentCenter treatmentCenter) {
    System.out.println(treatmentCenter.toString());
    List< TreatmentCenter > treatmentCenters = treatmentCenterService.search(treatmentCenter);
    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(treatmentCenters);

    SimpleBeanPropertyFilter simpleBeanPropertyFilterOne = SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "name", "number", "dsOffice");

    SimpleBeanPropertyFilter simpleBeanPropertyFilterTwo = SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "name");

    FilterProvider filter = new SimpleFilterProvider()
        .addFilter("GramaNiladhari", simpleBeanPropertyFilterOne)
        .addFilter("DsOffice", simpleBeanPropertyFilterTwo);
    mappingJacksonValue.setFilters(filter);

    return mappingJacksonValue;
  }

  @GetMapping( value = "/searchOne" )
  @ResponseBody
  public MappingJacksonValue searchOne(@RequestParam( "name" ) String name) {

    TreatmentCenter treatmentCenter = TreatmentCenter.builder().name(name).build();
    List< TreatmentCenter > treatmentCenters = treatmentCenterService.search(treatmentCenter);

    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(treatmentCenters);

    SimpleBeanPropertyFilter simpleBeanPropertyFilterOne = SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "name", "number");

    FilterProvider filter = new SimpleFilterProvider()
        .addFilter("GramaNiladhari", simpleBeanPropertyFilterOne);

    mappingJacksonValue.setFilters(filter);

    return mappingJacksonValue;
  }
  //todo : according to gramaniladari division vs person count[ according to person type]

}
