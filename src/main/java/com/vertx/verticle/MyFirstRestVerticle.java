package com.vertx.verticle;

import com.vertx.domain.Employee;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;


import java.util.ArrayList;
import java.util.List;

public class MyFirstRestVerticle extends AbstractVerticle {

    private  List<Employee> employees = new ArrayList<>();

    private void createServer(Router router, Future future) {
        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(
                        config().getInteger("http.port", 8080),
                        result -> {
                            if (result.succeeded()) {
                                future.complete();
                            } else {
                                future.fail(result.cause());
                            }
                        }
                );
    }

    public void start(Future<Void> future) {

        createSomeEmployees();
        Router router = Router.router(vertx);
        router.route("/").handler(r ->{
            r.response().putHeader("Content-type",
                    "text/html")
                    .end("<h1> This is an example rest Application </h1>");
        });

        router.route("/v1/employees*").handler(BodyHandler.create());

        router.get("/v1/employees").handler(this::getAllEmployees);

        router.post("/v1/employees").handler(this::addOne);

        router.get("/v1/employees/:id").handler(this::getOne);

        createServer(router, future);

    }

    public void getOne(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        Employee e1 = new Employee();

        for (Employee employee : employees) {
            if (id.equalsIgnoreCase(employee.getId())) {
                e1 = employee;
                break;
            }
        }
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(e1));
    }

    public void addOne(RoutingContext routingContext) {
        Employee employee = Json.decodeValue(routingContext.getBodyAsString(), Employee.class);
        employees.add(employee);
        routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(employee));
    }

    public void getAllEmployees(RoutingContext routingContext) {
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(employees));
    }

   public void createSomeEmployees() {
       Employee e1 = Employee.builder().
               id("ABC").name("John").city("Chennai").build();
       employees.add(e1);

       Employee e2 = Employee.builder().id("DEF")
               .name("Jagan").city("Bengaluru").build();
       employees.add(e2);
   }



}
