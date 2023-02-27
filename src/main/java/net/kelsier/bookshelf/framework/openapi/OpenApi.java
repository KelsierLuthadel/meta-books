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

package net.kelsier.bookshelf.framework.openapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.dropwizard.setup.Environment;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import net.kelsier.bookshelf.framework.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Register resources for swagger using scanning
 *
 * @author Kelsier Luthadel
 * @version 1.0.2
 */
public class OpenApi {

    private OpenApi() {
        throw new IllegalStateException("Utility class. Should not be instantiated.");
    }
    /**
     * Initialize OpenAPI with a given secured resource
     *
     * @param environment Dropwizard Environment
     * @param docRoot Document root
     * @param resources Resource paths to scan
     */
    public static void configure(final Environment environment, final String docRoot, final Set<String> resources) {
            final Info info = new Info()
                    .title("Kelsier: Book Server")
                    .description(Helper.readFromInputStream("assets/OpenApi"))
                    //  .termsOfService("http://swagger.io/terms/")
                    .contact(new Contact()
                            .email("kelsier.luthadel@protonmail.com"))
                    .license(new License()
                            .name("Apache 2.0")
                            .url("http://www.apache.org/licenses/LICENSE-2.0.html"));

            final OpenAPI oas = new OpenAPI();
            oas.info(info);

            final List<Server> servers = new ArrayList<>();
            servers.add(new Server().url(docRoot));


            oas.servers(servers);
            final SwaggerConfiguration oasConfig = new SwaggerConfiguration()
                    .openAPI(oas)
                    .prettyPrint(true)
                    .resourcePackages(resources);

            environment.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
            environment.jersey().register(new OpenApiResource().openApiConfiguration(oasConfig));
    }
}
