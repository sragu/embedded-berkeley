package org.sragu;


import com.sleepycat.je.*;

import java.io.File;

public class Db {
    private Database database;

    public Db(Database database) {
        this.database = database;
    }

    public Db put(String key, String value) {
        OperationStatus status = put(database, key, value);
        if (OperationStatus.SUCCESS != status) throw new RuntimeException("fail");
        return this;
    }

    public String get(String key) {
        return get(database, key);
    }

    private static OperationStatus put(Database db, String key, String value) {
        return db.put(transaction(), new StringValue(key), new StringValue(value));
    }

    private static String get(Database db, String key) {
        StringValue data = new StringValue();
        db.get(transaction(), new StringValue(key), data, LockMode.READ_UNCOMMITTED);
        return data.toString();
    }

    static Db database(String dbName) {
        return new Db(environment(environmentConfig()).openDatabase(transaction(), dbName, databaseConfig()));
    }

    private static EnvironmentConfig environmentConfig() {
        return new EnvironmentConfig().setAllowCreate(true);
    }

    static Db database(String dbName, DatabaseConfig dbConfig, EnvironmentConfig config) {
        return new Db(environment(config).openDatabase(transaction(), dbName, dbConfig));
    }

    private static Transaction transaction() {
        return null;
    }

    private static Environment environment(EnvironmentConfig config) {
        File dbFile = new File("/tmp/berkley_ho");
        return new Environment(dbFile, config);
    }

    private static DatabaseConfig databaseConfig() {
        return new DatabaseConfig()
                .setAllowCreate(true);
    }

    public String stats() {
        return database.getStats(new StatsConfig()).toString();
    }

    private static class StringValue extends DatabaseEntry {
        private StringValue() {
        }

        private StringValue(String value) {
            super(value.getBytes());
        }

        @Override
        public String toString() {
            return new String(getData());
        }
    }
}
