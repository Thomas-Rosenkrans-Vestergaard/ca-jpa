### JPA CA
___

_Explain the rationale behind the topic Object Relational Mapping and the Pros and Cons in using ORM_

The rationale behind ORM is to map entities in a relational database (RDMS) to objects in OOP languages like Java.

ORM frameworks help the programmer by doing much of the mapping.

There are many pros to using ORM and ORM frameworks:
* **Portability**. Many ORM frameworks are portable, meaning the same code can be used on multiple databases. In JPA, the query language
JPQL is used, and then compiled into database-specific SQL.
* **Operations**. Many popular operations are already implemented in ORM frameworks. The programmer therefor does not have to reimplement 
``persist`` or ``update`` operations, leading to fewer bugs.
* **Consistency**. Members in a team usually follow the same code practises when using 
a framework, compared to writing their own code. The data layer therefor becomes more consistent.
* **Documentation**. The ORM-framework has hopefully documented their code, and should therefor be easier to use, compared 
to team members who *forget* to document their code.

There are also some cons to using ORM and ORM frameworks.
* **Obscurity**. Since a lot of code is run out of view of the programmer, it becomes harder for the programmer to fix
bugs. I have also found that some errors lack documentation in ORM frameworks. It's also hard to know what operations
the ORM-framework performs behind the scene.
* **Performance**. Because of the above **obscurity** problem, designing high performance applications using
ORM-frameworks could be difficult.
 
___
*Explain the JPA strategy for handling Object Relational Mapping and important classes/annotations involved.*

The JPA strategy for handling Object Relational Mapping is to use XML configuration or Java Annotations. JPA additionally 
provide sensible defaults for most configuration options. I chose to use annotations over XML because the annotations are 
located on the entity they configure, compared to giant XML configuration files.

The most important annotations are:
```java
@Entity // Marks a class as an Entity
@Table  // Configures the table, the Entity represents, in the database. 
@Id     // Marks a field as the primary key.
@Column // Configures the column, the field represents, in the database.
@OneToOne // Configures a one-to-one relation in the database.
@OneToMany // Configures a one-to-many relation in the database.
@ManyToMany // Configures a many-to-many relation in the database.
@GeneratedValue // Assigns a strategy for auto-generation of values.
@JoinColumn // Configures how relations are stored in the database.  
````
___

*Outline some of the fundamental differences in Database handling using plain JDBC versus JPA*

When using plain JDBC, the burden of mapping database tables to objects often fall on the programmer. Writing code using
ORM-frameworks are much more ``Entity``-oriented than plain JDBC.
___