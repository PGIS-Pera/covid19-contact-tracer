package lk.covid19.contact_tracer.asset.common_asset.model;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Pager {

  private int buttonsToShow = 5;

  private int startPage;

  private int endPage;

  public Pager(int totalPages, int currentPage, int buttonsToShow) {

    setButtonsToShow(buttonsToShow);

    int halfPagesToShow = getButtonsToShow() / 2;

    if ( totalPages <= getButtonsToShow() ) {
      setStartPage(1);
      setEndPage(totalPages);

    } else if ( currentPage - halfPagesToShow <= 0 ) {
      setStartPage(1);
      setEndPage(getButtonsToShow());

    } else if ( currentPage + halfPagesToShow == totalPages ) {
      setStartPage(currentPage - halfPagesToShow);
      setEndPage(totalPages);

    } else if ( currentPage + halfPagesToShow > totalPages ) {
      setStartPage(totalPages - getButtonsToShow() + 1);
      setEndPage(totalPages);

    } else {
      setStartPage(currentPage - halfPagesToShow);
      setEndPage(currentPage + halfPagesToShow);
    }

  }

  @Override
  public String toString() {
    return "Pager [startPage=" + startPage + ", endPage=" + endPage + "]";
  }

}
