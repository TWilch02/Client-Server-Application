# Client-Server-Application
Two tier Java based client-server application interacting with a MySQL database utilizing JDBC for connectivity. Application lets clients with various permissions execute SQL commands against different databases. Includes a more specialized GUI interface as a monitoring application.

Detailed Description:

I ultimately developed two different Java applications that allow clients to execute SQL commands against a remote database. The first of these applications allows general types of end-users to issue SQL commands against various databases. The second application is closely based on the first one but is restricted to a specialized accountant client.

For the first application, I created a Java GUI-based front end that accepts any MySQL DDL or DML command, passes this through a JDBC connection to the MySQL database server, executes the statement, and returns the results to the clients. While the application can technically handle any DDL or DML command, I didn’t make use of every possible command. For example, it would be unusual to let a client create a database or table. I also accounted for the fact that the only DML command using the `executeQuery()` method in JDBC is the `SELECT` command, while all other DML and DDL commands use `executeUpdate()`.

My application allows users to execute any SQL DDL or DML command for which they have the correct permissions. User information for connections is maintained in properties files, but each user must still supply their username and password (for their MySQL account) via the GUI for verification.

I also implemented support for multiple simultaneous connections. Since MySQL’s default number of connections is set to 151, I was able to start multiple instances of my application and allow different clients to connect at the same time.

In addition to client interactions, I implemented a background business logic feature: a transaction logging operation that maintains a running total of the number of queries and updates executed by each user across all their sessions. This information is stored in a completely separate logging database, which my application connects to using special application-level privileges stored in a separate properties file (not accessible by end users). Each operation causes my application to connect to this logging database and update the operational logging table.

The second application I created is essentially a special-case version of the first. It is restricted to an accountant-level client whose permissions allow only viewing (querying) of the transaction logging database.

Once I created the main application, I executed a sequence of DML and DDL commands and demonstrated the output in the GUI for three different users. For this, I created two client users with limited permissions, an accountant-level user with restricted access to the logging database, and used the root user (who had full permissions on all databases).
