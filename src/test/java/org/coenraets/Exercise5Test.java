package org.coenraets;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.constructs.refreshahead.RefreshAheadCache;
import net.sf.ehcache.loader.CacheLoader;
import org.coenraets.service.Exercise5;
import org.coenraets.service.WineMysql;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.kubek2k.springockito.annotations.WrapWithSpy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;

/**
 * @author : Mathilde Lemee
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = { "classpath:applicationContext.xml" })
public class Exercise5Test {

  @Autowired
  private ApplicationContext applicationContext;

  @Resource
  private Exercise5 exercise5;

  @Resource
  @WrapWithSpy
  private WineMysql wineMysql;


  //specifier son nom en utilisant @Resource(name="toto")
  @Resource
  @WrapWithSpy
  CacheLoader myCacheLoader;


  @Before
  public void beforeTest() {
    exercise5.getCache().removeAll();
    reset(myCacheLoader);
    reset(wineMysql);

  }


  @Test
  public void when_refresh_ahead_then_cache_is_intance_of_RefreshAheadCache() throws InterruptedException {
    Ehcache cache = exercise5.getCache();
    assertThat(cache).isInstanceOf(RefreshAheadCache.class);
  }


  @Test
  public void when_refresh_ahead_then_data_is_updated_asynchronously() throws InterruptedException {
    Ehcache cache = exercise5.getCache();
    cache.put(new Element("1", "2"));
    Thread.sleep(1500);
    assertThat(cache.get("1").getObjectValue()).isEqualTo("2");   //premier acces - variable prise en cache
    Thread.sleep(1500);
    assertThat(cache.get("1").getObjectValue()).isNotEqualTo("2");   //deuxieme acces - variable prise en cache
    //pour info :
    System.out.println("Value : "+cache.get("1"));
  }


}
