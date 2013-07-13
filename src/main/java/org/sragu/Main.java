package org.sragu;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws IOException {
        ExecutorService service = Executors.newFixedThreadPool(1000);
        Db db = Db.database("zips");

        while (true) stream_data_into_database(db, service);
    }

    private static void stream_data_into_database(final Db db, ExecutorService service) {
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
                db.get(random);
            }
        });
    }

}
