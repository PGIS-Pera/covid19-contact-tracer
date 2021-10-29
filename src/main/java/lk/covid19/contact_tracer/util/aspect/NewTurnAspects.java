package lk.covid19.contact_tracer.util.aspect;


import lk.covid19.contact_tracer.asset.common_asset.model.enums.Province;
import lk.covid19.contact_tracer.asset.district.entity.District;
import lk.covid19.contact_tracer.asset.district.service.DistrictService;
import lk.covid19.contact_tracer.asset.ds_office.entity.DsOffice;
import lk.covid19.contact_tracer.asset.ds_office.service.DsOfficeService;
import lk.covid19.contact_tracer.asset.grama_niladhari.entity.GramaNiladhari;
import lk.covid19.contact_tracer.asset.grama_niladhari.service.GramaNiladhariService;
import lk.covid19.contact_tracer.asset.location_interact.entity.LocationInteract;
import lk.covid19.contact_tracer.asset.location_interact.service.LocationInteractService;
import lk.covid19.contact_tracer.asset.news_subscription.controller.NewsController;
import lk.covid19.contact_tracer.asset.news_subscription.entity.News;
import lk.covid19.contact_tracer.asset.news_subscription.service.NewsService;
import lk.covid19.contact_tracer.asset.person.entity.Person;
import lk.covid19.contact_tracer.asset.person.service.PersonService;
import lk.covid19.contact_tracer.asset.person_location_interact_time.controller.PersonLocationInteractTimeController;
import lk.covid19.contact_tracer.asset.person_location_interact_time.entity.PersonLocationInteractTime;
import lk.covid19.contact_tracer.asset.person_location_interact_time.service.PersonLocationInteractTimeService;
import lk.covid19.contact_tracer.asset.turn.entity.Turn;
import lk.covid19.contact_tracer.asset.turn.service.TurnService;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Aspect
@Configuration
@AllArgsConstructor
public class NewTurnAspects {

  private final MobileMessageService mobileMessageService;
  private final NewsService newsService;
  private final TurnService turnService;
  private final PersonService personService;
  private final LocationInteractService locationInteractService;
  private final PersonLocationInteractTimeService personLocationInteractTimeService;


  @After( value =
      "execution(* lk.covid19.contact_tracer.asset.person.service.PersonService.saveAndTurn(..))" )
  public void saveAndTurn() {
    System.out.println("came here save and turn");
    messageSend();
  }

  @After( value =
      "execution(* lk.covid19.contact_tracer.asset.turn.service.TurnService.persist(..))" )
  public void turn() {
    System.out.println("came here turn");
    messageSend();
  }

  private void messageSend() {
    PersonLocationInteractTime personLocationInteractTime = personLocationInteractTimeService.findLastOne();
    HashSet< News > newsServiceHashSet = new HashSet<>();
    HashSet< LocationInteract > locationInteractHashSet = new HashSet<>();

    Turn turn = turnService.findById(personLocationInteractTime.getTurn().getId());

    for ( PersonLocationInteractTime locationInteractTime : turn.getPersonLocationInteractTimes() ) {
      locationInteractHashSet.add(locationInteractTime.getLocationInteract());
    }
    for ( LocationInteract locationInteract : locationInteractHashSet ) {
      GramaNiladhari gramaNiladhari = locationInteractService.findById(locationInteract.getId()).getGramaNiladhari();
      newsServiceHashSet.addAll(newsService.findByGramaNiladhari(gramaNiladhari));
    }


    String locationListUrl = MvcUriComponentsBuilder
        .fromMethodName(PersonLocationInteractTimeController.class, "interactLocationSearchPage", "")
        .toUriString();

    newsServiceHashSet.forEach(x -> {
      String mobile = "+94" + x.getMobile().substring(1, 10);
      try {
        String unsubscribeUrl = MvcUriComponentsBuilder
            .fromMethodName(NewsController.class, "unSubscribe", x.getMobile())
            .toUriString();
        String message = "Please check new updated location list \n" + locationListUrl + "\n if you want to " +
            "unsubscribe click here " + unsubscribeUrl;

        mobileMessageService.sendSMS(mobile, message);
        System.out.println("send message");
      } catch ( Exception e ) {
        e.printStackTrace();
      }
    });
  }
}