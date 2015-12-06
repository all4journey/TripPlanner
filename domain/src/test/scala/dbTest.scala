import org.scalatest.{Matchers, FlatSpec}
import slick.jdbc.JdbcBackend.Database
import slick.driver.MySQLDriver

/**
  * Created by rjkj on 12/6/15.
  */
class dbTest extends FlatSpec with Matchers{

  "Slick" should "connect" in {
    val db = Database.forConfig("mysql-trip_planner")


  }

}
