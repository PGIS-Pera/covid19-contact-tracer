package lk.covid19.contact_tracer.asset.grama_niladhari.controller;


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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
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
    model.addAttribute("agOfficeURL",
                       MvcUriComponentsBuilder
                           .fromMethodName(DsOfficeController.class, "getAgOfficeByDistrict", "")
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
   /*
   @GetMapping(value = "/getGramaNiladhari/{id}")
    @ResponseBody
    public MappingJacksonValue getGramaNiladhariByPolice(@PathVariable int id) {
        PoliceStation gramaNiladhari=new PoliceStation();
        gramaNiladhari.setId(id);

        //MappingJacksonValue
        List<GramaNiladhari> gramaNiladharis = gramaNiladhariService.findByPoliceStation(gramaNiladhari);
        //employeeService.findByWorkingPlace(workingPlaceService.findById(id));

        //Create new mapping jackson value and set it to which was need to filter
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(gramaNiladharis);

        //simpleBeanPropertyFilter :-  need to give any id to addFilter method and created filter which was mentioned
        // what parameter's necessary to provide
        SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name");
        //filters :-  set front end required value to before filter

        FilterProvider filters = new SimpleFilterProvider()
                .addFilter("GramaNiladhari", simpleBeanPropertyFilter);
        //Employee :- need to annotate relevant class with JsonFilter  {@JsonFilter("Employee") }
        mappingJacksonValue.setFilters(filters);

        return mappingJacksonValue;
    }
    */

}
