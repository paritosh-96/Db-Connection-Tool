package clari5.dsm;

public class DbDetails {
    public enum DB_TYPE {MYSQL, ORACLE, SQLSERVER}

    public DB_TYPE dbType;
    public String user;
    public String password;
    public String ip;
    public String port;
    public String sId;
    public boolean dbOperation;

    boolean setDbType(String dbType) {
        switch (dbType.trim().toUpperCase()) {
            case "MYSQL":
                this.dbType = DB_TYPE.MYSQL;
                break;
            case "SQLSERVER":
                this.dbType = DB_TYPE.SQLSERVER;
                break;
            case "ORACLE":
                this.dbType = DB_TYPE.ORACLE;
                break;
            default:
                System.out.println("Db type not supported. Please choose in [MYSQL, SQLSERVER, ORACLE]");
                return false;
        }
        return true;
    }

    String getCreateQuery() {
        switch (dbType) {
            case SQLSERVER:
            case ORACLE:
                return "CREATE TABLE DSM_SEED_TEST(id INT, name VARCHAR2(50))";
            case MYSQL:
                return "CREATE TABLE DSM_SEED_TEST(id INT, name VARCHAR(50))";
        }
        return null;
    }
}
