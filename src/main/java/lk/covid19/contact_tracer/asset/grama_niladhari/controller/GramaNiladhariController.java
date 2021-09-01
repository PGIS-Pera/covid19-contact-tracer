package lk.covid19.contact_tracer.asset.grama_niladhari.controller;


import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lk.covid19.contact_tracer.asset.common_asset.model.Pager;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.Province;
import lk.covid19.contact_tracer.asset.district.controller.DistrictController;
import lk.covid19.contact_tracer.asset.district.service.DistrictService;
import lk.covid19.contact_tracer.asset.ds_office.controller.DsOfficeController;
import lk.covid19.contact_tracer.asset.ds_office.service.DsOfficeService;
import lk.covid19.contact_tracer.asset.grama_niladhari.entity.GramaNiladhari;
import lk.covid19.contact_tracer.asset.grama_niladhari.service.GramaNiladhariService;
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
@RequestMapping( "/gramaNiladhari" )
@RequiredArgsConstructor
public class GramaNiladhariController {

  private static final int BUTTONS_TO_SHOW = 5;
  private static final int INITIAL_PAGE = 0;
  private static final int INITIAL_PAGE_SIZE = 5;
  private static final int[] PAGE_SIZES = {5, 10, 20};
  private final GramaNiladhariService gramaNiladhariService;
  private final DistrictService districtService;
  private final DsOfficeService dsOfficeService;

  private String commonThing(Model model, Boolean booleanValue, GramaNiladhari gramaNiladhariObject) {
    model.addAttribute("addStatus", booleanValue);
    model.addAttribute("gramaNiladhari", gramaNiladhariObject);
    model.addAttribute("provinces", Province.values());
    model.addAttribute("districtURL",
                       MvcUriComponentsBuilder
                           .fromMethodName(DistrictController.class, "getDistrictByProvince", "")
                           .toUriString());
    model.addAttribute("dsOfficeURL",
                       MvcUriComponentsBuilder
                           .fromMethodName(DsOfficeController.class, "getDsOfficeByDistrict", "")
                           .toUriString());
    return "gramaNiladhari/addGramaNiladhari";
  }

  @GetMapping
  public ModelAndView showPersonsPage(@RequestParam( "pageSize" ) Optional< Integer > pageSize,
                                      @RequestParam( "page" ) Optional< Integer > page) {
    ModelAndView modelAndView = new ModelAndView("gramaNiladhari/gramaNiladhari");
    int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
    int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

    Page< GramaNiladhari > gramaNiladhari = gramaNiladhariService.findAllPageable(PageRequest.of(evalPage,
                                                                                                 evalPageSize));
    Pager pager = new Pager(gramaNiladhari.getTotalPages(), gramaNiladhari.getNumber(), BUTTONS_TO_SHOW);
    modelAndView.addObject("gramaNiladharis", gramaNiladhari);
    modelAndView.addObject("selectedPageSize", evalPageSize);
    modelAndView.addObject("pageSizes", PAGE_SIZES);
    modelAndView.addObject("pager", pager);
    modelAndView.addObject("searchUrl", MvcUriComponentsBuilder
        .fromMethodName(GramaNiladhariController.class, "search", new GramaNiladhari())
        .toUriString());
    return modelAndView;
  }

  @GetMapping( "/add" )
  public String form(Model model) {
    return commonThing(model, false, new GramaNiladhari());
  }

  @GetMapping( "/{id}" )
  public String findById(@PathVariable Integer id, Model model) {
    model.addAttribute("gramaNiladhariDetail", gramaNiladhariService.findById(id));
    return "gramaNiladhari/gramaNiladhari-detail";
  }

  @GetMapping( "/edit/{id}" )
  public String edit(@PathVariable Integer id, Model model) {
    model.addAttribute("districts", districtService.findAll());
    model.addAttribute("agOffices", dsOfficeService.findAll());
    return commonThing(model, true, gramaNiladhariService.findById(id));
  }

  @PostMapping( value = {"/save", "/update"} )
  public String persist(@Valid @ModelAttribute GramaNiladhari gramaNiladhari, BindingResult bindingResult,
                        RedirectAttributes redirectAttributes, Model model) {
    if ( bindingResult.hasErrors() ) {
      return commonThing(model, false, gramaNiladhari);
    }
    redirectAttributes.addFlashAttribute("gramaNiladhariDetail", gramaNiladhariService.persist(gramaNiladhari));
    return "redirect:/gramaNiladhari";
  }

  @GetMapping( "/delete/{id}" )
  public String delete(@PathVariable Integer id, Model model) {
    gramaNiladhariService.delete(id);
    return "redirect:/gramaNiladhari";
  }

  @PostMapping(value = "/search")
  @ResponseBody
  public List< GramaNiladhari > search( GramaNiladhari gramaNiladhari) {
    System.out.println(gramaNiladhari.toString());
    return gramaNiladhariService.search(gramaNiladhari);
  }

}
