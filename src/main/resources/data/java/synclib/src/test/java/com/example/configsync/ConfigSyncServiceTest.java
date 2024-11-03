
package com.example.configsync;

import org.junit.jupiter.api.Test;
import java.net.URI;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ConfigSyncServiceTest {

    @Test
    public void testStartAndStop() {
        URI uri = URI.create("http://example.com/config.json");
        ConfigSyncService service = new ConfigSyncService(uri, Paths.get("config.json"), 60000);
        
        assertDoesNotThrow(() -> {
            service.start();
            service.stop();
        });
    }
}
