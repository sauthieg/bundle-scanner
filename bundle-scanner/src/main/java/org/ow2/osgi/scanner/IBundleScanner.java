package org.ow2.osgi.scanner;

import org.osgi.framework.Bundle;

public interface IBundleScanner {

	void scan(Bundle bundle, IBundleScannerCallback callback);
}
