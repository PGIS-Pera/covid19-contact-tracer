package lk.covid19.contact_tracer.asset.report.service.impl;

import lk.covid19.contact_tracer.asset.district.service.DistrictService;
import lk.covid19.contact_tracer.asset.ds_office.service.DsOfficeService;
import lk.covid19.contact_tracer.asset.grama_niladhari.service.GramaNiladhariService;
import lk.covid19.contact_tracer.asset.person.entity.Person;
import lk.covid19.contact_tracer.asset.person.service.PersonService;
import lk.covid19.contact_tracer.asset.report.model.DistrictReportDTO;
import lk.covid19.contact_tracer.asset.report.model.DsOfficeReportDTO;
import lk.covid19.contact_tracer.asset.report.model.GramaniladariReportDTO;
import lk.covid19.contact_tracer.asset.report.model.ProvinceReportDTO;
import lk.covid19.contact_tracer.asset.report.service.ReportService;
import lk.covid19.contact_tracer.asset.turn.entity.Turn;
import lk.covid19.contact_tracer.asset.turn.service.TurnService;
import lk.covid19.contact_tracer.util.service.CommonService;
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
  private final DistrictService districtService;
  private final DsOfficeService dsOfficeService;

  public GramaniladariReportDTO gramaniladhari(GramaniladariReportDTO gramaniladariReportDTO) {
    List< Turn > turns = turnService.findByIdentifiedDateIsBetween(gramaniladariReportDTO.getTurnStartAt(),
                                                                   gramaniladariReportDTO.getTurnEndAt())
        .stream()
        .filter((x) -> x.getPerson().getGramaNiladhari().getId()
            .equals(gramaniladariReportDTO.getGramaNiladhari().getId()))
        .collect(Collectors.toList());

    List< Person > persons = new ArrayList<>();
    turns.forEach(x -> persons.add(personService.findById(x.getPerson().getId())));
    gramaniladariReportDTO.setAttributeAndCounts(commonService.personsAccordingToType(persons));
    gramaniladariReportDTO.setGramaNiladhari(gramaNiladhariService.findById(gramaniladariReportDTO.getGramaNiladhari().getId()));

    return gramaniladariReportDTO;
  }

  public DsOfficeReportDTO dsOffice(DsOfficeReportDTO dsOfficeReportDTO) {
    List< Turn > turns = turnService.findByIdentifiedDateIsBetween(dsOfficeReportDTO.getTurnStartAt(),
                                                                   dsOfficeReportDTO.getTurnEndAt())
        .stream()
        .filter(x -> {
                  var lets =
                      x.getPerson().getGramaNiladhari().getDsOffice().getId().equals(dsOfficeReportDTO.getDsOffice().getId());
                  System.out.println(x.getPerson().getGramaNiladhari().getDsOffice().getId());
                  System.out.println(lets);
                  System.out.println(dsOfficeReportDTO.getDsOffice().getId());
                  return lets;
                }
               )
        .collect(Collectors.toList());

    List< Person > persons = new ArrayList<>();
    turns.forEach(x -> persons.add(personService.findById(x.getPerson().getId())));
    dsOfficeReportDTO.setAttributeAndCounts(commonService.personsAccordingToType(persons));
    dsOfficeReportDTO.setDsOffice(dsOfficeService.findById(dsOfficeReportDTO.getDsOffice().getId()));

    return dsOfficeReportDTO;
  }

  public DistrictReportDTO district(DistrictReportDTO districtReportDTO) {
    List< Turn > turns = turnService.findByIdentifiedDateIsBetween(districtReportDTO.getTurnStartAt(),
                                                                   districtReportDTO.getTurnEndAt())
        .stream()
        .filter(x -> x.getPerson().getGramaNiladhari().getDsOffice().getDistrict().equals(districtReportDTO.getDistrict()))
        .collect(Collectors.toList());

    List< Person > persons = new ArrayList<>();
    turns.forEach(x -> persons.add(personService.findById(x.getPerson().getId())));
    districtReportDTO.setAttributeAndCounts(commonService.personsAccordingToType(persons));
    districtReportDTO.setDistrict(districtService.findById(districtReportDTO.getDistrict().getId()));
    return districtReportDTO;
  }

  public ProvinceReportDTO province(ProvinceReportDTO provinceReportDTO) {
    List< Turn > turns = turnService.findByIdentifiedDateIsBetween(provinceReportDTO.getTurnStartAt(),
                                                                   provinceReportDTO.getTurnEndAt())
        .stream()
        .filter(x -> x.getPerson().getGramaNiladhari().getDsOffice().getDistrict().getProvince().equals(provinceReportDTO.getProvince()))
        .collect(Collectors.toList());

    List< Person > persons = new ArrayList<>();
    turns.forEach(x -> persons.add(personService.findById(x.getPerson().getId())));
    provinceReportDTO.setAttributeAndCounts(commonService.personsAccordingToType(persons));
    return provinceReportDTO;
  }

  public ProvinceReportDTO all(ProvinceReportDTO provinceReportDTO) {
    List< Turn > turns = turnService.findByIdentifiedDateIsBetween(provinceReportDTO.getTurnStartAt(),
                                                                   provinceReportDTO.getTurnEndAt())
        .stream().distinct().collect(Collectors.toList());

    List< Person > persons = new ArrayList<>();
    turns.forEach(x -> persons.add(personService.findById(x.getPerson().getId())));
    provinceReportDTO.setAttributeAndCounts(commonService.personsAccordingToType(persons));
    return provinceReportDTO;
  }
}
