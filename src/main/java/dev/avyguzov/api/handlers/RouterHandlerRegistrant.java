package dev.avyguzov.api.handlers;

import spark.Route;

import static spark.Spark.get;
import static spark.Spark.post;

public class RouterHandlerRegistrant {

    public void addPostRoute(String path, String acceptType, Route router) {
        post(path, acceptType, router);
    }

    public void addGetRoute(String path, String acceptType, Route router) {
        get(path, acceptType, router);
    }
}
