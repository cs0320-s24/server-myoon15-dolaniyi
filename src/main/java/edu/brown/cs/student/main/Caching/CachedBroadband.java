package edu.brown.cs.student.main.Caching;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import edu.brown.cs.student.main.server.BroadbandHandler;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * A class that wraps a BroadbandHandler instance and caches responses for efficiency.
 * This is an example of the proxy pattern; callers will interact
 * with the CachedBroadband, rather than the "real" data source.
 *
 */
public class CachedBroadband {
  private final BroadbandHandler wrappedBroadbandHandler;
  private final LoadingCache<String, String> cache;

  /**
   * Proxy class: wrap an instance of BroadbandHandler (of any kind) and cache its results.

   * @param toWrap the BroadbandHandler to wrap
   */
  public CachedBroadband(BroadbandHandler toWrap) {
    this.wrappedBroadbandHandler = toWrap;

    //Build the Cache
    this.cache =
        CacheBuilder.newBuilder()
            // How many entries maximum in the cache?
            .maximumSize(10)
            // How long should entries remain in the cache?
            .expireAfterWrite(1, TimeUnit.MINUTES)
            // Keep statistical info around for profiling purposes
            .recordStats()
            .build(
                // Strategy pattern: how should the cache behave when
                // it's asked for something it doesn't have?
                new CacheLoader<>() {
                  @Override
                  public String load(String key)
                      throws URISyntaxException, IOException, InterruptedException {
                    // If this isn't yet present in the cache, load it:
                    String[] keyValue = key.split(",");
                    System.out.println("No data Cached for: " + Arrays.toString(keyValue));
                    return wrappedBroadbandHandler.sendRequest(keyValue[0], keyValue[1]);
                  }
                });
  }


    /**
     * Initiates search on cache
     * @param stateID stateID to search for
     * @param countyID countyID to search for
     * @return
     */
  public String search(String stateID, String countyID) {

    String result = cache.getUnchecked(stateID + "," + countyID);
    return result;
  }
}
