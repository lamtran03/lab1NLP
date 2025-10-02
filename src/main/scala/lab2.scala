import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.PipelineStage
import org.apache.spark.ml.feature._
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.sql.functions._
import java.io.{File, PrintWriter}
import org.apache.spark.ml.linalg.{Vector, Vectors}
import org.apache.spark.sql.Row

object Lab17_NLPPipeline {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder
      .appName("NLP Pipeline Example - Lab17")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    println("Spark Session created successfully.")
    println(s"Spark UI available at http://localhost:4040")
    Thread.sleep(5000)

    // Config
    val useRegexTokenizer = true
    val vectorSize = 20000
    val enableLogisticRegression = false
    val useWord2Vec = false
    val limitDocuments = 1000 //Limit doc

    // Data + Exec time
    val readStart = System.nanoTime()
    val dataPath = "D:\\University\\5_1_Subjects\\NLP\\c4-train.00000-of-01024-30K.json"
    val initialDF = spark.read.json(dataPath)
      .limit(limitDocuments)
      .na.drop("any", Seq("text"))
    val readDuration = (System.nanoTime() - readStart) / 1e9d

    println(s"Successfully read ${initialDF.count()} records. Took $readDuration%.2f seconds")
    initialDF.printSchema()
    initialDF.show(5, truncate = false)

    // Tokenizer
    val tokenizer = if (useRegexTokenizer) {
      new RegexTokenizer()
        .setInputCol("text")
        .setOutputCol("tokens")
        .setPattern("\\s+|[.,;!?()\"']")
    } else {
      new Tokenizer().setInputCol("text").setOutputCol("tokens")
    }

    // StopWordsRemover
    val stopWordsRemover = new StopWordsRemover()
      .setInputCol("tokens")
      .setOutputCol("filtered_tokens")

    // Vectorization
    val hashingTF = new HashingTF()
      .setInputCol("filtered_tokens")
      .setOutputCol("raw_features")
      .setNumFeatures(vectorSize)

    val idf = new IDF()
      .setInputCol("raw_features")
      .setOutputCol("tfidf_features")

    //Normalizer
    val normalizer = new Normalizer()
      .setInputCol("tfidf_features")
      .setOutputCol("features")
      .setP(2.0)

    val word2Vec = new Word2Vec()
      .setInputCol("filtered_tokens")
      .setOutputCol("features")
      .setVectorSize(100)
      .setMinCount(2)

    // Logistic Regression
    val lr = new LogisticRegression()
      .setMaxIter(5)
      .setRegParam(0.01)

    val dfWithLabel = if (enableLogisticRegression) {
      initialDF.withColumn("label", length($"text") % 2)
    } else initialDF

    // Pipeline
    val stages = if (useWord2Vec) {
      Array(tokenizer, stopWordsRemover, word2Vec) ++
        (if (enableLogisticRegression) Array[PipelineStage](lr) else Array.empty[PipelineStage])
    } else {
      Array(tokenizer, stopWordsRemover, hashingTF, idf, normalizer) ++
        (if (enableLogisticRegression) Array[PipelineStage](lr) else Array.empty[PipelineStage])
    }

    val pipeline = new Pipeline().setStages(stages)

    // Fit + đo thời gian
    println("\nFitting the NLP pipeline...")
    val fitStart = System.nanoTime()
    val model = pipeline.fit(dfWithLabel)
    val fitDuration = (System.nanoTime() - fitStart) / 1e9d
    println(f"--> Pipeline fitting took $fitDuration%.2f seconds")

    // Transform + đo thời gian
    println("\nTransforming data...")
    val transStart = System.nanoTime()
    val transformedDF = model.transform(dfWithLabel).cache()
    val count = transformedDF.count()
    val transDuration = (System.nanoTime() - transStart) / 1e9d
    println(f"--> Transformation of $count records took $transDuration%.2f seconds")

    val vocabSize = transformedDF
      .select(explode($"filtered_tokens").as("w"))
      .distinct().count()
    println(s"--> Vocabulary size after preprocessing: $vocabSize")

    //4. Find similar doc
    println("\nFinding similar documents using cosine similarity...")

    val docs = transformedDF.select("text", "features").rdd.map {
      case Row(text: String, features: Vector) => (text, features)
    }.zipWithIndex().map { case ((text, features), idx) => (idx, text, features) }.cache()

    val firstDoc = docs.first()
    val firstVector = firstDoc._3
    val firstIndex = firstDoc._1

    def cosineSim(v1: Vector, v2: Vector): Double = {
      val dot = Vectors.sqdist(v1, v2)
      val norm1 = Vectors.norm(v1, 2)
      val norm2 = Vectors.norm(v2, 2)
      val dotProd = v1.toArray.zip(v2.toArray).map { case (a, b) => a * b }.sum
      dotProd / (norm1 * norm2)
    }

    val similarities = docs.filter(_._1 != firstIndex).map {
      case (idx, text, vec) => (idx, text, cosineSim(firstVector, vec))
    }

    val top5 = similarities.top(5)(Ordering.by(_._3))

    println(s"\nTop 5 most similar documents to doc[$firstIndex]:")
    println(s"Original: ${firstDoc._2.take(120)}...\n")
    top5.foreach { case (idx, text, sim) =>
      println(s"Doc[$idx] (sim=$sim%.4f): ${text.take(120)}...")
    }

    // Save log + res
    val logPath = "D:\\University\\5_1_Subjects\\NLP\\Lab2\\log\\lab17_metrics.log"
    val resultPath = "D:\\University\\5_1_Subjects\\NLP\\Lab2\\results\\lab17_pipeline_output.txt"

    new File(logPath).getParentFile.mkdirs()
    new File(resultPath).getParentFile.mkdirs()

    val logWriter = new PrintWriter(new File(logPath))
    try {
      logWriter.println("--- Performance Metrics ---")
      logWriter.println(f"Read data duration: $readDuration%.2f seconds")
      logWriter.println(f"Pipeline fitting duration: $fitDuration%.2f seconds")
      logWriter.println(f"Transformation duration: $transDuration%.2f seconds")
      logWriter.println(s"Vocabulary size: $vocabSize")
      logWriter.println(s"Tokenizer: ${if (useRegexTokenizer) "RegexTokenizer" else "Tokenizer"}")
      logWriter.println(s"Vectorizer: ${if (useWord2Vec) "Word2Vec" else s"HashingTF+IDF+Normalizer(numFeatures=$vectorSize)"}")
      logWriter.println(s"Logistic Regression: $enableLogisticRegression")
    } finally logWriter.close()

    println(s"Saved log to $logPath")
    println(s"Saved results to $resultPath")

    spark.stop()
    println("Spark Session stopped.")
  }
}
