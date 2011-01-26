package org.ow2.osgi.scanner.test;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Inject;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.ow2.chameleon.testing.helpers.OSGiHelper;
import org.ow2.osgi.scanner.IBundleScanner;
import org.ow2.osgi.scanner.test.matcher.IsIn;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.ops4j.pax.exam.CoreOptions.*;


/**
 * Created by IntelliJ IDEA.
 * User: guillaume
 * Date: 16/11/10
 * Time: 19:01
 * To change this template use File | Settings | File Templates.
 */
@RunWith( JUnit4TestRunner.class )
public class BundleScannerTestCase {

    @Configuration
    public static Option[] configure() {
        return options(
                mavenBundle("org.apache.felix", "org.apache.felix.ipojo").versionAsInProject(),
                mavenBundle("org.ow2.osgi", "bundle-scanner").versionAsInProject(),
                mavenBundle("org.ow2.osgi", "test-resources").versionAsInProject(),
                mavenBundle("org.ow2.chameleon.testing", "osgi-helpers").versionAsInProject(),
                felix()

        );
    }

    private OSGiHelper osgi;

    @Inject
    private BundleContext context;

    @Before
    public void setup() {
        osgi = new OSGiHelper(context);
    }

    @After
    public void tearDown() {
        osgi.dispose();
    }

    @Test
    public void testScanningOfTestResourcesBundle() throws Exception {

        final Map<String, List<String>> discovered = new HashMap<String, List<String>>();

        IBundleScanner scanner = (IBundleScanner) osgi.getServiceObject(IBundleScanner.class.getName(), null);
        final Bundle osgiBundle = osgi.getBundle("org.ow2.osgi.test-resources");

        scanner.scan(osgiBundle, new TestingCallback(osgiBundle, discovered));

        // Assertions ....
        List<String> base = discovered.get(".");
        assertThat(base, is(CoreMatchers.<Object>notNullValue()));
        assertThat("at-the-root.txt", isIn(base));
        assertThat("testing/sub/in-packages.txt", isIn(base));
        assertThat("testing/Hello.class", isIn(base));


        List<String> inner = discovered.get("test-inner.jar");
        assertThat(inner, is(CoreMatchers.<Object>notNullValue()));
        assertThat("other-at-root.txt", isIn(inner));
        assertThat("testing2/Bonjour.class", isIn(inner));

    }

    public static <T> Matcher<T> isIn(Collection<T> collection) {
        return new IsIn<T>(collection);
    }

}
