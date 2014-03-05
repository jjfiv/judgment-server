package edu.umass.ciir.judge;

import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author jfoley.
 */
public class LocalDBBackend implements StorageBackend {
  Connection conn;

  public LocalDBBackend(String path) throws ClassNotFoundException, SQLException {
    Class.forName("org.h2.Driver");
    this.conn = null;
    open(path);
  }

  public void open(String path) throws SQLException {
    conn = DriverManager.getConnection("jdbc:h2:" + path, "sa", "");
  }

  public void close() throws SQLException {
    if(conn != null) {
      conn.close();
    }
  }

  @Override
  public void finalize() throws SQLException {
    close();
  }

  private Judgment.Value find(Judgment.Key key) {
    Judgment.Value value = null;
    try {
      PreparedStatement stmt = conn.prepareStatement("select judgment, note from judgments where user=? and qid=? and doc=?");
      stmt.setString(1, key.user);
      stmt.setString(2, key.qid);
      stmt.setString(3, key.docid);

      ResultSet rs = stmt.executeQuery();
      while(rs.next()) {
        // should only be one
        assert(value == null) : "Expected only one judgment for user,qid,docid key";
        String judgment = rs.getString("judgment");
        String note = rs.getString("note");
        value = new Judgment.Value(judgment, note);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return value;
  }

  @Override
  public Judgment getJudgment(String user, String qid, String doc) {
    Judgment.Key key = new Judgment.Key(user, qid, doc);
    Judgment.Value value = find(key);
    if(value == null) {
      return null;
    } else {
      return new Judgment(key, value);
    }
  }

  @Override
  public void submit(Judgment judgment) {
    if(!(update(judgment) || insert(judgment))) {
      throw new RuntimeException("Couldn't submit a judgment. Weird.");
    }
  }

  private boolean update(Judgment judgment) {
    try {
      PreparedStatement stmt = conn.prepareStatement("update judgments set judgment=?, note=? wherewhere user=? and qid=? and doc=?");
      stmt.setString(1, judgment.value.judgment);
      stmt.setString(2, judgment.value.note);
      stmt.setString(3, judgment.key.user);
      stmt.setString(4, judgment.key.qid);
      stmt.setString(5, judgment.key.docid);

      int rowsUpdated = stmt.executeUpdate();
      return rowsUpdated > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  private boolean insert(Judgment judgment) {
    try {
      PreparedStatement stmt = conn.prepareStatement("insert into judgments (user,qid,doc,judgment,note) values (?,?,?,?,?)");
      stmt.setString(1, judgment.key.user);
      stmt.setString(2, judgment.key.qid);
      stmt.setString(3, judgment.key.docid);
      stmt.setString(4, judgment.value.judgment);
      stmt.setString(5, judgment.value.note);

      int rowsUpdated = stmt.executeUpdate();
      return rowsUpdated > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
}
