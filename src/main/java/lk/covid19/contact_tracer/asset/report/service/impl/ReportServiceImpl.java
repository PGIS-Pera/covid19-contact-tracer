package lk.covid19.contact_tracer.asset.report.service.impl;

import lk.covid19.contact_tracer.asset.common_asset.model.AttributeAndCount;
import lk.covid19.contact_tracer.asset.district.service.DistrictService;
import lk.covid19.contact_tracer.asset.ds_office.service.DsOfficeService;
import lk.covid19.contact_tracer.asset.grama_niladhari.service.GramaNiladhariService;
import lk.covid19.contact_tracer.asset.person.entity.enums.PersonStatus;
import lk.covid19.contact_tracer.asset.person.service.PersonService;
import lk.covid19.contact_tracer.asset.report.model.*;
import lk.covid19.contact_tracer.asset.report.service.ReportService;
import lk.covid19.contact_tracer.asset.turn.entity.Turn;
import lk.covid19.contact_tracer.asset.turn.service.TurnService;
import lk.covid19.contact_tracer.asset.user.service.UserService;
import lk.covid19.contact_tracer.asset.user_details.entity.UserDetails;
import lk.covid19.contact_tracer.asset.user_details.service.UsersDetailsService;
import lk.covid19.contact_tracer.util.service.DateTimeAgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
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
  private final UsersDetailsService usersDetailsService;
  private final UserService userService;
  private final DateTimeAgeService dateTimeAgeService;

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

  public List< UserVsReportDTO > userVsReport() {
    LocalDate reportDay = LocalDate.now().minusDays(1);
    /*
    List< Turn > turns = turnService.findByCreatedAtIsBetween(dateTimeAgeService.dateTimeToLocalDateEndInDay(reportDay),
                                                              dateTimeAgeService.dateTimeToLocalDateStartInDay
                                                              (reportDay));
                                                              */
    /*todo remove this before deploy*/
    List< Turn > turns = turnService.findAll();

    List< UserVsReportDTO > userVsReportDTOS = new ArrayList<>();

    HashSet< String > usernames = new HashSet<>();
    turns.forEach(x -> usernames.add(x.getCreatedBy()));

    usernames.forEach((x) -> {
      UserVsReportDTO usernameVsTurnDTO = new UserVsReportDTO();
      List< Turn > turnUser = turns.stream().filter(y -> y.getCreatedBy().equals(x)).collect(Collectors.toList());

      Integer id = userService.findByUserName(x).getUserDetails().getId();
      UserDetails userDetails = usersDetailsService.findById(id);
      String email = userDetails.getEmail();
      String name = userDetails.getTitle().getTitle() + " " + userDetails.getName();

      usernameVsTurnDTO.setName(name);
      usernameVsTurnDTO.setEmail(email);
      usernameVsTurnDTO.setLocalDate(reportDay);
      usernameVsTurnDTO.setAttributeAndCounts(turnsAccordingToPersonStatus(turnUser));

      userVsReportDTOS.add(usernameVsTurnDTO);

    });


    return userVsReportDTOS;
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
