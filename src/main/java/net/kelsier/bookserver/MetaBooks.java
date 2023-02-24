package net.kelsier.bookserver;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MetaBooks extends Application<MetaBooksConfiguration> {

    public static void main(final String[] args) throws Exception {
        new MetaBooks().run(args);
    }

    @Override
    public String getName() {
        return "MetaBooks";
    }

    @Override
    public void initialize(final Bootstrap<MetaBooksConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final MetaBooksConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
