package edu.umass.ciir.judge;

/**
 * @author jfoley.
 */
public class Judgment {
  public static final class Key {
    public final String user;
    public final String qid;
    public final String docid;

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Key key = (Key) o;

      if (!docid.equals(key.docid)) return false;
      if (!qid.equals(key.qid)) return false;
      if (!user.equals(key.user)) return false;

      return true;
    }

    @Override
    public int hashCode() {
      int result = user.hashCode();
      result = 31 * result + qid.hashCode();
      result = 31 * result + docid.hashCode();
      return result;
    }

    public Key(String user, String qid, String docid) {
      this.user = user;
      this.qid = qid;
      this.docid = docid;
    }
  }

  public static final class Value {
    public final String note;
    public final String judgment;

    public Value(String judgment, String note) {
      this.judgment = judgment;
      this.note = note;
    }
  }

  public final Key key;
  public final Value value;

  public Judgment(String user, String qid, String docid, String judgment, String note) {
    this.key = new Key(user, qid, docid);
    this.value = new Value(judgment, note);
  }

  public Judgment(Key k, Value v) {
    this.key = k;
    this.value = v;
  }


  public String toTrecQrel() {
    return String.format("%s 0 %s %s", key.qid, key.docid, value.judgment);
  }
  public String toTSV() {
    StringBuilder sb = new StringBuilder();
    sb.append(key.user).append('\t')
        .append(key.qid).append('\t')
        .append(key.docid).append('\t')
        .append(value.judgment).append('\t')
        .append(value.note);

    return sb.toString();
  }
}
