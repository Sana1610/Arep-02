package arep.webserver.routes;

import arep.webserver.http.Request;
import arep.webserver.http.Response;

@FunctionalInterface
public interface Route {
    String handle(Request request, Response response);
}