package orm.db;

public class DataSet {
    protected long id;

    public DataSet() {}

    public DataSet(final long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
