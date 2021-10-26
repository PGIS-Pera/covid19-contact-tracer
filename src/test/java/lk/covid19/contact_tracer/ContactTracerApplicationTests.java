package lk.covid19.contact_tracer;

import lk.covid19.contact_tracer.asset.person.entity.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.ParameterResolutionDelegate;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@EnableAutoConfiguration( exclude = {DataSourceAutoConfiguration.class} )
@SpringBootTest
class ContactTracerApplicationTests {

	@Test
	void contextLoads() {
	}

}
