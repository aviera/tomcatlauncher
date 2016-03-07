package org.arielviera.gwttomcatlauncher;

import com.google.gwt.core.ext.ServletContainer;
import com.google.gwt.core.ext.ServletContainerLauncher;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;

import java.io.File;
import java.io.FileFilter;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author ariel.viera@gmail.com (Ariel Viera)
 */
public class TCLauncher extends ServletContainerLauncher {

    public static final Logger LOGGER = Logger.getLogger(TCLauncher.class.getName());

    private static volatile Object INSTANCE = null;

    static {
        try {
            File f = new File(ServletContainerLauncher.class.getProtectionDomain().getCodeSource().getLocation().getFile());

            File gwtlocation = f.getParentFile();
            LOGGER.info("Detected gwt in " +gwtlocation);
            File[] tomcats = gwtlocation.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isDirectory() && pathname.getName().contains("tomcat");
                }
            });

            if(tomcats.length==0){
                LOGGER.warning("Not detected tomcat in "+gwtlocation+ ". Please copy a tomcat installation into the gwt folder");
            } else {
                File tomcat = tomcats[0];
                List<URL> collect = null;
                collect = Files.walk(tomcat.toPath()).filter(p -> p.toString().endsWith(".jar"))
                        .map(Path::toUri)
                        .map(u -> {
                            try {
                                return u.toURL();
                            } catch (MalformedURLException e) {
                                return null;
                            }
                        }).collect(Collectors.toList());

                File file = new File(gwtlocation, "tomcatlauncher.jar");
                if(!file.exists()){
                    LOGGER.warning("tomcatlauncher not found. Download it from https://github.com/aviera/tomcatlauncher/wiki/tomcatlauncher.jar ");
                }
                collect.add(file.toURI().toURL());
                collect.add(f.toURI().toURL());

                URL[] urls = collect.toArray(new URL[]{});

                ClassLoader cl = new URLClassLoader(urls, null);

                Class<?> aClass = null;
                aClass = cl.loadClass("org.arielviera.gwttomcatlauncher.TomcatLauncher");
                INSTANCE =  aClass.newInstance();
            }
        } catch (Exception e){
            System.out.print(e);
            INSTANCE = null;
        }
    }
//
//    public static void main(String[] args) throws Exception {
//        ServletContainer start =INSTANCE.start(null, 41234, new File("C:\\Users\\avd\\.IntelliJIdea14\\system\\gwt\\X901.210fd13\\frontend.ca76f84c\\run\\www"));
//
//        Object getTomcat = start.getClass().getMethod("getTomcat").invoke(start);
//        Object getServer = getTomcat.getClass().getMethod("getServer").invoke(getTomcat);
//        getServer.getClass().getMethod("await").invoke(getServer);
//    }

    @Override
    public ServletContainer start(TreeLogger logger, int port, File appRootDir) throws BindException, Exception {

        if(port == 0) {
            ServerSocket s = new ServerSocket(0);
            port = s.getLocalPort();
            System.out.println("listening on port: " + s.getLocalPort());
            s.close();
        }
//        return INSTANCE.start(logger, port, appRootDir);
        final Object tomcatContainer = INSTANCE.getClass().getMethods()[1].invoke(INSTANCE, null, port, appRootDir);
        return new ServletContainer() {
            @Override
            public int getPort() {
                try {
                    return (int)tomcatContainer.getClass().getMethod("getPort").invoke(tomcatContainer);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void refresh() throws UnableToCompleteException {
                try {
                    tomcatContainer.getClass().getMethod("refresh").invoke(tomcatContainer);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void stop() throws UnableToCompleteException {
                try {
                    tomcatContainer.getClass().getMethod("stop").invoke(tomcatContainer);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
