package hu.lorem.ipsum.mdf;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MovieDataFetcherApplicationTests {

    @Autowired
    GlobalConfiguration configuration;
    
    @Test
    void contextLoads() {
        assertThat(configuration).isNotNull();
    }
    
}
