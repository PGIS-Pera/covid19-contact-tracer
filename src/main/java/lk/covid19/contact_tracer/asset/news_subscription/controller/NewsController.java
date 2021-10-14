package lk.covid19.contact_tracer.asset.news_subscription.controller;

import lk.covid19.contact_tracer.asset.news_subscription.entity.News;
import lk.covid19.contact_tracer.asset.news_subscription.service.NewsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping( "news" )
@AllArgsConstructor
public class NewsController {
  private final NewsService newsService;

  @PostMapping( value = "/subscribe", produces = "application/json" )
  public ResponseEntity< Object > subscribe(@Valid @RequestBody News news) {
    newsService.persist(news);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping( "/unsubscribe/{mobile}" )
  public ResponseEntity< Object > unSubscribe(@PathVariable( "mobile" ) String mobile) {
    newsService.unsubscribe(mobile);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
