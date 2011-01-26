package org.ow2.osgi.scanner.internal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;

import org.osgi.framework.Bundle;
import org.ow2.osgi.scanner.IBundleScannerCallback;
import org.testng.annotations.Test;

public class DefaultBundleScannerTestCase {

	@Test
	public void testSimpleUsage() throws Exception {
		
		// Values
		Properties props = new Properties();
		props.setProperty("Bundle-ClassPath", ".");
		Collection<String> list = new ArrayList<String>();
		list.add("index.html");
		Enumeration<String> paths = Collections.enumeration(list);
		
		// Mocks
		Bundle bundle = mock(Bundle.class);
		
		// Behaviors
		when(bundle.getHeaders()).thenReturn(props);
		when(bundle.getEntryPaths("/")).thenReturn(paths);
		
		DefaultBundleScanner scanner = new DefaultBundleScanner();
		scanner.scan(bundle, new IBundleScannerCallback() {
			
			public void onResource(Bundle bundle, String container, String resourceName) {
				assertEquals(container, ".");
				assertEquals(resourceName, "index.html");
			}
		});
	}

	@Test
	public void testInnerJarUsage() throws Exception {
		
		// Values
		Properties props = new Properties();
		props.setProperty("Bundle-ClassPath", "data.jar");
		URL dataJarUrl = getClass().getResource("/data.jar");
		
		// Mocks
		Bundle bundle = mock(Bundle.class);
		
		// Behaviors
		when(bundle.getHeaders()).thenReturn(props);
		when(bundle.getResource("data.jar")).thenReturn(dataJarUrl);
		
		DefaultBundleScanner scanner = new DefaultBundleScanner();
		scanner.scan(bundle, new IBundleScannerCallback() {
			
			public void onResource(Bundle bundle, String container, String resourceName) {
				assertEquals(container, "data.jar");
				assertEquals(resourceName, "data.txt");
			}
		});
	}
}
