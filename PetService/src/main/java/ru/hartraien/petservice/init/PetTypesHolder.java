package ru.hartraien.petservice.init;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@PropertySource(value = "classpath:pet-info.yml", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "pet-constants")
public class PetTypesHolder {
    private Set<String> types;

    public PetTypesHolder(Set<String> types) {
        this.types = types;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }
}
