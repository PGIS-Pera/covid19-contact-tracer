package lk.covid19.contact_tracer.asset.turn.controller;


import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.Province;
import lk.covid19.contact_tracer.asset.turn.entity.Turn;
import lk.covid19.contact_tracer.asset.turn.service.TurnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping( "/turn" )
@RequiredArgsConstructor
public class TurnController {
  //todo
  private final TurnService turnService;

  private String commonThing(Model model, Boolean booleanValue, Turn turnObject) {
    model.addAttribute("provinces", Province.values());
    model.addAttribute("addStatus", booleanValue);
    model.addAttribute("turn", turnObject);
    return "turn/addTurn";
  }

  @GetMapping
  public String findAll(Model model) {
    model.addAttribute("turns", turnService.findAll());
    return "turn/turn";
  }

  @GetMapping( "/add" )
  public String form(Model model) {
    return commonThing(model, false, new Turn());
  }

  @GetMapping( "/{id}" )
  public String findById(@PathVariable Integer id, Model model) {
    model.addAttribute("turnDetail", turnService.findById(id));
    return "turn/turn-detail";
  }

  @GetMapping( "/edit/{id}" )
  public String edit(@PathVariable Integer id, Model model) {
    return commonThing(model, true, turnService.findById(id));
  }

  @PostMapping( value = {"/save", "/update"} )
  public String persist(@Valid @ModelAttribute Turn turn, BindingResult bindingResult,
                        RedirectAttributes redirectAttributes, Model model) {
    if ( bindingResult.hasErrors() ) {
      return commonThing(model, false, turn);
    }
    try {
      redirectAttributes.addFlashAttribute("turnDetail", turnService.persist(turn));
    } catch ( Exception e ) {
      ObjectError error = new ObjectError("turn",
                                          "Please make sure that resolve following error \n. System message -->" + e.getCause().getCause().getMessage());
      bindingResult.addError(error);
      return commonThing(model, false, turn);
    }
    return "redirect:/turn";
  }

  @GetMapping( "/delete/{id}" )
  public String delete(@PathVariable Integer id, Model model) {
    turnService.delete(id);
    return "redirect:/turn";
  }

//  @GetMapping( value = "/getTurn/{province}" )
//  @ResponseBody
//  public MappingJacksonValue getTurnByProvince(@PathVariable String province) {
//
//    List< Turn > turns = turnService.findByProvince(Province.valueOf(province));
//
//    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(turns);
//
//    SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter
//        .filterOutAllExcept("id", "name");
//    FilterProvider filters = new SimpleFilterProvider()
//        .addFilter("Turn", simpleBeanPropertyFilter);
//    mappingJacksonValue.setFilters(filters);
//
//    return mappingJacksonValue;
//  }

}
