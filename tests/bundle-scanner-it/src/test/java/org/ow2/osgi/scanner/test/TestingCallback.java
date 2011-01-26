package org.ow2.osgi.scanner.test;

import org.osgi.framework.Bundle;
import org.ow2.osgi.scanner.IBundleScannerCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

/**
* Created by IntelliJ IDEA.
* User: guillaume
* Date: 26/01/11
* Time: 22:15
* To change this template use File | Settings | File Templates.
*/
class TestingCallback implements IBundleScannerCallback {
    private final Bundle osgiBundle;
    private final Map<String, List<String>> discovered;

    public TestingCallback(Bundle osgiBundle, Map<String, List<String>> discovered) {
        this.osgiBundle = osgiBundle;
        this.discovered = discovered;
    }

    public void onResource(Bundle bundle, String container, String resourceName) {

        assertThat(bundle, is( sameInstance(osgiBundle) ));

        List<String> resources = discovered.get(container);
        if (resources == null) {
            resources = new ArrayList<String>();
            discovered.put(container, resources);
        }

        resources.add(resourceName);
    }
}
