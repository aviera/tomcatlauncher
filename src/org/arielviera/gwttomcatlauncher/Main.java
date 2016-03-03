package org.arielviera.gwttomcatlauncher;

import com.google.gwt.core.ext.ServletContainerLauncher;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Main {

    public static final Logger LOGGER = Logger.getLogger(Main.class.getName());

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
                INSTANCE = aClass.newInstance();
            }
        } catch (Exception e){
            System.out.print(e);
            INSTANCE = null;
        }
    }

    public static void main(String[] args) throws Exception {
        Object tomcatContainer = INSTANCE.getClass().getMethods()[1].invoke(INSTANCE, null, 41234, new File("C:\\Users\\avd\\.IntelliJIdea14\\system\\gwt\\X901.210fd13\\frontend.ca76f84c\\run\\www"));
        Object getTomcat = tomcatContainer.getClass().getMethod("getTomcat").invoke(tomcatContainer);
        Object getServer = getTomcat.getClass().getMethod("getServer").invoke(getTomcat);
        getServer.getClass().getMethod("await").invoke(getServer);



    }
}
