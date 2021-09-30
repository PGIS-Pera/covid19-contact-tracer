package lk.covid19.contact_tracer.asset.report.controller;

import lk.covid19.contact_tracer.asset.common_asset.model.TwoDate;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.Province;
import lk.covid19.contact_tracer.asset.district.service.DistrictService;
import lk.covid19.contact_tracer.asset.ds_office.service.DsOfficeService;
import lk.covid19.contact_tracer.asset.grama_niladhari.controller.GramaNiladhariController;
import lk.covid19.contact_tracer.asset.person.service.PersonService;
import lk.covid19.contact_tracer.asset.report.model.DistrictReportDTO;
import lk.covid19.contact_tracer.asset.report.model.DsOfficeReportDTO;
import lk.covid19.contact_tracer.asset.report.model.GramaniladariReportDTO;
import lk.covid19.contact_tracer.asset.report.model.ProvinceReportDTO;
import lk.covid19.contact_tracer.asset.report.service.ReportService;
import lk.covid19.contact_tracer.asset.turn.service.TurnService;
import lk.covid19.contact_tracer.util.service.DateTimeAgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@Controller
@RequestMapping( "/report" )
@RequiredArgsConstructor
public class ReportController {

  private final DistrictService districtService;
  private final DsOfficeService dsOfficeService;
  private final ReportService reportService;

  @GetMapping( "/gramaniladhari" )
  public String gramaniladhari(Model model) {
    model.addAttribute("message", "Please select date range and gramanilashari division.");
    model.addAttribute("gramaNiladhariSearchUrl", MvcUriComponentsBuilder
        .fromMethodName(GramaNiladhariController.class, "searchOne", "")
        .toUriString());
    model.addAttribute("gramaniladhariReportDetail", new GramaniladariReportDTO());
    return "report/gramaniladhari";
  }

  @PostMapping( "/gramaniladhari" )
  public String gramaniladhariReport(@ModelAttribute GramaniladariReportDTO gramaniladariReportDTO, Model model) {
    model.addAttribute("message",
                       "This report is belongs to from " + gramaniladariReportDTO.getTurnStartAt().toString() + " to "
                           + gramaniladariReportDTO.getTurnEndAt().toString());
    model.addAttribute("gramaNiladhariSearchUrl", MvcUriComponentsBuilder
        .fromMethodName(GramaNiladhariController.class, "searchOne", "")
        .toUriString());
    model.addAttribute("gramaniladhariReportDetail", reportService.gramaniladhari(gramaniladariReportDTO));
    return "report/gramaniladhari";
  }

  @GetMapping( "/dsOffice" )
  public String dsOffice(Model model) {
    model.addAttribute("dsOffices", dsOfficeService.findAll());
    model.addAttribute("message", "Please select date range and divisional sectary division.");
    model.addAttribute("dsOfficeReportDetail", new DsOfficeReportDTO());
    return "report/dsOffice";
  }

  @PostMapping( "/dsOffice" )
  public String dsOfficeReport(@ModelAttribute DsOfficeReportDTO dsOfficeReportDTO, Model model) {
    model.addAttribute("message",
                       "This report is belongs to from " + dsOfficeReportDTO.getTurnStartAt().toString() + " to "
                           + dsOfficeReportDTO.getTurnEndAt().toString());
    model.addAttribute("dsOffices", dsOfficeService.findAll());
    model.addAttribute("dsOfficeReportDetail", reportService.dsOffice(dsOfficeReportDTO));
    return "report/dsOffice";
  }

  @GetMapping( "/district" )
  public String district(Model model) {
    model.addAttribute("districts", districtService.findAll());
    model.addAttribute("message", "Please select date range and district.");
    model.addAttribute("districtReportDetail", new DistrictReportDTO());
    return "report/district";
  }

  @PostMapping( "/district" )
  public String districtReport(@ModelAttribute DistrictReportDTO districtReportDTO, Model model) {
    model.addAttribute("message",
                       "This report is belongs to from " + districtReportDTO.getTurnStartAt().toString() + " to "
                           + districtReportDTO.getTurnEndAt().toString());
    model.addAttribute("districts", districtService.findAll());
    model.addAttribute("districtReportDetail", reportService.district(districtReportDTO));
    return "report/district";
  }

  @GetMapping( "/province" )
  public String province(Model model) {
    model.addAttribute("provinces", Province.values());
    model.addAttribute("message", "Please select date range and province.");
    model.addAttribute("provinceReportDetail", new ProvinceReportDTO());
    return "report/province";
  }

  @PostMapping( "/province" )
  public String provinceReport(@ModelAttribute ProvinceReportDTO provinceReportDTO, Model model) {
    model.addAttribute("provinces", Province.values());
    model.addAttribute("message",
                       "This report is belongs to from " + provinceReportDTO.getTurnStartAt().toString() + " to "
                           + provinceReportDTO.getTurnEndAt().toString());
    model.addAttribute("provinceReportDetail", reportService.province(provinceReportDTO));
    return "report/province";
  }

  @GetMapping( "/all" )
  public String all(Model model) {
    model.addAttribute("message", "Please select date range.");
    model.addAttribute("provinceReportDetail", new ProvinceReportDTO());
    return "report/all";
  }

  @PostMapping( "/all" )
  public String allReport(@ModelAttribute ProvinceReportDTO provinceReportDTO, Model model) {
    model.addAttribute("message",
                       "This report is belongs to from " + provinceReportDTO.getTurnStartAt().toString() + " to "
                           + provinceReportDTO.getTurnEndAt().toString());
    model.addAttribute("allReportDetail", reportService.all(provinceReportDTO));

    return "report/all";
  }
}
