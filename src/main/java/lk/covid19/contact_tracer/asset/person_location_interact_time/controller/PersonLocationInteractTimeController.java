package lk.covid19.contact_tracer.asset.person_location_interact_time.controller;


import lk.covid19.contact_tracer.asset.grama_niladhari.controller.GramaNiladhariController;
import lk.covid19.contact_tracer.asset.turn.service.TurnService;
import lk.covid19.contact_tracer.asset.location_interact.service.LocationInteractService;
import lk.covid19.contact_tracer.asset.person_location_interact_time.service.PersonLocationInteractTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@Controller
@RequestMapping( "/patientVisitedPlaceTime" )
@RequiredArgsConstructor
public class PersonLocationInteractTimeController {
  private final PersonLocationInteractTimeService personLocationInteractTimeService;
  private final TurnService turnService;
  private final LocationInteractService locationInteractService;

  @GetMapping
  public String interactLocationSearchPage(Model model) {
    model.addAttribute("gramaNiladhariSearchUrl", MvcUriComponentsBuilder
        .fromMethodName(GramaNiladhariController.class, "searchOne", "")
        .toUriString());
    return "patientVisitedPlaceTime/patientVisitedPlaceTime";
  }

}
