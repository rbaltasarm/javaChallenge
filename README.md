# javaChallenge
Java Challenge.   

* 09/05/2020
	- Initial aproach: Create the test and user class using persistence H2 (in memory)
	
* 11/05/2020
	- Persistance MySQL layer has been added.
	- MySql connection has been configured using src/main/resources/application.properties
		+ Database: epg
		+ user/password: epgtest/epgtest
		+ Creation script:
			CREATE DATABASE EPG;
			CREATE USER 'epgtest'@'%' identified by 'epgtest';
			GRANT ALL ON epg.* to 'epgtest'@'%'; --Of course this is for demo purposes.  After all tables creation this should be adjusted
			
    - Friends/FirendsOf has been modified due to ManyToMany relations should be managed as sets, not as lists. 	
	
* 12/05/2020
	- Controller creation:
		* javaChallenge/user/add -> for user creation
		* javaChallenge/user/{id}/connect -> for connection creation between 2 users
		* javaChallenge/user({id}/connected -> retrieve the set of user connections ( created by the user itself and by other users)
	- Override toString method for class User: to display friendly