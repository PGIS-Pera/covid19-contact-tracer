package lk.covid19.contact_tracer.asset.person.controller;


import lk.covid19.contact_tracer.asset.attempt.entity.Attempt;
import lk.covid19.contact_tracer.asset.common_asset.model.Pager;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.Gender;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.TwoDate;
import lk.covid19.contact_tracer.asset.person.entity.Person;
import lk.covid19.contact_tracer.asset.person.entity.enums.PersonStatus;
import lk.covid19.contact_tracer.asset.person.service.PersonService;
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
@RequestMapping( "/person" )
@RequiredArgsConstructor
public class PersonController {

  private static final int BUTTONS_TO_SHOW = 5;
  private static final int INITIAL_PAGE = 0;
  private static final int INITIAL_PAGE_SIZE = 5;
  private static final int[] PAGE_SIZES = {5, 10, 20};

  private final PersonService personService;
  private final CommonService commonService;

  @GetMapping
  public ModelAndView showPersonsPage(@RequestParam( "pageSize" ) Optional< Integer > pageSize,
                                      @RequestParam( "page" ) Optional< Integer > page) {
    ModelAndView modelAndView = new ModelAndView("person/person");
    int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
    int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

    Page< Person > persons = personService.findAllPageable(PageRequest.of(evalPage, evalPageSize));
    Pager pager = new Pager(persons.getTotalPages(), persons.getNumber(), BUTTONS_TO_SHOW);

    modelAndView.addObject("persons", persons);
    modelAndView.addObject("selectedPageSize", evalPageSize);
    modelAndView.addObject("pageSizes", PAGE_SIZES);
    modelAndView.addObject("pager", pager);
    return modelAndView;
  }

  private void commonForAll(Model model) {
    model.addAttribute("gender", Gender.values());
    model.addAttribute("deadLives", PersonStatus.values());
  }

  private String commonThings(Model model) {
    commonForAll(model);
    model.addAttribute("patientNic",
                       MvcUriComponentsBuilder
                           .fromMethodName(PersonController.class, "findByNic", "")
                           .toUriString());
    return "person/addPatient";
  }

  @GetMapping( "/{id}" )
  public String patientView(@PathVariable( "id" ) Integer id, Model model) {
    Person person = personService.findById(id);
    model.addAttribute("patientDetail", person);
    model.addAttribute("addStatus", false);
    return "person/person-detail";
  }

  @GetMapping( "/edit/{id}" )
  public String editPatientForm(@PathVariable( "id" ) Integer id, Model model) {
    Person person = personService.findById(id);
    model.addAttribute("person", person);
    model.addAttribute("addStatus", false);
    return commonThings(model);
  }

  @GetMapping( "/add" )
  public String patientAddForm(Model model) {
    model.addAttribute("addStatus", true);
    model.addAttribute("person", new Person());
    return commonThings(model);
  }

  @PostMapping( value = {"/save", "/update"} )
  public String addPatient(@Valid @ModelAttribute Person person, BindingResult result, Model model) {

    if ( result.hasErrors() ) {
      model.addAttribute("addStatus", true);
      model.addAttribute("person", person);
      return commonThings(model);
    }
    try {
      personService.persist(person);
    } catch ( Exception e ) {
      ObjectError error = new ObjectError("person",
                                          "Please make sure that resolve following error \n. <br> System message -->" + e.getCause().getCause().getMessage());
      result.addError(error);
      model.addAttribute("addStatus", true);
      model.addAttribute("person", person);
      return commonThings(model);
    }


    return "redirect:/person";
  }

  @GetMapping( "/remove/{id}" )
  public String removePatient(@PathVariable Integer id) {
    personService.delete(id);
    return "redirect:/person";
  }

  @GetMapping( "/getPatient/{nic}" )
  @ResponseBody
  public Person findByNic(@PathVariable String nic) {
    return personService.findByNic(nic);
  }

  @GetMapping( "/search" )
  public String search(Model model) {
    commonForAll(model);
    model.addAttribute("person", new Person());
    return "person/patientSearch";
  }

  @PostMapping( "/search" )
  public String search(Model model, Person person) {
    List< Person > people = personService.search(person);
    if ( people.size() == 1 ) {
      model.addAttribute("patientDetail", people.get(0));
      return "person/person-detail";
    } else {
      if ( person.getIdentifiedDate() != null ) {
        people.stream()
            .filter((x) -> {
              boolean attemptEqual = false;
              for ( Attempt attempt : x.getAttempts() ) {
                if ( attempt.getIdentifiedDate().equals(person.getIdentifiedDate()) ) {
                  attemptEqual = true;
                  break;
                }
              }
              return attemptEqual;
            })
            .collect(Collectors.toList());
      }
      model.addAttribute("persons", people);
      model.addAttribute("message", message(person));
      commonForAll(model);
      model.addAttribute("person", new Person());
      return "person/patientSearch";
    }
  }

  @PostMapping( "/searchDate" )
  public String searchDate(Model model, TwoDate twoDate) {
    String message = "This report is belong start at " + twoDate.getStartDate() + " end at " + twoDate.getEndDate();
    model.addAttribute("persons", personService.findByAttemptIdentifiedDateRange(twoDate.getStartDate(),
                                                                                 twoDate.getEndDate()));
    model.addAttribute("message", message);
    commonForAll(model);
    model.addAttribute("person", new Person());
    return "person/patientSearch";
  }

  @GetMapping( "/excel" )
  public String saveExcelData() {
    return "person/patientAddByExcel";
  }

  @PostMapping( "/excelAdd" )
  public String saveExcelData(@ModelAttribute Person person, RedirectAttributes redirectAttributes) throws IOException {
    // Workbook workbook = commonService.getExcelWorkbook(person.getMultipartFile().getInputStream(), Objects
    // .requireNonNull(person.getMultipartFile().getOriginalFilename()));

    int name_column = 0;
    int age_column = 0;
    int nic_column = 0;
    int mobile_column = 0;
    int policeArea_column = 0;
    int remark_column = 0;
    int identified_date_column = 0;
    int default_column = 0;

    List< Person > people = new ArrayList<>();

    HSSFWorkbook workbook = new HSSFWorkbook(person.getMultipartFile().getInputStream());
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
          return "redirect:/person/excel";
        }
      } else {
        attempt.setRemark(commonService.stringCapitalize(row.getCell(remark_column).getRichStringCellValue().toString()));
        attempt.setIdentifiedDate(LocalDate.parse(row.getCell(identified_date_column).getRichStringCellValue().toString()));
        Person personIncoming = Person.builder()
            .name(commonService.stringCapitalize(row.getCell(name_column).getRichStringCellValue().toString()))
            .age(Integer.parseInt(row.getCell(age_column).getRichStringCellValue().toString()))
            .nic(commonService.stringCapitalize(row.getCell(nic_column).getRichStringCellValue().toString()))
            .mobile(commonService.phoneNumberLengthValidator(commonService.stringCapitalize(row.getCell(mobile_column).getRichStringCellValue().toString())))
            .policeStationName(commonService.stringCapitalize(row.getCell(policeArea_column).getRichStringCellValue().toString()))
            .attempt(attempt)
            .build();
        people.add(personIncoming);
      }
    }
    redirectAttributes.addFlashAttribute("persons", personService.persistList(people));
    return "redirect:/person/excel";
  }

  private String message(Person person) {
    String message = "";
    try {
      if ( person.getName() != null ) {
        message += " Name : " + person.getName();
      }
      if ( person.getNic() != null ) {
        message += " NIC : " + person.getNic();
      }
      if ( person.getCode() != null ) {
        message += " Register Number : " + person.getCode();
      }
      if ( person.getAge() > 0 ) {
        message += " Age : " + person.getAge();
      }
      if ( person.getGender() != null ) {
        message += " Gender : " + person.getGender().getGender();
      }
      if ( person.getPersonStatus() != null ) {
        message += " Live or Dead : " + person.getPersonStatus().getDeadLive();
      }
      if ( person.getAddress() != null ) {
        message += " Address : " + person.getAddress();
      }
      if ( person.getMobile() != null ) {
        message += " Mobile : " + person.getMobile();
      }
      if ( person.getIdentifiedDate() != null ) {
        message += " Identified Date : " + person.getIdentifiedDate();
      }
    } catch ( Exception e ) {
      message += e.getCause().getCause().getMessage();
    }
    return message;
  }
}
