This project makes it easier to add invite capabilities to an existing Java application. Instead of reinventing this wheel for the hundredth time I decided to make a project out of it.

## Introduction ##
The basic idea is one of your existing website members may invite another person to become a member of your site. This library allows you to generate invitation codes that are stored in a database so that later on you can compare invite codes and confirm or revoke the invitation.

### Some Requirements ###
This library involves a database table so that invites can be stored. I use MySQL and so all the SQL is geared for MySQL. The queries are very basic so it would be trivial to port this library to another database.

The database table is named invite and consists of 3 columns; user, code(primary key), and createdOn. The following query is used in the unit tests for creating the table.
```
create table invite(code varchar(36) primary key, user varchar(50) not null, createdOn datetime not null);
```
This library assumes this exact table is available to the connection. If you want to change the structure of the invite table you need to change classes `com.treetops.jinvite.InviteDAO` and `com.treetops.jinvite.Invite`
### Usage Example ###

Here's how to generate a new invitation code assuming the inviting user's username is a `String` named `invitingUser`.
```
Connection conn = DriverManager.getConnection(...);
String code = Inviter.getInviteCode(conn, invitingUser);
```

Later on you can then confirm this invite and remove it from the database table. Assuming the code to be confirmed is a `String` called `incomingInviteCode`
```
Connection conn = DriverManager.getConnection(...);
boolean isConfirmed = Inviter.confirm(conn,incomingInviteCode);
```
The `Inviter` class won't change the database connection in any way.
### Note on the SVN checkout ###
This project checks out as an Eclipse Tomcat project. The reason is I use JInvite for webapps and the Tomcat project makes for an easy webapp testbed. You're not required to use Tomcat or even Eclipse you can edit/compile the sources however you please but if you want to commit well... you get the idea.

It is best to have the Sysdeo Tomcat plugin installed. The plugin is available at http://www.eclipsetotale.com/tomcatPlugin.html