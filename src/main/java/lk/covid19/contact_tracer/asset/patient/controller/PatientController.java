package lk.covid19.contact_tracer.asset.patient.controller;


import lk.covid19.contact_tracer.asset.attempt.entity.Attempt;
import lk.covid19.contact_tracer.asset.common_asset.model.Pager;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.Gender;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.TwoDate;
import lk.covid19.contact_tracer.asset.patient.entity.Patient;
import lk.covid19.contact_tracer.asset.patient.entity.enums.DeadLive;
import lk.covid19.contact_tracer.asset.patient.service.PatientService;
import lk.covid19.contact_tracer.util.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
@RequestMapping( "/patient" )
@RequiredArgsConstructor
public class PatientController {

  private static final int BUTTONS_TO_SHOW = 5;
  private static final int INITIAL_PAGE = 0;
  private static final int INITIAL_PAGE_SIZE = 5;
  private static final int[] PAGE_SIZES = {5, 10, 20};

  private final PatientService patientService;
  private final CommonService commonService;

  @GetMapping
  public ModelAndView showPersonsPage(@RequestParam( "pageSize" ) Optional< Integer > pageSize,
                                      @RequestParam( "page" ) Optional< Integer > page) {
    ModelAndView modelAndView = new ModelAndView("patient/patient");
    int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
    int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

    Page< Patient > patients = patientService.findAllPageable(PageRequest.of(evalPage, evalPageSize));
    Pager pager = new Pager(patients.getTotalPages(), patients.getNumber(), BUTTONS_TO_SHOW);

    modelAndView.addObject("patients", patients);
    modelAndView.addObject("selectedPageSize", evalPageSize);
    modelAndView.addObject("pageSizes", PAGE_SIZES);
    modelAndView.addObject("pager", pager);
    return modelAndView;
  }

  private void commonForAll(Model model) {
    model.addAttribute("gender", Gender.values());
    model.addAttribute("deadLives", DeadLive.values());
  }

  private String commonThings(Model model) {
    commonForAll(model);
    model.addAttribute("patientNic",
                       MvcUriComponentsBuilder
                           .fromMethodName(PatientController.class, "findByNic", "")
                           .toUriString());
    return "patient/addPatient";
  }

  @GetMapping( "/{id}" )
  public String patientView(@PathVariable( "id" ) Integer id, Model model) {
    Patient patient = patientService.findById(id);
    model.addAttribute("patientDetail", patient);
    model.addAttribute("addStatus", false);
    return "patient/patient-detail";
  }

  @GetMapping( "/edit/{id}" )
  public String editPatientForm(@PathVariable( "id" ) Integer id, Model model) {
    Patient patient = patientService.findById(id);
    model.addAttribute("patient", patient);
    model.addAttribute("addStatus", false);
    return commonThings(model);
  }

  @GetMapping( "/add" )
  public String patientAddForm(Model model) {
    model.addAttribute("addStatus", true);
    model.addAttribute("patient", new Patient());
    return commonThings(model);
  }

  @PostMapping( value = {"/save", "/update"} )
  public String addPatient(@Valid @ModelAttribute Patient patient, BindingResult result, Model model) {

    if ( result.hasErrors() ) {
      model.addAttribute("addStatus", true);
      model.addAttribute("patient", patient);
      return commonThings(model);
    }
    try {
      patientService.persist(patient);
    } catch ( Exception e ) {
      ObjectError error = new ObjectError("patient",
                                          "Please make sure that resolve following error \n. <br> System message -->" + e.getCause().getCause().getMessage());
      result.addError(error);
      model.addAttribute("addStatus", true);
      model.addAttribute("patient", patient);
      return commonThings(model);
    }


    return "redirect:/patient";
  }

  @GetMapping( "/remove/{id}" )
  public String removePatient(@PathVariable Integer id) {
    patientService.delete(id);
    return "redirect:/patient";
  }

  @GetMapping( "/getPatient/{nic}" )
  @ResponseBody
  public Patient findByNic(@PathVariable String nic) {
    return patientService.findByNic(nic);
  }

  @GetMapping( "/search" )
  public String search(Model model) {
    commonForAll(model);
    model.addAttribute("patient", new Patient());
    return "patient/patientSearch";
  }

  @PostMapping( "/search" )
  public String search(Model model, Patient patient) {
    List< Patient > patients = patientService.search(patient);
    if ( patients.size() == 1 ) {
      model.addAttribute("patientDetail", patients.get(0));
      return "patient/patient-detail";
    } else {
      if ( patient.getIdentifiedDate() != null ) {
        patients.stream()
            .filter((x) -> {
              boolean attemptEqual = false;
              for ( Attempt attempt : x.getAttempts() ) {
                if ( attempt.getIdentifiedDate().equals(patient.getIdentifiedDate()) ) {
                  attemptEqual = true;
                  break;
                }
              }
              return attemptEqual;
            })
            .collect(Collectors.toList());
      }
      model.addAttribute("patients", patients);
      model.addAttribute("message", message(patient));
      commonForAll(model);
      model.addAttribute("patient", new Patient());
      return "patient/patientSearch";
    }
  }

  @PostMapping( "/searchDate" )
  public String searchDate(Model model, TwoDate twoDate) {
    String message = "This report is belong start at " + twoDate.getStartDate() + " end at " + twoDate.getEndDate();
    model.addAttribute("patients", patientService.findByAttemptIdentifiedDateRange(twoDate.getStartDate(),
                                                                                   twoDate.getEndDate()));
    model.addAttribute("message", message);
    commonForAll(model);
    model.addAttribute("patient", new Patient());
    return "patient/patientSearch";
  }

  @GetMapping( "/excel" )
  public String saveExcelData() {
    return "patient/patientAddByExcel";
  }

  @PostMapping( "/excelAdd" )
  public String saveExcelData(@ModelAttribute Patient patient, RedirectAttributes redirectAttributes) throws IOException {
    // Workbook workbook = commonService.getExcelWorkbook(patient.getMultipartFile().getInputStream(), Objects
    // .requireNonNull(patient.getMultipartFile().getOriginalFilename()));

    int name_column = 0;
    int age_column = 0;
    int nic_column = 0;
    int mobile_column = 0;
    int policeArea_column = 0;
    int remark_column = 0;
    int identified_date_column = 0;
    int default_column = 0;

    List< Patient > patients = new ArrayList<>();

    HSSFWorkbook workbook = new HSSFWorkbook(patient.getMultipartFile().getInputStream());
    SummaryInformation summaryInfo = workbook.getSummaryInformation();

    //Creates a worksheet object representing the first sheet
    HSSFSheet worksheet = workbook.getSheetAt(0);
    //Reads the data in excel file until last row is encountered

    Attempt attempt = Attempt.builder()
        .sheetName(worksheet.getSheetName())
        .title(summaryInfo.getTitle())
        .author(summaryInfo.getAuthor())
        .createdDate(summaryInfo.getCreateDateTime())
        .lastAuthor(summaryInfo.getLastAuthor())
        .build();


    for ( int i = 0; i <= worksheet.getLastRowNum(); i++ ) {
      HSSFRow row = worksheet.getRow(i);
      if ( i == 0 ) {
        for ( int j = 0; j < 10; j++ ) {
          switch ( commonService.stringCapitalize(row.getCell(j).getRichStringCellValue().toString()) ) {
            case "Nic":
              nic_column = j;
              break;
            case "Name":
              name_column = j;
              break;
            case "Age":
              age_column = j;
              break;
            case "Mobile No":
            case "Mobile":
              mobile_column = j;
              break;
            case "Remark":
              remark_column = j;
              break;
            case "Police Station":
            case "Police Area":
            case "Police":
              policeArea_column = j;
              break;
            case "Identified Date":
            case "Identified":
            case "Date":
              identified_date_column = j;
              break;
            default:
              default_column = j;
          }
        }

        if ( name_column > 0 && age_column > 0 && nic_column > 0 && mobile_column > 0 && policeArea_column > 0 && remark_column > 0 && default_column > 0 ) {
          redirectAttributes.addFlashAttribute("message", "Some one change the excel sheet please provide valid " +
              "excel" +
              " sheet");
          return "redirect:/patient/excel";
        }
      } else {
        attempt.setRemark(commonService.stringCapitalize(row.getCell(remark_column).getRichStringCellValue().toString()));
        attempt.setIdentifiedDate(LocalDate.parse(row.getCell(identified_date_column).getRichStringCellValue().toString()));
        Patient patientIncoming = Patient.builder()
            .name(commonService.stringCapitalize(row.getCell(name_column).getRichStringCellValue().toString()))
            .age(Integer.parseInt(row.getCell(age_column).getRichStringCellValue().toString()))
            .nic(commonService.stringCapitalize(row.getCell(nic_column).getRichStringCellValue().toString()))
            .mobile(commonService.phoneNumberLengthValidator(commonService.stringCapitalize(row.getCell(mobile_column).getRichStringCellValue().toString())))
            .policeStationName(commonService.stringCapitalize(row.getCell(policeArea_column).getRichStringCellValue().toString()))
            .attempt(attempt)
            .build();
        patients.add(patientIncoming);
      }
    }
    redirectAttributes.addFlashAttribute("patients", patientService.persistList(patients));
    return "redirect:/patient/excel";
  }

  private String message(Patient patient) {
    String message = "";
    try {
      if ( patient.getName() != null ) {
        message += " Name : " + patient.getName();
      }
      if ( patient.getNic() != null ) {
        message += " NIC : " + patient.getNic();
      }
      if ( patient.getCode() != null ) {
        message += " Register Number : " + patient.getCode();
      }
      if ( patient.getAge() > 0 ) {
        message += " Age : " + patient.getAge();
      }
      if ( patient.getGender() != null ) {
        message += " Gender : " + patient.getGender().getGender();
      }
      if ( patient.getDeadLive() != null ) {
        message += " Live or Dead : " + patient.getDeadLive().getDeadLive();
      }
      if ( patient.getAddress() != null ) {
        message += " Address : " + patient.getAddress();
      }
      if ( patient.getMobile() != null ) {
        message += " Mobile : " + patient.getMobile();
      }
      if ( patient.getIdentifiedDate() != null ) {
        message += " Identified Date : " + patient.getIdentifiedDate();
      }
    } catch ( Exception e ) {
      message += e.getCause().getCause().getMessage();
    }
    return message;
  }
}
