package com.tripPlanner.domain
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.driver.MySQLDriver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = SchemaVersion.schema ++ users.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table SchemaVersion
   *  @param versionRank Database column version_rank SqlType(INT)
   *  @param installedRank Database column installed_rank SqlType(INT)
   *  @param version Database column version SqlType(VARCHAR), PrimaryKey, Length(50,true)
   *  @param description Database column description SqlType(VARCHAR), Length(200,true)
   *  @param `type` Database column type SqlType(VARCHAR), Length(20,true)
   *  @param script Database column script SqlType(VARCHAR), Length(1000,true)
   *  @param checksum Database column checksum SqlType(INT), Default(None)
   *  @param installedBy Database column installed_by SqlType(VARCHAR), Length(100,true)
   *  @param installedOn Database column installed_on SqlType(TIMESTAMP)
   *  @param executionTime Database column execution_time SqlType(INT)
   *  @param success Database column success SqlType(BIT) */
  case class SchemaVersionRow(versionRank: Int, installedRank: Int, version: String, description: String, `type`: String, script: String, checksum: Option[Int] = None, installedBy: String, installedOn: java.sql.Timestamp, executionTime: Int, success: Boolean)
  /** GetResult implicit for fetching SchemaVersionRow objects using plain SQL queries */
  implicit def GetResultSchemaVersionRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[Int]], e3: GR[java.sql.Timestamp], e4: GR[Boolean]): GR[SchemaVersionRow] = GR{
    prs => import prs._
    SchemaVersionRow.tupled((<<[Int], <<[Int], <<[String], <<[String], <<[String], <<[String], <<?[Int], <<[String], <<[java.sql.Timestamp], <<[Int], <<[Boolean]))
  }
  /** Table description of table schema_version. Objects of this class serve as prototypes for rows in queries.
   *  NOTE: The following names collided with Scala keywords and were escaped: type */
  class SchemaVersion(_tableTag: Tag) extends Table[SchemaVersionRow](_tableTag, "schema_version") {
    def * = (versionRank, installedRank, version, description, `type`, script, checksum, installedBy, installedOn, executionTime, success) <> (SchemaVersionRow.tupled, SchemaVersionRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(versionRank), Rep.Some(installedRank), Rep.Some(version), Rep.Some(description), Rep.Some(`type`), Rep.Some(script), checksum, Rep.Some(installedBy), Rep.Some(installedOn), Rep.Some(executionTime), Rep.Some(success)).shaped.<>({r=>import r._; _1.map(_=> SchemaVersionRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7, _8.get, _9.get, _10.get, _11.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column version_rank SqlType(INT) */
    val versionRank: Rep[Int] = column[Int]("version_rank")
    /** Database column installed_rank SqlType(INT) */
    val installedRank: Rep[Int] = column[Int]("installed_rank")
    /** Database column version SqlType(VARCHAR), PrimaryKey, Length(50,true) */
    val version: Rep[String] = column[String]("version", O.PrimaryKey, O.Length(50,varying=true))
    /** Database column description SqlType(VARCHAR), Length(200,true) */
    val description: Rep[String] = column[String]("description", O.Length(200,varying=true))
    /** Database column type SqlType(VARCHAR), Length(20,true)
     *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Rep[String] = column[String]("type", O.Length(20,varying=true))
    /** Database column script SqlType(VARCHAR), Length(1000,true) */
    val script: Rep[String] = column[String]("script", O.Length(1000,varying=true))
    /** Database column checksum SqlType(INT), Default(None) */
    val checksum: Rep[Option[Int]] = column[Option[Int]]("checksum", O.Default(None))
    /** Database column installed_by SqlType(VARCHAR), Length(100,true) */
    val installedBy: Rep[String] = column[String]("installed_by", O.Length(100,varying=true))
    /** Database column installed_on SqlType(TIMESTAMP) */
    val installedOn: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("installed_on")
    /** Database column execution_time SqlType(INT) */
    val executionTime: Rep[Int] = column[Int]("execution_time")
    /** Database column success SqlType(BIT) */
    val success: Rep[Boolean] = column[Boolean]("success")

    /** Index over (installedRank) (database name schema_version_ir_idx) */
    val index1 = index("schema_version_ir_idx", installedRank)
    /** Index over (success) (database name schema_version_s_idx) */
    val index2 = index("schema_version_s_idx", success)
    /** Index over (versionRank) (database name schema_version_vr_idx) */
    val index3 = index("schema_version_vr_idx", versionRank)
  }
  /** Collection-like TableQuery object for table SchemaVersion */
  lazy val SchemaVersion = new TableQuery(tag => new SchemaVersion(tag))

  /** Entity class storing rows of table User
   *  @param id Database column ID SqlType(VARCHAR), Length(50,true)
   *  @param firstName Database column FIRST_NAME SqlType(VARCHAR), Length(50,true)
   *  @param lastName Database column LAST_NAME SqlType(VARCHAR), Length(50,true)
   *  @param registrationDate Database column REGISTRATION_DATE SqlType(DATE) */
  case class UserRow(id: String, firstName: String, lastName: String, registrationDate: java.sql.Date)
  /** GetResult implicit for fetching UserRow objects using plain SQL queries */
  implicit def GetResultUserRow(implicit e0: GR[String], e1: GR[java.sql.Date]): GR[UserRow] = GR{
    prs => import prs._
    UserRow.tupled((<<[String], <<[String], <<[String], <<[java.sql.Date]))
  }
  /** Table description of table USER. Objects of this class serve as prototypes for rows in queries. */
  class Users(_tableTag: Tag) extends Table[UserRow](_tableTag, "USER") {
    def * = (id, firstName, lastName, registrationDate) <> (UserRow.tupled, UserRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(firstName), Rep.Some(lastName), Rep.Some(registrationDate)).shaped.<>({r=>import r._; _1.map(_=> UserRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(VARCHAR), Length(50,true) */
    val id: Rep[String] = column[String]("ID", O.Length(50,varying=true))
    /** Database column FIRST_NAME SqlType(VARCHAR), Length(50,true) */
    val firstName: Rep[String] = column[String]("FIRST_NAME", O.Length(50,varying=true))
    /** Database column LAST_NAME SqlType(VARCHAR), Length(50,true) */
    val lastName: Rep[String] = column[String]("LAST_NAME", O.Length(50,varying=true))
    /** Database column REGISTRATION_DATE SqlType(DATE) */
    val registrationDate: Rep[java.sql.Date] = column[java.sql.Date]("REGISTRATION_DATE")
  }
  /** Collection-like TableQuery object for table User */
  lazy val users = new TableQuery(tag => new Users(tag))
}
