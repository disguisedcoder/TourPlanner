package tourplanner.tourplanner.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;

public class ObjectMapperBean {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
