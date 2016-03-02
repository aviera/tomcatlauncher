package org.arielviera.gwttomcatlauncher;

import com.google.gwt.core.ext.ServletContainer;
import com.google.gwt.core.ext.ServletContainerLauncher;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.net.BindException;

/**
 * @author ariel.viera@gmail.com (Ariel Viera)
 */
public class TomcatLauncher extends ServletContainerLauncher {
    @Override
    public ServletContainer start(TreeLogger logger, final int port, File appRootDir) throws BindException, Exception {
        final Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);

        tomcat.start();

        final Context context = tomcat.addWebapp("/", appRootDir.getPath());
        return new ServletContainer() {
            @Override
            public int getPort() {
                return port;
            }

            @Override
            public void refresh() throws UnableToCompleteException {
                context.reload();
            }

            @Override
            public void stop() throws UnableToCompleteException {
                try {
                    tomcat.stop();
                } catch (LifecycleException e) {
                    throw new UnableToCompleteException();
                }
            }
        };

    }
}
