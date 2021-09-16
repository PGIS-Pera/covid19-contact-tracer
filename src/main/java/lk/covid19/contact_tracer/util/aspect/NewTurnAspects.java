package lk.covid19.contact_tracer.util.aspect;


import lk.covid19.contact_tracer.asset.common_asset.model.enums.Province;
import lk.covid19.contact_tracer.asset.district.entity.District;
import lk.covid19.contact_tracer.asset.district.service.DistrictService;
import lk.covid19.contact_tracer.asset.ds_office.entity.DsOffice;
import lk.covid19.contact_tracer.asset.ds_office.service.DsOfficeService;
import lk.covid19.contact_tracer.asset.grama_niladhari.entity.GramaNiladhari;
import lk.covid19.contact_tracer.asset.grama_niladhari.service.GramaNiladhariService;
import lk.covid19.contact_tracer.asset.person.entity.Person;
import lk.covid19.contact_tracer.asset.person.service.PersonService;
import lk.covid19.contact_tracer.asset.person_location_interact_time.controller.PersonLocationInteractTimeController;
import lk.covid19.contact_tracer.util.service.CommonService;
import lk.covid19.contact_tracer.util.service.MobileMessageService;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Aspect
@Configuration
@AllArgsConstructor
public class NewTurnAspects {

  private final MobileMessageService mobileMessageService;
  private final PersonService personService;


    /*
       //What kind of method calls I would intercept
       //execution(* PACKAGE.*.*(..))
       //Weaving & Weaver
       @Before( "execution(* com.aop.example.aspect.service.HelloService.getMessage(..))" )
       public void before() {
           System.out.println(" Before");
       }
       @After( "execution(* com.aop.example.aspect.service.HelloService.getMessage(..))" )
       public void after() {
           System.out.println(" After");
       }
       @AfterReturning( "execution(* com.aop.example.aspect.service.HelloService.getMessage(..))" )
       public void afterReturning() {
           //when method execute successfully this method would execute
           System.out.println(" After Returning");
       }
       @AfterThrowing( "execution(* com.aop.example.aspect.service.HelloService.getMessage(..))" )
       public void afterThrowing() {
           //without error this method would not be executed
           System.out.println(" After Throwing");
       }
   */


  // after

  @After( value =
      "execution(* lk.covid19.contact_tracer.asset.person.service.PersonService.saveAndTurn(..))" )
  public void before() {
    Person person = personService.findLastPatient();
    List< Person > persons = personService.findByGramaNiladhari(person.getGramaNiladhari());
    String locationListUrl = MvcUriComponentsBuilder
        .fromMethodName(PersonLocationInteractTimeController.class, "interactLocationSearchPage")
        .toUriString();
    String message = "Please check new updated location list \n" + locationListUrl;
    persons.forEach(x -> {
      String mobile = "+94" + x.getMobile().substring(1, 9);
      try {
        mobileMessageService.sendSMS(mobile, message);
      } catch ( Exception e ) {
        e.printStackTrace();
      }
    });

  }
}