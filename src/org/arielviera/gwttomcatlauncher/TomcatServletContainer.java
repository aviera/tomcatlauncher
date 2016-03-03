package org.arielviera.gwttomcatlauncher;

import com.google.gwt.core.ext.ServletContainer;
import com.google.gwt.core.ext.UnableToCompleteException;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletException;
import java.io.File;

/**
 * @author ariel.viera@gmail.com (Ariel Viera)
 */
public class TomcatServletContainer extends ServletContainer {

    private final Tomcat tomcat;
    private final int port;
    private final Context context;


    public TomcatServletContainer(int port, String s, String path) throws ServletException, LifecycleException {
        tomcat = new Tomcat();
        this.port = port;
        File file = new File(Tomcat.class.getProtectionDomain().getCodeSource().getLocation().getFile());
        File basedir = file.getParentFile().getParentFile();
        tomcat.setBaseDir(basedir.toString());
        tomcat.setPort(port);
        context = tomcat.addWebapp(s, path);
        context.setParentClassLoader(TomcatServletContainer.class.getClassLoader());
        tomcat.start();
    }

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

    public Tomcat getTomcat() {
        return tomcat;
    }
}
