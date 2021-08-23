package lk.covid19.contact_tracer.util.schedule;


import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class VesselOrderSchedule {
 /* private final VesselOrderService vesselOrderService;
  private final ChandlerLicenseService chandlerLicenseService;
  private final PaymentService paymentService;

  public VesselOrderSchedule(VesselOrderService vesselOrderService, ChandlerLicenseService chandlerLicenseService,
                             PaymentService paymentService) {
    this.vesselOrderService = vesselOrderService;
    this.chandlerLicenseService = chandlerLicenseService;
    this.paymentService = paymentService;
  }

  @Scheduled( cron = "0 0 0 * * *" )
  void fourthSchedule() {
    //vesselOrder validity check
    List< VesselOrder > vesselOrders = new ArrayList<>();
    vesselOrderService.findByVesselOrderStatus(VesselOrderStatus.PROCESSING)
        .stream()
        .filter(x -> x.getClosingDate().equals(LocalDate.now()))
        .collect(Collectors.toList())
        .forEach(x -> {
          x.setVesselOrderStatus(VesselOrderStatus.BIDEN);
          vesselOrders.add(x);
        });
    vesselOrderService.saveAll(vesselOrders);
//chandler license validity check
    List< ChandlerLicense > chandlerLicenses = new ArrayList<>();
    chandlerLicenseService.findByLicenseStatus(LicenseStatus.VALID)
        .stream()
        .filter(x -> x.getEndDate().equals(LocalDate.now()))
        .collect(Collectors.toList())
        .forEach(x -> {
          x.setLicenseStatus(LicenseStatus.INVALID);
          chandlerLicenses.add(x);
        });

    chandlerLicenseService.saveAll(chandlerLicenses);

    // chandler accepted payments -> to
    paymentService.findByStatusConformation(StatusConformation.RECEVINGPAYMENT).forEach(
        x -> {
          x.setStatusConformation(StatusConformation.ORDERCOLOSE);
          paymentService.persist(x);
          VesselOrder vesselOrder = vesselOrderService.findById(x.getVesselOrder().getId());
          vesselOrder.setVesselOrderStatus(VesselOrderStatus.COMPLETED);
          vesselOrderService.persist(vesselOrder);
        });

  }

*/
}
