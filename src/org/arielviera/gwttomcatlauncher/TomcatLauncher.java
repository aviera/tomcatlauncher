package org.arielviera.gwttomcatlauncher;

import com.google.gwt.core.ext.ServletContainer;
import com.google.gwt.core.ext.ServletContainerLauncher;
import com.google.gwt.core.ext.TreeLogger;

import java.io.File;
import java.net.BindException;

/**
 * @author ariel.viera@gmail.com (Ariel Viera)
 */
public class TomcatLauncher extends ServletContainerLauncher {
    @Override
    public ServletContainer start(TreeLogger logger, final int port, File appRootDir) throws BindException, Exception {
        System.out.println("port " + port + " appRootDir " + appRootDir);
        return new TomcatServletContainer(port, "/", appRootDir.getPath());
    }

    public static void main(String[] args) throws Exception {
        TomcatLauncher tc = new TomcatLauncher();
        tc.start(null, 41233, new File("C:\\Users\\avd\\.IntelliJIdea14\\system\\gwt\\X901.210fd13\\frontend.ca76f84c\\run\\www"));
    }
}
