package org.sragu;

import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.EnvironmentConfig;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DbStats {

    public static void main(String[] args) throws IOException {
        Db db = Db.database("zips", readOnlyDb(), readOnlyEnvironment());
        accessible_stats(db, Executors.newCachedThreadPool());
    }

    private static void accessible_stats(final Db db, ExecutorService executorService) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/stats", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String stats = db.stats();

                exchange.sendResponseHeaders(200, stats.length());
                exchange.getResponseBody().write(stats.getBytes());
                exchange.close();
            }
        });
        server.setExecutor(executorService);
        server.start();
    }

    private static EnvironmentConfig readOnlyEnvironment() {
        return new EnvironmentConfig().setAllowCreate(true).setReadOnly(true);
    }

    private static DatabaseConfig readOnlyDb() {
        return new DatabaseConfig().setReadOnly(true);
    }
}
