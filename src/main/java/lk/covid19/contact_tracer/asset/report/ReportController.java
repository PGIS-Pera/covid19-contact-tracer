package lk.covid19.contact_tracer.asset.report;

import lk.covid19.contact_tracer.asset.attempt.service.AttemptService;
import lk.covid19.contact_tracer.asset.district.service.DistrictService;
import lk.covid19.contact_tracer.asset.person.service.PersonService;
import lk.covid19.contact_tracer.util.service.DateTimeAgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( "/report" )
@RequiredArgsConstructor
public class ReportController {
  private final AttemptService attemptService;
  private final PersonService personService;
  private final DateTimeAgeService dateTimeAgeService;
  private final DistrictService districtService;


}
