package lk.covid19.contact_tracer.asset.visited_place.dao;


import lk.covid19.contact_tracer.asset.visited_place.entity.VisitedPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitedPlaceDao extends JpaRepository< VisitedPlace, Integer > {

}
