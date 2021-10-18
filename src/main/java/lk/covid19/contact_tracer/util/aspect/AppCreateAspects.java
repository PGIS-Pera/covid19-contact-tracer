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
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
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


  @After( "execution(* lk.covid19.contact_tracer.asset.common_asset.controller.ApplicationCreateController.district(." +
      ".))" )
  public void createRowSData() {
    ClassLoader cl1 = this.getClass().getClassLoader();

    HashSet< District > savedDistrict = new HashSet<>();
    HashSet< DsOffice > savedDsOffice = new HashSet<>();

    try {
      // FileInputStream inputStream = new FileInputStream(resource.getFile());
      InputStream inputStream = cl1.getResourceAsStream("classpath:excel_files/district.xlsx");
      assert inputStream != null;
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
          Thread.sleep(100);
        }
      }
    } catch ( IOException | InterruptedException e ) {
      e.printStackTrace();
      System.err.println(e.getMessage());
    }

    ClassLoader cl = this.getClass().getClassLoader();
    try {
      InputStream inputStream_grama = cl.getResourceAsStream("classpath:excel_files/grama_niladhari.xlsx");
      assert inputStream_grama != null;
      Workbook workbook_grama = new XSSFWorkbook(inputStream_grama);

      Sheet sheet_grama = workbook_grama.getSheetAt(0);

      for ( int i = 0; i < sheet_grama.getLastRowNum(); i++ ) {
        DsOffice dsOfficeDb = null;
        District district = null;

        Row row = sheet_grama.getRow(i);
        if ( i > 0 && row != null ) {
          Integer gramaniladhari_id = (int) row.getCell(0).getNumericCellValue();
          String districts_name =
              commonService.stringCapitalize(String.valueOf(row.getCell(1).getRichStringCellValue()));
          String ds_name = commonService.stringCapitalize(String.valueOf(row.getCell(2).getRichStringCellValue()));
          String gramaniladhari_name =
              commonService.stringCapitalize(String.valueOf(row.getCell(3).getRichStringCellValue()));
          String gramaniladhari_number = row.getCell(4).getCellType().equals(CellType.NUMERIC) ?
              String.valueOf(((int) row.getCell(4).getNumericCellValue())) :
              String.valueOf(row.getCell(4).getStringCellValue());

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
              .id(gramaniladhari_id)
              .dsOffice(dsOfficeDb)
              .name(gramaniladhari_name)
              .number(gramaniladhari_number)
              .build();
          gramaNiladhariService.persist(gramaNiladhari);
          Thread.sleep(100);
        }
      }
    } catch ( IOException | InterruptedException e ) {
      e.printStackTrace();
      System.err.println(e.getMessage());
    }
  }

}