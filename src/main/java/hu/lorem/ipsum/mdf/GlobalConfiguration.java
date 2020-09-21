package hu.lorem.ipsum.mdf;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import hu.lorem.ipsum.mdf.fetch.JedisCachingMovieListFetcher;
import hu.lorem.ipsum.mdf.fetch.MovieListFetcher;
import hu.lorem.ipsum.mdf.fetch.OmdbMovieListFetcher;
import hu.lorem.ipsum.mdf.fetch.TmdbMovieListFetcher;
import hu.lorem.ipsum.mdf.mock.MockFactory;
import redis.clients.jedis.Jedis;

@Configuration
public class GlobalConfiguration {
    
    @Value("${app.useCache:true}")
    private boolean useCache;
    
    @Value("${app.omdbApiKey:}")
    private String omdbApiKey;
    
    @Value("${app.tmdbApiKey:}")
    private String tmdbApiKey;
    
    @Value("${app.redisHost:localhost}")
    private String redisHost;
    
    
    @Bean("supportedMovieListFetchers")
    public Map<String, MovieListFetcher> collectSupportedMovieListFetchers(
            ObjectMapper objectMapper) {
        
        return Map.of(
                "omdb", cachingIfEnabled(createOmdbMovieListFetcher(), "omdb", objectMapper),
                "tmdb", cachingIfEnabled(createTmdbMovieListFetcher(), "tmdb", objectMapper),
                "mock", new MockFactory().createMockMovieListFetcher());
    }
    
    private MovieListFetcher cachingIfEnabled(
            MovieListFetcher innerFetcher, String cacheName, ObjectMapper objectMapper) {
        
        if (useCache) {
            return JedisCachingMovieListFetcher.builder()
                    .withInnerFetcher(innerFetcher)
                    .withJedis(createJedis())
                    .withCacheName(cacheName)
                    .withObjectMapper(objectMapper)
                    .build();
        } else {
            return innerFetcher;
        }
    }

    @Bean
    public OmdbMovieListFetcher createOmdbMovieListFetcher() {
        return new OmdbMovieListFetcher(omdbApiKey);
    }

    @Bean
    public TmdbMovieListFetcher createTmdbMovieListFetcher() {
        return new TmdbMovieListFetcher(tmdbApiKey);
    }

    @Bean
    public Jedis createJedis() {
        return new Jedis(redisHost);
    }
    
}
