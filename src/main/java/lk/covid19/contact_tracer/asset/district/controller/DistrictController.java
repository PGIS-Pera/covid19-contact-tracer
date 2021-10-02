package lk.covid19.contact_tracer.asset.district.controller;


import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.Province;
import lk.covid19.contact_tracer.asset.district.entity.District;
import lk.covid19.contact_tracer.asset.district.service.DistrictService;
import lk.covid19.contact_tracer.asset.ds_office.service.DsOfficeService;
import lk.covid19.contact_tracer.asset.grama_niladhari.service.GramaNiladhariService;
import lk.covid19.contact_tracer.asset.person.entity.Person;
import lk.covid19.contact_tracer.util.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping( "/district" )
@RequiredArgsConstructor
public class DistrictController {

  private final DistrictService districtService;
  private final DsOfficeService dsOfficeService;
  private final GramaNiladhariService gramaNiladhariService;
  private final CommonService commonService;

  private String commonThing(Model model, Boolean booleanValue, District districtObject) {
    model.addAttribute("provinces", Province.values());
    model.addAttribute("addStatus", booleanValue);
    model.addAttribute("district", districtObject);
    return "district/addDistrict";
  }

  @GetMapping
  public String findAll(Model model) {
    model.addAttribute("districts", districtService.findAll());
    return "district/district";
  }

  @GetMapping( "/add" )
  public String form(Model model) {
    return commonThing(model, false, new District());
  }

  @GetMapping( "/{id}" )
  public String findById(@PathVariable Integer id, Model model) {
    District district = districtService.findById(id);
    model.addAttribute("districtDetail", district);
    List< Person > persons = new ArrayList<>();
    district.getDsOffices().forEach(x -> dsOfficeService.findById(x.getId()).getGramaNiladharis().forEach(y -> persons.addAll(gramaNiladhariService.findById(y.getId()).getPersons())));
    model.addAttribute("attributeAndCounts", commonService.personsAccordingToType(persons));
    return "district/district-detail";
  }

  @GetMapping( "/edit/{id}" )
  public String edit(@PathVariable Integer id, Model model) {
    return commonThing(model, true, districtService.findById(id));
  }

  @PostMapping( value = {"/save", "/update"} )
  public String persist(@Valid @ModelAttribute District district, BindingResult bindingResult,
                        RedirectAttributes redirectAttributes, Model model) {
    if ( bindingResult.hasErrors() ) {
      return commonThing(model, false, district);
    }
    try {
      redirectAttributes.addFlashAttribute("districtDetail", districtService.persist(district));
    } catch ( Exception e ) {
      ObjectError error = new ObjectError("district",
                                          "Please make sure that resolve following error \n. System message -->" + e.getCause().getCause().getMessage());
      bindingResult.addError(error);
      return commonThing(model, false, district);
    }
    return "redirect:/district";
  }

  @GetMapping( "/remove/{id}" )
  public String delete(@PathVariable Integer id, Model model) {
    districtService.delete(id);
    return "redirect:/district";
  }

  @GetMapping( value = "/getDistrict/{province}" )
  @ResponseBody
  public MappingJacksonValue getDistrictByProvince(@PathVariable String province) {

    List< District > districts = districtService.findByProvince(Province.valueOf(province));

    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(districts);

    SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "name");
    FilterProvider filters = new SimpleFilterProvider()
        .addFilter("District", simpleBeanPropertyFilter);
    mappingJacksonValue.setFilters(filters);

    return mappingJacksonValue;
  }

}
