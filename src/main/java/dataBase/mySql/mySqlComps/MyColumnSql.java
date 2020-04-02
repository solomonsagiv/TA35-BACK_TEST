package dataBase.mySql.mySqlComps;

import serverObjects.BASE_CLIENT_OBJECT;

public abstract class MyColumnSql< T > {

    public static final int STRING = 0;
    public static final int DOUBLE = 1;
    public static final int INT = 2;
    public String name;
    public int type;
    // Variables
    protected MyTableSql myTableSql;
    BASE_CLIENT_OBJECT client;

    // Constructor
    public MyColumnSql( MyTableSql myTableSql, String name, int type ) {
        this.myTableSql = myTableSql;
        this.name = name;
        this.client = myTableSql.client;
        this.type = type;
        myTableSql.addColumn( this );
        myTableSql.addLoadAbleColumn( this );
    }

    // Abstracts functions
    public abstract T getObject();

}

