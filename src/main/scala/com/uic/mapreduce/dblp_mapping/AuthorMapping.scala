package com.uic.mapreduce.dblp_mapping

import com.typesafe.scalalogging.Logger
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.apache.hadoop.mapreduce.{Job, Mapper, Reducer}

/**
  * Created by gkatzioura on 2/14/17.
  */
object AuthorMapping extends App {

  class AuthorMapper extends Mapper[LongWritable, Text, Text, LongWritable] {
    val logger = Logger("AuthorMapper")
    val one = new LongWritable(1)


    override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, LongWritable]#Context): Unit = {
      //      logger.info("xml file-log:{}", value)
      println(s"map value:$value")
      //      val xml = XML.loadString(value.toString)
      logger.info("map value:{}", value.toString)
      val word = new Text()

      val arrAuth: Array[String] = value.toString.split(",")
      scala.util.Sorting.quickSort(arrAuth)

      /*arrAuth.zipWithIndex.foreach { case (author1, i1) => {

        if (arrAuth.length > 1) {
          arrAuth.zipWithIndex.foreach { case (author2, i2) => {
            if (i2 > i1) {
              word.set(author1.toString + "," + author2.toString+",")
            }
          }
          }
        }
		else{
		word.set(author1.toString+","+author1.toString+",")
		}
        context.write(word, one)
      }
      }*/
      for (i <- arrAuth.indices) {
        if (arrAuth.length == 1) {
          word.set(arrAuth(i).toString + "," + arrAuth(i).toString+",")
          context.write(word, one)
        }
        else {
          for (j <- arrAuth.indices) {
            if (i < j) {
              word.set(arrAuth(i).toString + "," + arrAuth(j).toString+",")
              context.write(word, one)
            }
          }
        }
      }



      //      (xml \ "article")
      //        .foreach(article => (article \ "author")
      //          .foreach(author => {
      //            logger.info("Author:{}", author)
      //            word.set(author.text)
      //            context.write(word, one)
      //          }))


    }
  }

  class IntSumReader extends Reducer[Text, LongWritable, Text, LongWritable] {
    val logger = Logger("IntSumReader")


    override def reduce(key: Text, values: Iterable[LongWritable], context: Reducer[Text, LongWritable, Text, LongWritable]#Context): Unit = {
      logger.info("reduce key:{}", key)
      val sum = values.asScala.foldLeft(0L)(_ + _.get)
      logger.info("reduce sum values:{}", sum)



      context.write(key, new LongWritable(sum))
    }
  }


  override def main(args: Array[String]): Unit = {
    val configuration = new Configuration

    //    configuration.set(XmlInputFormat.START_TAG_KEY, "<article>")
    //    configuration.set(XmlInputFormat.END_TAG_KEY, "</article>")

    val job = Job.getInstance(configuration, "author count")
    job.setJarByClass(this.getClass)
    job.setMapperClass(classOf[AuthorMapper])
    job.setCombinerClass(classOf[IntSumReader])
    job.setReducerClass(classOf[IntSumReader])
    job.setOutputKeyClass(classOf[Text])
    job.setOutputKeyClass(classOf[Text])
    job.setOutputValueClass(classOf[LongWritable])
    FileInputFormat.addInputPath(job, new Path(args(0)))
    FileOutputFormat.setOutputPath(job, new Path(args(1)))
    System.exit(if (job.waitForCompletion(true)) 0 else 1)
  }
}
