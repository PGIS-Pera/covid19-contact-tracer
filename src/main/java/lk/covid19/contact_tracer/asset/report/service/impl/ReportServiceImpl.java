package lk.covid19.contact_tracer.asset.report.service.impl;

import lk.covid19.contact_tracer.asset.common_asset.model.AttributeAndCount;
import lk.covid19.contact_tracer.asset.district.service.DistrictService;
import lk.covid19.contact_tracer.asset.ds_office.service.DsOfficeService;
import lk.covid19.contact_tracer.asset.grama_niladhari.service.GramaNiladhariService;
import lk.covid19.contact_tracer.asset.person.entity.Person;
import lk.covid19.contact_tracer.asset.person.entity.enums.PersonStatus;
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

    gramaniladariReportDTO.setAttributeAndCounts(turnsAccordingToPersonStatus(turns));
    gramaniladariReportDTO.setGramaNiladhari(gramaNiladhariService.findById(gramaniladariReportDTO.getGramaNiladhari().getId()));

    return gramaniladariReportDTO;
  }

  public DsOfficeReportDTO dsOffice(DsOfficeReportDTO dsOfficeReportDTO) {
    List< Turn > turns = turnService.findByIdentifiedDateIsBetween(dsOfficeReportDTO.getTurnStartAt(),
                                                                   dsOfficeReportDTO.getTurnEndAt())
        .stream()
        .filter(x -> x.getPerson().getGramaNiladhari().getDsOffice().getId().equals(dsOfficeReportDTO.getDsOffice().getId())
               )
        .collect(Collectors.toList());

    dsOfficeReportDTO.setAttributeAndCounts(turnsAccordingToPersonStatus(turns));
    dsOfficeReportDTO.setDsOffice(dsOfficeService.findById(dsOfficeReportDTO.getDsOffice().getId()));

    return dsOfficeReportDTO;
  }

  public DistrictReportDTO district(DistrictReportDTO districtReportDTO) {
    List< Turn > turns = turnService.findByIdentifiedDateIsBetween(districtReportDTO.getTurnStartAt(),
                                                                   districtReportDTO.getTurnEndAt())
        .stream()
        .filter(x -> x.getPerson().getGramaNiladhari().getDsOffice().getDistrict().equals(districtReportDTO.getDistrict()))
        .collect(Collectors.toList());

    districtReportDTO.setAttributeAndCounts(turnsAccordingToPersonStatus(turns));
    districtReportDTO.setDistrict(districtService.findById(districtReportDTO.getDistrict().getId()));
    return districtReportDTO;
  }

  public ProvinceReportDTO province(ProvinceReportDTO provinceReportDTO) {
    List< Turn > turns = turnService.findByIdentifiedDateIsBetween(provinceReportDTO.getTurnStartAt(),
                                                                   provinceReportDTO.getTurnEndAt())
        .stream()
        .filter(x -> x.getPerson().getGramaNiladhari().getDsOffice().getDistrict().getProvince().equals(provinceReportDTO.getProvince()))
        .collect(Collectors.toList());

    provinceReportDTO.setAttributeAndCounts(turnsAccordingToPersonStatus(turns));
    return provinceReportDTO;
  }

  public ProvinceReportDTO all(ProvinceReportDTO provinceReportDTO) {
    List< Turn > turns = turnService.findByIdentifiedDateIsBetween(provinceReportDTO.getTurnStartAt(),
                                                                   provinceReportDTO.getTurnEndAt())
        .stream().distinct().collect(Collectors.toList());

    provinceReportDTO.setAttributeAndCounts(turnsAccordingToPersonStatus(turns));
    return provinceReportDTO;
  }

  public ProvinceReportDTO allCurrent() {
    ProvinceReportDTO provinceReportDTO = new ProvinceReportDTO();
    List< Turn > turns = turnService.findAll()
        .stream().distinct().collect(Collectors.toList());

    provinceReportDTO.setAttributeAndCounts(turnsAccordingToPersonStatus(turns));
    return provinceReportDTO;
  }

  public List< AttributeAndCount > turnsAccordingToPersonStatus(List< Turn > turns) {
    List< AttributeAndCount > attributeAndCounts = new ArrayList<>();
    for ( PersonStatus personStatus : PersonStatus.values() ) {
      int count = (int) turns.stream().filter(x -> x.getPersonStatus().equals(personStatus)).count();
      String name = personStatus.getPersonStatus();
      AttributeAndCount attributeAndCount = AttributeAndCount.builder().count(count).name(name).build();
      attributeAndCounts.add(attributeAndCount);
    }

    return attributeAndCounts;
  }

}
