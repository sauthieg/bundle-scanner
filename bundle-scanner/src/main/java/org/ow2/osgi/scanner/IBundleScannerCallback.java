package org.ow2.osgi.scanner;

import org.osgi.framework.Bundle;

/**
 * This callback will be notified for each resource available discovered by the scanner.
 */
public interface IBundleScannerCallback {

	/**
	 * Notify that a resource has been found.
	 * The resources will be accessible from the Bundle's ClassLoader when resolved.
	 * 
	 * @param bundle Scanned Bundle (may or may not be resolved).
	 * @param container Name of the scanned Bundle-ClassPath entry
	 *        (ex: {@code .}, {@code WEB-INF/classes/}, {@code inner.jar}, ...)
	 * @param resourceName discovered resource inside the container.
	 */
	void onResource(Bundle bundle, String container, String resourceName);
}
