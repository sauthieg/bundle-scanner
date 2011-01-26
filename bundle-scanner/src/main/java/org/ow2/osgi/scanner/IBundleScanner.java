package org.ow2.osgi.scanner;

import org.osgi.framework.Bundle;

/**
 * A BundleScanner is responsible to traverse a Bundle and notify
 * a callback for each resource found.
 * Each discovered resource will be available through Bundle ClassLoader
 * when Bundle will be resolved.
 */
public interface IBundleScanner {

    /**
     * Scan the given Bundle for retrievable resources
     * @param bundle scanned bundle
     * @param callback notified for each discovered resource
     */
	void scan(Bundle bundle, IBundleScannerCallback callback);
}
