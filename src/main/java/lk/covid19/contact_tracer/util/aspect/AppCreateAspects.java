package lk.covid19.contact_tracer.util.aspect;


import lk.covid19.contact_tracer.asset.common_asset.model.enums.Province;
import lk.covid19.contact_tracer.asset.district.entity.District;
import lk.covid19.contact_tracer.asset.district.service.DistrictService;
import lk.covid19.contact_tracer.asset.ds_office.entity.DsOffice;
import lk.covid19.contact_tracer.asset.ds_office.service.DsOfficeService;
import lk.covid19.contact_tracer.asset.grama_niladhari.entity.GramaNiladhari;
import lk.covid19.contact_tracer.asset.grama_niladhari.service.GramaNiladhariService;
import lk.covid19.contact_tracer.util.service.CommonService;
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

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;

@Aspect
@Configuration
@AllArgsConstructor
public class AppCreateAspects {

  private final CommonService commonService;
  private final ResourceLoader resourceLoader;
  private final DistrictService districtService;
  private final DsOfficeService dsOfficeService;
  private final GramaNiladhariService gramaNiladhariService;


    /*
       //What kind of method calls I would intercept
       //execution(* PACKAGE.*.*(..))
       //Weaving & Weaver
       @Before( "execution(* com.aop.example.aspect.service.HelloService.getMessage(..))" )
       public void before() {
           System.out.println(" Before");
       }
       @After( "execution(* com.aop.example.aspect.service.HelloService.getMessage(..))" )
       public void after() {
           System.out.println(" After");
       }
       @AfterReturning( "execution(* com.aop.example.aspect.service.HelloService.getMessage(..))" )
       public void afterReturning() {
           //when method execute successfully this method would execute
           System.out.println(" After Returning");
       }
       @AfterThrowing( "execution(* com.aop.example.aspect.service.HelloService.getMessage(..))" )
       public void afterThrowing() {
           //without error this method would not be executed
           System.out.println(" After Throwing");
       }
   */


/*    //first example
    @Around( "execution(* com.aop.example.aspect.service.HelloService.getMessage(..))" )
    public Object around(ProceedingJoinPoint joinPoint) {
        System.out.println("Something else !!");
        return "Something else";
    }*/

  //second example
/*  @Around( "execution(* com.aop.example.aspect.service.HelloService.getMessage(..))" )
  public Object around(ProceedingJoinPoint joinPoint) {
    System.out.println("Before");
    Object result = null;
    try {
      System.out.println("parameter "+ Arrays.toString(joinPoint.getArgs()));
      result = joinPoint.proceed();
      System.out.println("After");
    } catch ( Throwable throwable ) {
      throwable.printStackTrace();
    }
    return result;
  }*/

  @After( "execution(* lk.covid19.contact_tracer.asset.common_asset.controller.ApplicationCreateController.sample(..)" +
      ")" )
  public void testONe() {
    System.out.println(LocalDateTime.now());
    System.out.println("afert");
  }

  @Before( "execution(* lk.covid19.contact_tracer.asset.common_asset.controller.ApplicationCreateController.sample(." +
      ".))" )
  public void testTwo() {
    System.out.println(LocalDateTime.now());
    System.out.println("before");

  }

  @After( "execution(* lk.covid19.contact_tracer.asset.common_asset.controller.ApplicationCreateController.district(." +
      ".))" )
  public void createRowSData() {
    Resource resource = resourceLoader.getResource("classpath:excel_files/district.xlsx");
    HashSet< District > savedDistrict = new HashSet<>();
    HashSet< DsOffice > savedDsOffice = new HashSet<>();

    try {
      FileInputStream inputStream = new FileInputStream(resource.getFile());
      Workbook workbook = new XSSFWorkbook(inputStream);
      Sheet sheet = workbook.getSheetAt(0);
      for ( int i = 0; i < sheet.getLastRowNum(); i++ ) {
        Row row = sheet.getRow(i);
        if ( i > 0 && row != null ) {
          District district = new District();
          district.setId(((int) row.getCell(0).getNumericCellValue()));
          district.setProvince(Province.valueOf(String.valueOf(row.getCell(1).getRichStringCellValue())));
          district.setName(commonService.stringCapitalize(String.valueOf(row.getCell(2).getRichStringCellValue())));
          savedDistrict.add(districtService.persist(district));
        }
      }
    } catch ( IOException e ) {
      e.printStackTrace();
      System.err.println(e.getMessage());
    }

    Resource resource_grma = resourceLoader.getResource("classpath:excel_files/grama_niladhari.xlsx");

    try {
      FileInputStream inputStream_grama = new FileInputStream(resource_grma.getFile());
      Workbook workbook_grama = new XSSFWorkbook(inputStream_grama);

      Sheet sheet_grama = workbook_grama.getSheetAt(0);

      for ( int i = 0; i < sheet_grama.getLastRowNum(); i++ ) {
        DsOffice dsOfficeDb = null;
        District district = null;

        Row row = sheet_grama.getRow(i);
        if ( i > 0 && row != null ) {
          String districts_name =
              commonService.stringCapitalize(String.valueOf(row.getCell(0).getRichStringCellValue()));
          String ds_name = commonService.stringCapitalize(String.valueOf(row.getCell(1).getRichStringCellValue()));
          String gramaniladhari_name =
              commonService.stringCapitalize(String.valueOf(row.getCell(2).getRichStringCellValue()));
          String gramaniladhari_number = row.getCell(3).getCellType().equals(CellType.NUMERIC) ?
              String.valueOf(((int) row.getCell(3).getNumericCellValue())) :
              String.valueOf(row.getCell(3).getStringCellValue());

          for ( District districtSaved : savedDistrict ) {
            if ( districtSaved.getName().equals(districts_name) ) {
              district = districtSaved;
              break;
            }
          }

          for ( DsOffice dsOfficeSaved : savedDsOffice ) {
            if ( dsOfficeSaved.getName().equals(ds_name) ) {
              dsOfficeDb = dsOfficeSaved;
              break;
            }
          }

          if ( dsOfficeDb == null ) {
            DsOffice dsOffice = DsOffice.builder()
                .district(district)
                .name(ds_name)
                .build();
            dsOfficeDb = dsOfficeService.persist(dsOffice);
            savedDsOffice.add(dsOfficeDb);
          }

          GramaNiladhari gramaNiladhari = GramaNiladhari.builder()
              .dsOffice(dsOfficeDb)
              .name(gramaniladhari_name)
              .number(gramaniladhari_number)
              .build();
          gramaNiladhariService.persist(gramaNiladhari);
        }
      }
    } catch ( IOException e ) {
      e.printStackTrace();
      System.err.println(e.getMessage());
    }
  }
}