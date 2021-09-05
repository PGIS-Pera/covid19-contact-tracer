package lk.covid19.contact_tracer.asset.person_location_interact_time.controller;


import lk.covid19.contact_tracer.asset.attempt.service.AttemptService;
import lk.covid19.contact_tracer.asset.location_interact.service.LocationInteractService;
import lk.covid19.contact_tracer.asset.person_location_interact_time.service.PersonLocationInteractTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( "/patientVisitedPlaceTime" )
@RequiredArgsConstructor
public class PersonLocationInteractTimeController {
  private final PersonLocationInteractTimeService personLocationInteractTimeService;
  private final AttemptService attemptService;
  private final LocationInteractService locationInteractService;

}
