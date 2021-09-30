package lk.covid19.contact_tracer.asset.report.service;

import lk.covid19.contact_tracer.asset.report.model.DsOfficeReportDTO;
import lk.covid19.contact_tracer.asset.report.model.GramaniladariReportDTO;

public interface ReportService {
  GramaniladariReportDTO gramaniladhari(GramaniladariReportDTO gramaniladariReportDTO);

  DsOfficeReportDTO dsOffice(DsOfficeReportDTO dsOfficeReportDTO);
}
