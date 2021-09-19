package lk.covid19.contact_tracer.asset.news_subscription.controller;

import lk.covid19.contact_tracer.asset.news_subscription.entity.News;
import lk.covid19.contact_tracer.asset.news_subscription.service.NewsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "news" )
@AllArgsConstructor
public class NewsController {
  private final NewsService newsService;

  @GetMapping( "/subscribe" )
  public ResponseEntity< Object > subscribe(News news) {
    newsService.persist(news);
    return new ResponseEntity<>("Subscribe is added successfully", HttpStatus.CREATED);
  }

  @GetMapping( "/unsubscribe/{mobile}" )
  public ResponseEntity< Object > unSubscribe(@PathVariable( "mobile" ) String mobile) {
    newsService.unsubscribe(mobile);
    return new ResponseEntity<>("Unsubscribe is done successfully", HttpStatus.CREATED);
  }
}
