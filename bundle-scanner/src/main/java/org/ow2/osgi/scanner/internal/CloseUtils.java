package org.ow2.osgi.scanner.internal;

import java.io.Closeable;
import java.io.IOException;

public class CloseUtils {

    /**
     * Close all given objects, ignoring IOException that may be thrown.
     * @param closeables Objects to be closed
     */
	public static void close(Closeable... closeables) {
		if (closeables != null && closeables.length != 0) {
			for (Closeable closeable : closeables) {
				if (closeable != null) {
					try {
						closeable.close();
					} catch (IOException e) {
						// Ignored
					}
				}
			}
		}
	}
}
