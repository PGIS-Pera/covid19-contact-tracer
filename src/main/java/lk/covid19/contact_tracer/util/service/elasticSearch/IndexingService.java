package lk.covid19.contact_tracer.util.service.elasticSearch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
@Slf4j
public class IndexingService {

  private final EntityManager em;

  @Transactional
  public void initiateIndexing() throws InterruptedException {
    log.info("Initiating indexing...");
    FullTextEntityManager fullTextEntityManager =
        Search.getFullTextEntityManager(em);
    fullTextEntityManager.createIndexer().startAndWait();
    log.info("All entities indexed");
  }
}

//https://github.com/thombergs/code-examples/tree/master/spring-boot/hibernate-search
      /*
  https://reflectoring.io/hibernate-search/

   private final EntityManager entityManager;

    public List<Post> getPostBasedOnWord(String word){
        FullTextEntityManager fullTextEntityManager =
                Search.getFullTextEntityManager(entityManager);

        QueryBuilder qb = fullTextEntityManager
                .getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Post.class)
                .get();

        Query foodQuery = qb.keyword()
                .onFields("body","hashTags")
                .matching(word)
                .createQuery();

        FullTextQuery fullTextQuery = fullTextEntityManager
                .createFullTextQuery(foodQuery, Post.class);
        return (List<Post>) fullTextQuery.getResultList();
    }*/
    /*
    *First, we create an object of FullTextEntityManager which is a wrapper over our EntityManager.
Next, we create QueryBuilder for the index on which we want to perform a search. We also need to pass the entity
* class object in it.
We use a QueryBuilder to build our Query.
Next, we make use of the keyword query keyword() which allows us to look for a specific word in a field or fields.
* Finally, we pass the word that we want to search in the matching function.
Lastly, we wrap everything in FullTextQuery and fetch the result list by calling getResultList().
One thing to note here is that although we are performing a query on Elasticsearch, Hibernate will still fire a query
*  on the database to fetch the full entity.

Which makes sense, because as we saw in the previous section we didn’t store all the fields of the Post entity in the
*  index and those fields still need to be retrieved. If we only want to fetch what’s stored in your index anyway and
*  think this database call is redundant, we can make use of a Projection.
    * */