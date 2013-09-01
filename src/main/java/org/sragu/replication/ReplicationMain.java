package org.sragu.replication;

import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.dbi.ReplicatedDatabaseConfig;

import java.io.File;

public class ReplicationMain {

    public static void main(String[] args) {
        EnvironmentConfig config = new EnvironmentConfig();
        config.setAllowCreate(true);

        ReplicatedDatabaseConfig databaseConfig = new ReplicatedDatabaseConfig();
        Environment secondary = new Environment(new File("/tmp/berkeley_sec"), config);

    }
}
