package lk.covid19.contact_tracer.asset.person_location_interact_time.dao;


import com.twilio.rest.api.v2010.account.availablephonenumbercountry.Local;
import lk.covid19.contact_tracer.asset.common_asset.model.LocationInteractTimeReport;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.StopActive;
import lk.covid19.contact_tracer.asset.location_interact.entity.LocationInteract;
import lk.covid19.contact_tracer.asset.person_location_interact_time.entity.PersonLocationInteractTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface PersonLocationInteractTimeDao extends JpaRepository< PersonLocationInteractTime, Integer > {

  List< PersonLocationInteractTime > findByLocationInteractAndArrivalAtBetweenAndLeaveAtBetweenAndStopActive(LocationInteract locationInteract, LocalDateTime arrivalAt, LocalDateTime leaveAt, LocalDateTime arrivalAt1,
                                                                                                             LocalDateTime leaveAt1, StopActive active);

  List< LocationInteractTimeReport > findByArrivalAtBetween(LocalDateTime from, LocalDateTime now);

  PersonLocationInteractTime findFirstByOrderByIdDesc();
}
