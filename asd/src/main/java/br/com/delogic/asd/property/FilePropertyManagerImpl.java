package br.com.delogic.asd.property;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.core.io.Resource;

import br.com.delogic.asd.util.Has;

public class FilePropertyManagerImpl implements PropertyManager {

    static class PropertyConfigurer {

        private final AsyncCallback<Map<String, String>> callback;
        private final String[] properties;

        public PropertyConfigurer(AsyncCallback<Map<String, String>> callback, String[] properties) {
            this.callback = callback;
            this.properties = properties;
        }

        public void configureProperty(Properties propertiesLoaded) {
            Map<String, String> propertiesRequired = new HashMap<String, String>();
            for (String prop : properties) {
                if (Has.content(propertiesLoaded.get(prop))) {
                    propertiesRequired.put(prop, propertiesLoaded.getProperty(prop));
                } else {
                    throw new IllegalArgumentException("Property " + prop + " was not configured");
                }
            }
            callback.onSuccess(propertiesRequired);
        }

    }

    private final List<PropertyConfigurer> callbacks;
    private Properties propertiesLoaded;

    public FilePropertyManagerImpl(Resource resource) {
        callbacks = new ArrayList<FilePropertyManagerImpl.PropertyConfigurer>();
        propertiesLoaded = new Properties();
        try {
            propertiesLoaded.load(resource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Error loading the properties file", e);
        }
    }

    @Override
    public void getProperty(AsyncCallback<Map<String, String>> callback, String... properties) {
        PropertyConfigurer config = new PropertyConfigurer(callback, properties);
        callbacks.add(config);
        config.configureProperty(propertiesLoaded);
    }

}
