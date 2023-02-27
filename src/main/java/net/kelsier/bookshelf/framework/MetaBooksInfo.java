/*
 * Copyright (c) 2023 Kelsier Luthadel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.kelsier.bookshelf.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Manage and store data from the MANIFEST.MF resource
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
@SuppressWarnings("unused")
public class MetaBooksInfo {
    private static final String META_INF_MANIFEST_MF = "META-INF/MANIFEST.MF";
    private static final Logger LOGGER = LoggerFactory.getLogger(MetaBooksInfo.class);
    private String title = "";
    private String url = "";
    private String org = "";
    private String version = "";
    private String javaVersion = "";
    private String buildTime = "";

    /**
     * Constructor
     *
      * @param classLoader Class loader, used to access the resource files
     */
    public MetaBooksInfo(final ClassLoader classLoader)  {
        try {
            final Enumeration<URL> resources = classLoader.getResources(META_INF_MANIFEST_MF);

            while (resources.hasMoreElements()) {
                final Manifest manifest = new Manifest(resources.nextElement().openStream());
                final Attributes attributes = manifest.getMainAttributes();

                if ("MetaBooks".equals(attributes.getValue("Kelsier-Title"))) {
                    title = attributes.getValue("Title");
                    url = attributes.getValue("Url");
                    org = attributes.getValue("Org");
                    version = attributes.getValue("Version");
                    javaVersion = attributes.getValue("Build-Jdk-Spec");
                    buildTime = attributes.getValue("Build-Time");
                }
            }
        } catch (IOException e) {
            LOGGER.debug("Unable to read resources {}", e.getMessage());
        }
    }

    /**
     * Return the product version
     *
     * @return A string containing the version from the Manifest file
     */
    public String getVersion() {
        return version;
    }

    /**
     * Return the product title
     *
     * @return A string containing the title from the Manifest file
     */
    public String getTitle() {
        return title;
    }

    /**
     * Return the product url
     *
     * @return A string url the version from the Manifest file
     */
    public String getUrl() {
        return url;
    }

    /**
     * Return the product org
     *
     * @return A string containing the organization from the Manifest file
     */
    public String getOrg() {
        return org;
    }

    /**
     * Return the java version
     *
     * @return A string containing the java version from the Manifest file
     */
    public String getJavaVersion() {
        return javaVersion;
    }

    /**
     * Return the date-time when the application was built
     *
     * @return A string containing the date-time when the application was built
     */
    public String getBuildTime() {
        return buildTime;
    }
}
