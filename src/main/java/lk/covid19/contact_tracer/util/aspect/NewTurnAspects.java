package lk.covid19.contact_tracer.util.aspect;


import lk.covid19.contact_tracer.asset.grama_niladhari.entity.GramaNiladhari;
import lk.covid19.contact_tracer.asset.location_interact.entity.LocationInteract;
import lk.covid19.contact_tracer.asset.location_interact.service.LocationInteractService;
import lk.covid19.contact_tracer.asset.news_subscription.controller.NewsController;
import lk.covid19.contact_tracer.asset.news_subscription.entity.News;
import lk.covid19.contact_tracer.asset.news_subscription.service.NewsService;
import lk.covid19.contact_tracer.asset.person_location_interact_time.controller.PersonLocationInteractTimeController;
import lk.covid19.contact_tracer.asset.person_location_interact_time.entity.PersonLocationInteractTime;
import lk.covid19.contact_tracer.asset.person_location_interact_time.service.PersonLocationInteractTimeService;
import lk.covid19.contact_tracer.asset.turn.entity.Turn;
import lk.covid19.contact_tracer.asset.turn.service.TurnService;
import lk.covid19.contact_tracer.util.service.MobileMessageService;
import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.HashSet;

@Aspect
@Configuration
@AllArgsConstructor
public class NewTurnAspects {

  private final MobileMessageService mobileMessageService;
  private final NewsService newsService;
  private final TurnService turnService;
  private final LocationInteractService locationInteractService;
  private final PersonLocationInteractTimeService personLocationInteractTimeService;


  @After( value =
      "execution(* lk.covid19.contact_tracer.asset.person.controller.PersonController.savePersonTurn(..))" )
  public void saveAndTurn() {
    messageSend();
  }

  private void messageSend() {
    PersonLocationInteractTime personLocationInteractTime = personLocationInteractTimeService.findLastOne();
    System.out.println(personLocationInteractTime.getId());
    HashSet< News > newsServiceHashSet = new HashSet<>();
    HashSet< LocationInteract > locationInteractHashSet = new HashSet<>();

    Turn turn = turnService.findById(personLocationInteractTime.getTurn().getId());

    if ( turn != null ) {
      for ( PersonLocationInteractTime locationInteractTime : personLocationInteractTimeService.findByTurn(turn) ) {
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

  @After( value =
      "execution(* lk.covid19.contact_tracer.asset.turn.controller.TurnController.persist(..))" )
  public void turn() {
    messageSend();
  }
}