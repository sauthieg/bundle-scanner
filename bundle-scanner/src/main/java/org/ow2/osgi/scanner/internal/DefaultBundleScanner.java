package org.ow2.osgi.scanner.internal;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Provides;
import org.osgi.framework.Bundle;
import org.ow2.osgi.scanner.IBundleScanner;
import org.ow2.osgi.scanner.IBundleScannerCallback;

@Component
@Provides
public class DefaultBundleScanner implements IBundleScanner {

    @Property(name = "scan.inner.jars", value = "true")
	private boolean scanInnerJars = true;
	
	public boolean isScanInnerJars() {
		return scanInnerJars;
	}

	public void setScanInnerJars(boolean scanInnerJars) {
		this.scanInnerJars = scanInnerJars;
	}

	public void scan(Bundle bundle, IBundleScannerCallback callback) {

		String classpath = (String) bundle.getHeaders().get("Bundle-ClassPath");

		// Iterates on all Bundle-ClassPath elements
		String[] paths = classpath.split(",");
		for (String path : paths) {
			
			// Trim any trailing/leading spaces
			path = path.trim();
			
			// Special handling for Jar resources
			if (path.endsWith(".jar")) {
				if (scanInnerJars) {
					handleJar(bundle, callback, path);
				} // else jar is ignored
				
			} else {
				// This is not a Jar file, so it must be a directory
				handleDirectory(bundle, callback, path);
			}
		}

	}

	private void handleDirectory(final Bundle bundle,
			                     final IBundleScannerCallback callback,
			                     final String path) {
		String searched = path;
		if (".".equals(path)) {
			// . (dot) is the root of the bundle
			searched = "/";
		}

        handleDirectoryRecursive(bundle, callback, path, searched);
	}


    private void handleDirectoryRecursive(final Bundle bundle,
                                          final IBundleScannerCallback callback,
                                          final String container,
                                          final String path) {

        // getEntryPath acts as a ls in a directory: sub-paths are not shown
		@SuppressWarnings("unchecked")
		Enumeration<String> paths = bundle.getEntryPaths(path);
		Collection<String> entries = Collections.list(paths);
		for (String entry : entries) {
            if (entry.endsWith("/")) {
                // Traverse sub directories
                handleDirectoryRecursive(bundle, callback, container, entry);
            } else {
                // Notify the found resource
                callback.onResource(bundle, container, entry);
            }

		}

    }

	private void handleJar(final Bundle bundle,
			               final IBundleScannerCallback callback,
			               final String path) {
		
		// Get the resource
		URL element = bundle.getResource(path);

		JarInputStream jis = null;
		try {
			jis = new JarInputStream(element.openStream());
			JarEntry entry = jis.getNextJarEntry();
			while (entry != null) {
				callback.onResource(bundle, path, entry.getName());
				entry = jis.getNextJarEntry();
			}
		} catch (IOException e) {
			// TODO Log a warning ....
		} finally {
			CloseUtils.close(jis);
		}
	}

}
