package lk.covid19.contact_tracer.asset.news_subscription.service;


import lk.covid19.contact_tracer.asset.common_asset.model.enums.Province;
import lk.covid19.contact_tracer.asset.grama_niladhari.entity.GramaNiladhari;
import lk.covid19.contact_tracer.asset.news_subscription.entity.News;

import java.util.List;

public interface NewsService {

  List< News > findAll();

  News findById(Integer id);

  News persist(News news);

  boolean delete(Integer id);

  List< News > search(News news);

  List< News > findByGramaNiladhari(GramaNiladhari gramaNiladhari);
}
