package lk.covid19.contact_tracer.asset.person_location_interact_time.controller;


import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lk.covid19.contact_tracer.asset.common_asset.model.TwoDateGramaNiladhari;
import lk.covid19.contact_tracer.asset.grama_niladhari.controller.GramaNiladhariController;
import lk.covid19.contact_tracer.asset.grama_niladhari.entity.GramaNiladhari;
import lk.covid19.contact_tracer.asset.location_interact.entity.LocationInteract;
import lk.covid19.contact_tracer.asset.news_subscription.controller.NewsController;
import lk.covid19.contact_tracer.asset.news_subscription.entity.News;
import lk.covid19.contact_tracer.asset.person_location_interact_time.entity.PersonLocationInteractTime;
import lk.covid19.contact_tracer.asset.person_location_interact_time.service.PersonLocationInteractTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping( "/patientVisitedPlaceTime" )
@RequiredArgsConstructor
public class PersonLocationInteractTimeController {
  private final PersonLocationInteractTimeService personLocationInteractTimeService;

  @GetMapping
  public String interactLocationSearchPage(Model model) {
    model.addAttribute("gramaNiladhariSearchUrl", MvcUriComponentsBuilder
        .fromMethodName(GramaNiladhariController.class, "searchOne", "")
        .toUriString());
    model.addAttribute("subscribeUrl", MvcUriComponentsBuilder
        .fromMethodName(NewsController.class, "subscribe", "")
        .toUriString());
    model.addAttribute("interactLocationSearchPageDataUrl", MvcUriComponentsBuilder
        .fromMethodName(PersonLocationInteractTimeController.class, "interactLocationSearchPageData",
                        new TwoDateGramaNiladhari())
        .toUriString());
    return "patientVisitedPlaceTime/patientVisitedPlaceTime";
  }

  @PostMapping( "/getPlaces" )
  @ResponseBody
  public MappingJacksonValue interactLocationSearchPageData(@Valid @RequestBody TwoDateGramaNiladhari twoDateGramaNiladhari) {

    MappingJacksonValue mappingJacksonValue =
        new MappingJacksonValue(personLocationInteractTimeService.searchWithDateTime(twoDateGramaNiladhari));

    SimpleBeanPropertyFilter personLocationInteractTime = SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "stopActive", "arrivalAt", "leaveAt", "locationInteract");

    SimpleBeanPropertyFilter locationInteract = SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "name", "gramaNiladhari");

    SimpleBeanPropertyFilter gramaNiladhari = SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "name", "number");

    FilterProvider filter = new SimpleFilterProvider()
        .addFilter("PersonLocationInteractTime", personLocationInteractTime)
        .addFilter("LocationInteract", locationInteract)
        .addFilter("GramaNiladhari", gramaNiladhari);

    mappingJacksonValue.setFilters(filter);

    return mappingJacksonValue;
  }

}
