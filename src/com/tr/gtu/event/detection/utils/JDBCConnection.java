package com.tr.gtu.event.detection.utils;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by TY.
 */
public class JDBCConnection
{
    private static Logger log = Logger.getLogger(JDBCConnection.class);

    private static Connection instance = null;
    private static Object lock = new Object();

    public static Connection instance()
    {
        if (instance == null)
        {
            synchronized (lock)
            {
                if (instance == null)
                {
                    try
                    {
                        Class.forName("com.mysql.jdbc.Driver");
                    }
                    catch (ClassNotFoundException e)
                    {
                        log.error("An error occurred while setting database driver...", e);
                    }

                    try
                    {
                        instance = DriverManager.getConnection("jdbc:mysql://localhost:3306/twitter", "root", "123456");
                    }
                    catch (SQLException e)
                    {
                        log.error("An error occurred while opening database connection...", e);
                    }
                }
            }
        }
        return instance;
    }

    public boolean isConnected()
    {
        try
        {
            if(instance.isClosed())
                return false;
            else
                return true;
        }
        catch (SQLException e)
        {
            log.error("An error occurred while checking database connection...", e);
            return false;
        }
    }

    public void disconnect()
    {
        try
        {
            instance.close();
        }
        catch (SQLException e)
        {
            log.error("An error occurred while closing database connection...", e);
        }
    }
}
