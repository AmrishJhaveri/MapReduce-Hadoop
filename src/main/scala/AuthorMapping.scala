import java.lang.Iterable

import com.typesafe.scalalogging.Logger
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.apache.hadoop.mapreduce.{Job, Mapper, Reducer}

import scala.collection.JavaConverters._

object AuthorMapping extends App {

  /**
    * Mapper implementation for Hadoop.
    * We get the parsed xml of UIC professors in the required format and create the entry with frequency of 1.
    * The value input to the map will be comma separated(if multiple) UIC authors.
    * We generate all possible combination after sorting these values and write to context.
    * If its a single author then we insert the same author so that it will represent an self-loop edge.
    *
    */
  class AuthorMapper extends Mapper[LongWritable, Text, Text, LongWritable] {

    val logger = Logger("AuthorMapper")
    val one = new LongWritable(1)

    override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, LongWritable]#Context): Unit = {
      logger.info("map value:{}", value.toString)
      val word = new Text()

      val arrAuth: Array[String] = value.toString.split(",")
      scala.util.Sorting.quickSort(arrAuth)

      for (i <- arrAuth.indices) {
        if (arrAuth.length == 1) {
          word.set(arrAuth(i).toString + "," + arrAuth(i).toString + ",")
          context.write(word, one)
        }
        else {
          for (j <- arrAuth.indices) {
            if (i < j) {
              word.set(arrAuth(i).toString + "," + arrAuth(j).toString + ",")
              context.write(word, one)
            }
          }
        }
      }
    }
  }

  /**
    * Add up the values which have the same key i.e. comma separated authors
    */
  class AuthorReducer extends Reducer[Text, LongWritable, Text, LongWritable] {
    val logger = Logger("AuthorReducer")

    override def reduce(key: Text, values: Iterable[LongWritable], context: Reducer[Text, LongWritable, Text, LongWritable]#Context): Unit = {
      logger.info("reduce key:{}", key)
      //sum up all the values with the same key.
      val sum = values.asScala.foldLeft(0L)(_ + _.get)
      logger.info("reduce sum values:{}", sum)
      context.write(key, new LongWritable(sum))
    }
  }

  override def main(args: Array[String]): Unit = {
    val configuration = new Configuration
    val job = Job.getInstance(configuration, "author count")
    //Set the current class as the class to be called by Hadoop.
    job.setJarByClass(this.getClass)
    //Set the class which provides the mapper implementation
    job.setMapperClass(classOf[AuthorMapper])
    //Set the class which provides the Combine/Reducer implementation
    job.setCombinerClass(classOf[AuthorReducer])
    job.setReducerClass(classOf[AuthorReducer])
    //Output class format of the key is comma separated authors
    job.setOutputKeyClass(classOf[Text])
    // Output class format of the count
    job.setOutputValueClass(classOf[LongWritable])
    //input path will be provided as a commandline argument
    FileInputFormat.addInputPath(job, new Path(args(0)))
    //output path will be provided as a command line argument.
    FileOutputFormat.setOutputPath(job, new Path(args(1)))
    //Wait for the job to complete.
    System.exit(if (job.waitForCompletion(true)) 0 else 1)
  }
}