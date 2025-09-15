package project3;
// A TableModel that supplies ResultSet data to a JTable.
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.table.AbstractTableModel;
import java.util.Properties;
import com.mysql.cj.jdbc.MysqlDataSource;


// ResultSet rows and columns are counted from 1 and JTable 
// rows and columns are counted from 0. When processing 
// ResultSet rows or columns for use in a JTable, it is 
// necessary to add 1 to the row or column number to manipulate
// the appropriate ResultSet column (i.e., JTable column 0 is 
// ResultSet column 1 and JTable row 0 is ResultSet row 1).
public class ResultSetTableModel extends AbstractTableModel 
{
 
	private static final long serialVersionUID = 1L;
private Connection connection;
   private Statement statement;
   private ResultSet resultSet;
   private ResultSetMetaData metaData;
   private int numberOfRows;

   // keep track of database connection status
   private boolean connectedToDatabase = false;
   

   public ResultSetTableModel(Connection connection, String query) throws SQLException {
	    this.connection = connection;
	    statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    connectedToDatabase = true;
	    setQuery(query);
	}

   // get class that represents column type
   public Class getColumnClass( int column ) throws IllegalStateException
   {
      // ensure database connection is available
      if ( !connectedToDatabase ) 
         throw new IllegalStateException( "Not Connected to Database" );

      // determine Java class of column
      try 
      {
         String className = metaData.getColumnClassName( column + 1 );
         
         // return Class object that represents className
         return Class.forName( className );
      } // end try
      catch ( Exception exception ) 
      {
         exception.printStackTrace();
      } // end catch
      
      return Object.class; // if problems occur above, assume type Object
   } // end method getColumnClass

   // get number of columns in ResultSet
   public int getColumnCount() throws IllegalStateException
   {   
      // ensure database connection is available
      if ( !connectedToDatabase ) 
         throw new IllegalStateException( "Not Connected to Database" );

      // determine number of columns
      try 
      {
         return metaData.getColumnCount(); 
      } // end try
      catch ( SQLException sqlException ) 
      {
         sqlException.printStackTrace();
      } // end catch
      
      return 0; // if problems occur above, return 0 for number of columns
   } // end method getColumnCount

   // get name of a particular column in ResultSet
   public String getColumnName( int column ) throws IllegalStateException
   {    
      // ensure database connection is available
      if ( !connectedToDatabase ) 
         throw new IllegalStateException( "Not Connected to Database" );

      // determine column name
      try 
      {
         return metaData.getColumnName( column + 1 );  
      } // end try
      catch ( SQLException sqlException ) 
      {
         sqlException.printStackTrace();
      } // end catch
      
      return ""; // if problems, return empty string for column name
   } // end method getColumnName

   // return number of rows in ResultSet
   public int getRowCount() throws IllegalStateException
   {      
      // ensure database connection is available
      if ( !connectedToDatabase ) 
         throw new IllegalStateException( "Not Connected to Database" );
 
      return numberOfRows;
   } // end method getRowCount

   // obtain value in particular row and column
   public Object getValueAt( int row, int column ) 
      throws IllegalStateException
   {
      // ensure database connection is available
      if ( !connectedToDatabase ) 
         throw new IllegalStateException( "Not Connected to Database" );

      // obtain a value at specified ResultSet row and column
      try 
      {
		   resultSet.next();  /* fixes a bug in MySQL/Java with date format */
         resultSet.absolute( row + 1 );
         return resultSet.getObject( column + 1 );
      } // end try
      catch ( SQLException sqlException ) 
      {
         sqlException.printStackTrace();
      } // end catch
      
      return ""; // if problems, return empty string object
   } // end method getValueAt
   
   public void setQuery(String query) throws SQLException {
	   
	    if (!connectedToDatabase)
	        throw new IllegalStateException("Not Connected to Database");

	    if (query.trim().toLowerCase().startsWith("select")) {
	        resultSet = statement.executeQuery(query);
	       
	    } else {
	        
	        if (query.trim().toLowerCase().startsWith("show tables")) {
	            resultSet = statement.executeQuery(query);
	            
	        } else {
	            statement.executeUpdate(query);
	            
	        }
	    }

	    fireTableStructureChanged();
	}



   public void setUpdate(String query) throws SQLException {
	    
	    if (!connectedToDatabase) {
	        throw new IllegalStateException("Not Connected to Database");
	    }

	    statement.executeUpdate(query);
	    fireTableStructureChanged();
	}


                
   public void disconnectFromDatabase()            
   {              
      if ( !connectedToDatabase )                  
         return;
          
      else try                                          
      {                                            
         statement.close();                        
         connection.close();                       
      }                              
      catch ( SQLException sqlException )          
      {                                            
         sqlException.printStackTrace();           
      }                             
      finally 
      {                                            
         connectedToDatabase = false;              
      }                              
   } 
   


   public void executeQuery(Connection connection, String query) throws SQLException {
   
       if (!connectedToDatabase) {
           throw new IllegalStateException("Not Connected to Database");
       }

       Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
       boolean hasResultSet = statement.execute(query);
       if (hasResultSet) {
           resultSet = statement.getResultSet();
           metaData = resultSet.getMetaData();
           resultSet.last();
           numberOfRows = resultSet.getRow();
           resultSet.beforeFirst();
       }

       fireTableStructureChanged();
   }

  
   public int executeUpdateQuery(Connection connection, String query) throws SQLException {
  
       if (!connectedToDatabase) {
           throw new IllegalStateException("Not Connected to Database");
       }

       Statement statement = connection.createStatement();
       return statement.executeUpdate(query);
   }

   
}  






