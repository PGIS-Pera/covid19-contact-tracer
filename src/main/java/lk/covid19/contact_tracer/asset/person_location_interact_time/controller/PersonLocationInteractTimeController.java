package lk.covid19.contact_tracer.asset.person_location_interact_time.controller;


import lk.covid19.contact_tracer.asset.common_asset.model.TwoDateGramaNiladhari;
import lk.covid19.contact_tracer.asset.grama_niladhari.controller.GramaNiladhariController;
import lk.covid19.contact_tracer.asset.location_interact.entity.LocationInteract;
import lk.covid19.contact_tracer.asset.news_subscription.controller.NewsController;
import lk.covid19.contact_tracer.asset.news_subscription.entity.News;
import lk.covid19.contact_tracer.asset.person_location_interact_time.entity.PersonLocationInteractTime;
import lk.covid19.contact_tracer.asset.person_location_interact_time.service.PersonLocationInteractTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

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
  public Map< LocationInteract, List< PersonLocationInteractTime > > interactLocationSearchPageData(TwoDateGramaNiladhari twoDateGramaNiladhari) {
    System.out.println(twoDateGramaNiladhari.toString());
    return personLocationInteractTimeService.searchWithDateTime(twoDateGramaNiladhari);
  }

}
