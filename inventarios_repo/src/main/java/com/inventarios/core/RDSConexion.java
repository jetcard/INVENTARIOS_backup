
package com.inventarios.core;

import com.amazonaws.secretsmanager.sql.AWSSecretsManagerPostgreSQLDriver;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class RDSConexion {
  public static final String DATABASE_NAME_ENV = "DBName";
  public static final String POSTGRES_SECRET_ARN_ENV = "RDSSecretArn";
  public static final String DB_ENDPOINT = "DBEnpoint";
  public static final String DB_USER = "user";
  public static final String DB_PASS = "pass";

  static final Properties info;

  static {
    System.setProperty("software.amazon.awssdk.http.service.impl",
      "software.amazon.awssdk.http.urlconnection.UrlConnectionSdkHttpService");
    var driver = new AWSSecretsManagerPostgreSQLDriver();
    info = new Properties();
    info.put("user", rdsSecretArn());
  }
  public static Connection getConnection() {
    try {
      final String dbURL = "jdbc-secretsmanager:postgresql://" + rdsEndpoint() + "/" + rdsDatabase();
      return DriverManager.getConnection(dbURL, info);
    } catch (SQLException se) {
      System.err.println(se.getMessage());
      return null;
    }
  }

  public static String rdsDatabase() {
    //return "basededatos";
    return System.getenv(DATABASE_NAME_ENV);
  }

  public static String rdsSecretArn() {
    //return "arn:aws:secretsmanager:ap-southeast-2:905418357889:secret:RDSSecret-MmhlwJYSDon9-FpMoqC";
    return System.getenv(POSTGRES_SECRET_ARN_ENV);
  }

  public static String rdsEndpoint() {
    //return "sam-app-rdsinstance-7vei4ommu3iu.ctcosak24j76.ap-southeast-2.rds.amazonaws.com";
    return System.getenv(DB_ENDPOINT);
  }

  public static String rdsUserDB() {
    //return "postgres";
    return System.getenv(DB_USER);
  }

  public static String rdsPassDB() {
    //return "1234567890";
    return System.getenv(DB_PASS);
  }

  public static DSLContext getDSL() {
    return (DSL.using(getConnection(), SQLDialect.POSTGRES));
  }

}