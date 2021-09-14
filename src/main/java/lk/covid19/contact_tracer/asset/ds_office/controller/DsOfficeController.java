package lk.covid19.contact_tracer.asset.ds_office.controller;


import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.Province;
import lk.covid19.contact_tracer.asset.district.controller.DistrictController;
import lk.covid19.contact_tracer.asset.district.entity.District;
import lk.covid19.contact_tracer.asset.district.service.DistrictService;
import lk.covid19.contact_tracer.asset.ds_office.entity.DsOffice;
import lk.covid19.contact_tracer.asset.ds_office.service.DsOfficeService;
import lk.covid19.contact_tracer.asset.grama_niladhari.service.GramaNiladhariService;
import lk.covid19.contact_tracer.asset.person.entity.Person;
import lk.covid19.contact_tracer.util.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping( "/dsOffice" )
@RequiredArgsConstructor
public class DsOfficeController {

  private final DsOfficeService dsOfficeService;
  private final DistrictService districtService;
  private final GramaNiladhariService gramaNiladhariService;
  private final CommonService commonService;


  private String commonThing(Model model, Boolean booleanValue, DsOffice dsOfficeObject) {
    model.addAttribute("provinces", Province.values());
    model.addAttribute("addStatus", booleanValue);
    model.addAttribute("dsOffice", dsOfficeObject);
    model.addAttribute("districtURL",
                       MvcUriComponentsBuilder
                           .fromMethodName(DistrictController.class, "getDistrictByProvince", "")
                           .toUriString());
    return "dsOffice/addDsOffice";
  }

  @GetMapping
  public String findAll(Model model) {
    model.addAttribute("dsOffices", dsOfficeService.findAll());
    return "dsOffice/dsOffice";
  }

  @GetMapping( "/add" )
  public String form(Model model) {
    return commonThing(model, false, new DsOffice());
  }

  @GetMapping( "/{id}" )
  public String findById(@PathVariable Integer id, Model model) {
    DsOffice dsOffice = dsOfficeService.findById(id);
    model.addAttribute("dsOfficeDetail", dsOffice);
    List< Person > persons = new ArrayList<>();
    dsOffice.getGramaNiladharis().forEach(x -> persons.addAll(gramaNiladhariService.findById(x.getId()).getPersons()));
    model.addAttribute("attributeAndCounts", commonService.personsAccordingToType(persons));
    return "dsOffice/dsOffice-detail";
  }

  @GetMapping( "/edit/{id}" )
  public String edit(@PathVariable Integer id, Model model) {
    model.addAttribute("districts", districtService.findAll());
    return commonThing(model, true, dsOfficeService.findById(id));
  }

  @PostMapping( value = {"/save", "/update"} )
  public String persist(@Valid @ModelAttribute DsOffice dsOffice, BindingResult bindingResult,
                        RedirectAttributes redirectAttributes, Model model) {
    if ( bindingResult.hasErrors() ) {
      return commonThing(model, false, dsOffice);
    }
    redirectAttributes.addFlashAttribute("dsOfficeDetail", dsOfficeService.persist(dsOffice));
    return "redirect:/dsOffice";
  }

  @GetMapping( "/delete/{id}" )
  public String delete(@PathVariable Integer id, Model model) {
    dsOfficeService.delete(id);
    return "redirect:/dsOffice";
  }

  @GetMapping( value = "/getDsOffice/{id}" )
  @ResponseBody
  public MappingJacksonValue getDsOfficeByDistrict(@PathVariable Integer id) {
    District district = new District();
    district.setId(id);

    //MappingJacksonValue
    List< DsOffice > dsOffices = dsOfficeService.findByDistrict(district);

    //Create new mapping jackson value and set it to which was need to filter
    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(dsOffices);

    //simpleBeanPropertyFilter :-  need to give any id to addFilter method and created filter which was mentioned
    // what parameter's necessary to provide
    SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "name");
    //filters :-  set front end required value to before filter

    FilterProvider filters = new SimpleFilterProvider()
        .addFilter("DsOffice", simpleBeanPropertyFilter);
    //Employee :- need to annotate relevant class with JsonFilter  {@JsonFilter("Employee") }
    mappingJacksonValue.setFilters(filters);

    return mappingJacksonValue;
  }
}
