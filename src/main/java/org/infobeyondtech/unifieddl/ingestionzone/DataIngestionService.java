package org.infobeyondtech.unifieddl.ingestionzone;


import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;

import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.Arrays;


public class DataIngestionService {

    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public DataIngestionService()
    {

    }

    public void unstructuredMetadataExtraction() throws Exception{
        System.out.println("testing!!");

        try {
            //create a file object and assume sample.txt is in your current directory
            File file = new File("F:\\us-navy.jpg");

            //Parser method parameters
            Parser parser = new AutoDetectParser();
            BodyContentHandler handler = new BodyContentHandler();
            Metadata metadata = new Metadata();
            FileInputStream inputstream = new FileInputStream(file);
            ParseContext context = new ParseContext();

            //parsing the document
            parser.parse(inputstream, handler, metadata, context);

//            //list of meta data elements before adding new elements
//            System.out.println( " metadata elements :"  + Arrays.toString(metadata.names()));

            //adding new meta data name value pair
            metadata.add("Author","Tutorials Point");
            System.out.println(" metadata name value pair is successfully added");
            //printing all the meta data elements after adding new elements
            System.out.println("Here is the list of all the metadata elements after adding new elements");
            System.out.println(Arrays.toString(metadata.names()));

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }

    public void test() throws Exception{
        System.out.println("testing!!");

        try {
            // This will load the MySQL driver, each DB has its own driver
//            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
//            Class.forName("com.mysql.jdbc.Driver");
             connect=DriverManager.getConnection(
                    "jdbc:mysql://192.168.1.136:3306/autogenmalware","vehchain","Inf0Bey0nd!");
//            connect = DriverManager
//                    .getConnection("192.168.1.136:3306"
//                            + "user=vehchain&password=Inf0Bey0nd!");

            DatabaseMetaData databaseMetaData = connect.getMetaData();

            try(ResultSet resultSet = databaseMetaData.getTables("autogenmalware", null, null, new String[]{"TABLE"})){
                while(resultSet.next()) {
                    String tableName = resultSet.getString("TABLE_NAME");
                    String remarks = resultSet.getString("REMARKS");
                    String catelog = resultSet.getString("TABLE_CAT");
                    String schema = resultSet.getString("TABLE_SCHEM");
//                    System.out.println(tableName + "----" + remarks + "---" +catelog+ "----"+schema);
                }
            }

            try(ResultSet columns = databaseMetaData.getColumns(null,null, "experiemnts_raw", null)){
                while(columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String columnSize = columns.getString("COLUMN_SIZE");
                    String datatype = columns.getString("DATA_TYPE");
                    String isNullable = columns.getString("IS_NULLABLE");
                    String isAutoIncrement = columns.getString("IS_AUTOINCREMENT");
                    System.out.println(columnName + "--" + columnSize + "--" +datatype+ "--"+isNullable+"--"+isAutoIncrement);

//                    java.sql.Types types
                }
            }



/*            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query
            resultSet = statement
                    .executeQuery("select * from feedback.comments");
//            writeResultSet(resultSet);

            // PreparedStatements can use variables and are more efficient
            preparedStatement = connect
                    .prepareStatement("insert into  feedback.comments values (default, ?, ?, ?, ? , ?, ?)");
            // "myuser, webpage, datum, summary, COMMENTS from feedback.comments");
            // Parameters start with 1
            preparedStatement.setString(1, "Test");
            preparedStatement.setString(2, "TestEmail");
            preparedStatement.setString(3, "TestWebpage");
            preparedStatement.setDate(4, new java.sql.Date(2009, 12, 11));
            preparedStatement.setString(5, "TestSummary");
            preparedStatement.setString(6, "TestComment");
            preparedStatement.executeUpdate();

            preparedStatement = connect
                    .prepareStatement("SELECT myuser, webpage, datum, summary, COMMENTS from feedback.comments");
            resultSet = preparedStatement.executeQuery();
//            writeResultSet(resultSet);

            // Remove again the insert comment
            preparedStatement = connect
                    .prepareStatement("delete from feedback.comments where myuser= ? ; ");
            preparedStatement.setString(1, "Test");
            preparedStatement.executeUpdate();

            resultSet = statement
                    .executeQuery("select * from feedback.comments");
//            writeMetaData(resultSet);*/

        } catch (Exception e) {
            throw e;
        } finally {
           close();
        }
    }

    // You need to close the resultSet
    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {

        }
    }
}
