package lk.covid19.contact_tracer.asset.report.service;

import lk.covid19.contact_tracer.asset.report.model.*;

import java.util.List;

public interface ReportService {
  GramaniladariReportDTO gramaniladhari(GramaniladariReportDTO gramaniladariReportDTO);

  DsOfficeReportDTO dsOffice(DsOfficeReportDTO dsOfficeReportDTO);

  DistrictReportDTO district(DistrictReportDTO districtReportDTO);

  ProvinceReportDTO province(ProvinceReportDTO provinceReportDTO);

  ProvinceReportDTO all(ProvinceReportDTO provinceReportDTO);

  ProvinceReportDTO allCurrent();

  List< UserVsReportDTO > userVsReport();
}
