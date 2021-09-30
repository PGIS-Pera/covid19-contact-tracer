package lk.covid19.contact_tracer.asset.report.service.impl;

import lk.covid19.contact_tracer.asset.district.service.DistrictService;
import lk.covid19.contact_tracer.asset.ds_office.service.DsOfficeService;
import lk.covid19.contact_tracer.asset.grama_niladhari.service.GramaNiladhariService;
import lk.covid19.contact_tracer.asset.person.entity.Person;
import lk.covid19.contact_tracer.asset.person.service.PersonService;
import lk.covid19.contact_tracer.asset.report.model.GramaniladariReportDTO;
import lk.covid19.contact_tracer.asset.report.service.ReportService;
import lk.covid19.contact_tracer.asset.turn.entity.Turn;
import lk.covid19.contact_tracer.asset.turn.service.TurnService;
import lk.covid19.contact_tracer.util.service.CommonService;
import lk.covid19.contact_tracer.util.service.DateTimeAgeService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

  private final CommonService commonService;
  private final TurnService turnService;
  private final PersonService personService;
  private final GramaNiladhariService gramaNiladhariService;
  private final DateTimeAgeService dateTimeAgeService;
  private final DistrictService districtService;
  private final DsOfficeService dsOfficeService;

  public GramaniladariReportDTO gramaniladhari(GramaniladariReportDTO gramaniladariReportDTO) {

    List< Turn > turns = turnService.findByIdentifiedDateIsBetween(gramaniladariReportDTO.getTurnStartAt(),
                                                                   gramaniladariReportDTO.getTurnEndAt())
        .stream()
        .filter(x -> x.getPerson().getGramaNiladhari().equals(gramaniladariReportDTO.getGramaNiladhari()))
        .collect(Collectors.toList());

    List< Person > persons = new ArrayList<>();
    turns.forEach(x -> persons.add(personService.findById(x.getPerson().getId())));
    gramaniladariReportDTO.setAttributeAndCounts(commonService.personsAccordingToType(persons));
    gramaniladariReportDTO.setGramaNiladhari(gramaNiladhariService.findById(gramaniladariReportDTO.getGramaNiladhari().getId()));

    return gramaniladariReportDTO;
  }
}
