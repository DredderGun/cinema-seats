package dev.avyguzov;

import com.google.inject.*;
import com.google.inject.multibindings.Multibinder;
import dev.avyguzov.api.config.RoutesMapper;
import dev.avyguzov.api.handlers.GetAllSeats;
import dev.avyguzov.api.handlers.TakeSeat;
import spark.Route;

import java.io.IOException;

import static spark.Spark.init;

public class Main extends AbstractModule {
    public enum Profile {
        TEST, PROD
    }
    public static Profile currentProfile;

    @Provides
    static ConfigsReader getConfigsReader() throws IOException {
        return new ConfigsReader("./application-" + currentProfile.name().toLowerCase() + ".properties");
    }

    public void configure() {
        Multibinder<Route> routeMultiBinder = Multibinder.newSetBinder(binder(), Route.class);
        routeMultiBinder.addBinding().to(TakeSeat.class);
        routeMultiBinder.addBinding().to(GetAllSeats.class);

        // to initialize all routes
        bind(RoutesMapper.class).asEagerSingleton();
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            currentProfile = Profile.PROD;
        } else if (args[0].equalsIgnoreCase("test")) {
            currentProfile = Profile.TEST;
        }

        init();
        Guice.createInjector(new Main());
    }
}
