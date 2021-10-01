package lk.covid19.contact_tracer.util.service;


import lk.covid19.contact_tracer.asset.common_asset.model.AttributeAndCount;
import lk.covid19.contact_tracer.asset.person.entity.Person;
import lk.covid19.contact_tracer.asset.person.entity.enums.PersonStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommonService {
  private final DateTimeAgeService dateTimeAgeService;

  public Integer numberAutoGen(String lastNumber) {
    int newNumber;
    int previousNumber;
    int newNumberFirstTwoCharacters;

    int currentYearLastTwoNumber =
        Integer.parseInt(String.valueOf(dateTimeAgeService.getCurrentDate().getYear()).substring(2, 4));

    if ( lastNumber != null ) {
      previousNumber = Integer.parseInt(lastNumber.substring(0, 9));
      newNumberFirstTwoCharacters = Integer.parseInt(lastNumber.substring(0, 2));

      if ( currentYearLastTwoNumber == newNumberFirstTwoCharacters ) {
        newNumber = previousNumber + 1;
      } else {
        newNumber = Integer.parseInt(currentYearLastTwoNumber + "0000000");
      }

    } else {
      newNumber = Integer.parseInt(currentYearLastTwoNumber + "0000000");
    }
    return newNumber;
  }

  public String stringCapitalize(String text) {
    StringBuilder out_put = new StringBuilder();
    String[] words = text.split(" ");
    for ( String word : words ) {
      if ( word.length() > 0 ) {
        out_put.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase()).append(" ");
      }
    }
    return out_put.toString();
  }

  public String phoneNumberLengthValidator(String number) {
    if ( number.length() == 9 ) {
      number = "0".concat(number);
    }
    return number;
  }

  public boolean isValidEmail(String email) {
    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    Pattern pat = Pattern.compile(emailRegex);
    if ( email == null )
      return false;
    return pat.matcher(email).matches();
  }

  public List< AttributeAndCount > personsAccordingToType(List< Person > persons) {
    List< AttributeAndCount > attributeAndCounts = new ArrayList<>();
    for ( PersonStatus personStatus : PersonStatus.values() ) {
      int count = (int) persons.stream().filter(x -> x.getPersonStatus().equals(personStatus)).count();
      String name = personStatus.getPersonStatus();
      AttributeAndCount attributeAndCount = AttributeAndCount.builder().count(count).name(name).build();
      attributeAndCounts.add(attributeAndCount);
    }

    return attributeAndCounts;
  }
}