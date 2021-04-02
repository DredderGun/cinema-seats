package dev.avyguzov;

import com.google.inject.*;
import dev.avyguzov.api.routes.Routes;
import dev.avyguzov.db.Database;
import dev.avyguzov.service.MessageService;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends AbstractModule {
    public enum Profile {
        TEST, PROD
    }
    public static Profile currentProfile;

    @Provides
    static MessageService getMessageService() throws IOException {
        return new MessageService("./application-" + currentProfile.name().toLowerCase() + ".properties");
    }

    public static void main(String[] args) throws SQLException, IOException {
        if (args.length == 0) {
            currentProfile = Profile.PROD;
        } else if (args[0].equalsIgnoreCase("test")) {
            currentProfile = Profile.TEST;
        }

        Injector injector = Guice.createInjector(new Main());
        Routes.establishRoutes(injector);
        Database.initDb(injector.getInstance(Database.class));
    }
}
