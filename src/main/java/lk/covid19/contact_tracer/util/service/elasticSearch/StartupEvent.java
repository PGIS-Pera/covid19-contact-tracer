package lk.covid19.contact_tracer.util.service.elasticSearch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class StartupEvent implements ApplicationListener< ApplicationReadyEvent > {

  private final IndexingService service;

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    try {
      service.initiateIndexing();
    } catch ( InterruptedException e ) {
      log.error("Failed to reindex entities ", e);
    }
  }
}
