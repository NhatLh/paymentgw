package com.digitechlabs.paymentgw.dbpooling;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class BaseDAO
{
  protected Connection conn = null;

  protected Logger logger = null;

  public BaseDAO()
  {
    this.logger = Logger.getLogger(getClass());
  }

  public void closeResource(ResultSet rs)
  {
    try
    {
      if (rs != null) {
        rs.close();
//        rs = null;
      }
    }
    catch (SQLException e) {
      this.logger.warn(e);
    }
  }

  public void closeResource(PreparedStatement stmt)
  {
    try
    {
      if (stmt != null) {
        stmt.close();
//        stmt = null;
      }
    }
    catch (SQLException e) {
      this.logger.warn(e);
    }
  }

  public void closeResource(Connection conn)
  {
    if (conn != null)
      try {
        if (!conn.isClosed())
          conn.close();
      }
      catch (SQLException e)
      {
        this.logger.warn(e);
      } finally {
//        conn = null;
      }
  }

  protected void openDBConnect(String id)
    throws SQLException
  {
    closeDBConnect();
    this.conn = DBConnect.getConnection(id);
  }

  public void closeDBConnect()
  {
    closeResource(this.conn);
  }
}