/*
import AuthorMapping.AuthorMapper
import org.apache.hadoop.io
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mrunit.{MapDriver, MapReduceDriver, ReduceDriver}
import org.scalatest._


class AuthorMappingTest extends FunSuite with BeforeAndAfter {
  val mapper: AuthorMapper = new AuthorMapper()
  val mapDriver: MapDriver[LongWritable, Text, Text, LongWritable] =
    MapDriver.newMapDriver()
//  mapDriver.setMapper(new AuthorMapper())
  //  mapDriver.setMapper(mapper[LongWritable, Text, Text, LongWritable])
  //  val reduceDriver: ReduceDriver[Text, LongWritable, Text, LongWritable]
  //  val mapReduceDriver: MapReduceDriver[LongWritable, Text, Text, LongWritable, Text, LongWritable, Text, LongWritable]


  before {
    //    val mapper:AuthorMapper= new AuthorMapper

  }

  test("") {
    mapDriver.withInput(new LongWritable(1), new Text("adasd,asadads"))
    mapDriver.withOutput(new Text("adasd,asadads"), new LongWritable(1))
    mapDriver.runTest()
  }

}
*/
