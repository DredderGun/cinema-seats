package dev.avyguzov.api.routes;

import static spark.Spark.get;
import static spark.Spark.post;

import com.google.inject.Injector;

public class Routes {
    public static void establishRoutes(Injector injector) {
        get("/get-all-seats", "application/json", injector.getInstance(GetAllSeats.class));
        post("/take-seats", "application/json", injector.getInstance(TakeSeat.class));
    }
}
