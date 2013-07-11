package org.sragu;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws IOException {
        final Db db = Db.database("zips")
                .put("addison", "75001")
                .put("plano", "75024");

        ExecutorService service = Executors.newFixedThreadPool(1000);
        accessible_stats(db, service);
        stream_data_into_database(db, service);
    }

    private static void stream_data_into_database(final Db db, ExecutorService service) {

        while (true) {
            final String random = UUID.randomUUID().toString();

            service.submit(new Runnable() {
                @Override
                public void run() {
                    db.put(random, random);
                }
            });

            service.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println(db.get(random));
                }
            });
        }
    }

    private static void accessible_stats(final Db db, ExecutorService executorService) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/stats", new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                String stats = db.stats();
                httpExchange.sendResponseHeaders(200, stats.length());
                OutputStream responseBody = httpExchange.getResponseBody();
                responseBody.write(stats.getBytes());
                responseBody.close();
                httpExchange.close();
            }
        });
        server.setExecutor(executorService);
        server.start();
    }
}
