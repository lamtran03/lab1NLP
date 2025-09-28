import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.PipelineStage
import org.apache.spark.ml.feature._
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.sql.functions._
import java.io.{File, PrintWriter}

object lab2 {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder
      .appName("NLP Pipeline Example")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._
    println("Spark Session created successfully.")
    println(s"Spark UI available at http://localhost:4040")
    println("Pausing for 10 seconds to allow you to open the Spark UI...")
    Thread.sleep(10000)

    // Config
    val useRegexTokenizer = true          // Ex1: false = dùng Tokenizer cơ bản
    val vectorSize = 20000                // Ex2: đổi thành 1000 để thử
    val enableLogisticRegression = false  // Ex3: true = thêm LogisticRegression
    val useWord2Vec = false               // Ex4: true = thay TF-IDF bằng Word2Vec

    //Đọc dữ liệu
    val dataPath = "D:\\University\\5_1_Subjects\\NLP\\c4-train.00000-of-01024-30K.json"
    val initialDF = spark.read.json(dataPath).limit(1000).na.drop("any", Seq("text"))
    println(s"Successfully read ${initialDF.count()} records.")
    initialDF.printSchema()
    initialDF.show(5, truncate = false)

    //Tokenizer (Ex1)
    val tokenizer = if (useRegexTokenizer) {
      new RegexTokenizer()
        .setInputCol("text")
        .setOutputCol("tokens")
        .setPattern("\\s+|[.,;!?()\"']")
    } else {
      new Tokenizer().setInputCol("text").setOutputCol("tokens")
    }

    //StopWordsRemover
    val stopWordsRemover = new StopWordsRemover()
      .setInputCol("tokens")
      .setOutputCol("filtered_tokens")

    //Vectorization (Ex2+4)
    val hashingTF = new HashingTF()
      .setInputCol("filtered_tokens")
      .setOutputCol("raw_features")
      .setNumFeatures(vectorSize)

    val idf = new IDF()
      .setInputCol("raw_features")
      .setOutputCol("features")

    val word2Vec = new Word2Vec()
      .setInputCol("filtered_tokens")
      .setOutputCol("features")
      .setVectorSize(100)
      .setMinCount(2)

    //Logistic Regression (Ex3)
    val lr = new LogisticRegression()
      .setMaxIter(5)
      .setRegParam(0.01)

    val dfWithLabel = if (enableLogisticRegression) {
      initialDF.withColumn("label", length($"text") % 2)
    } else initialDF

    //pipeline
val stages = if (useWord2Vec) {
  Array(tokenizer, stopWordsRemover, word2Vec) ++
    (if (enableLogisticRegression) Array[PipelineStage](lr) else Array.empty[PipelineStage])
} else {
  Array(tokenizer, stopWordsRemover, hashingTF, idf) ++
    (if (enableLogisticRegression) Array[PipelineStage](lr) else Array.empty[PipelineStage])
}

    val pipeline = new Pipeline().setStages(stages)

    println("\nFitting the NLP pipeline...")
    val fitStart = System.nanoTime()
    val model = pipeline.fit(dfWithLabel)
    val fitDuration = (System.nanoTime() - fitStart) / 1e9d
    println(f"--> Pipeline fitting took $fitDuration%.2f seconds")

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

    //Save log + res
    val logPath = "D:\\University\\5_1_Subjects\\NLP\\log\\lab2_metrics.log"
    val resultPath = "D:\\University\\5_1_Subjects\\NLP\\results\\lab2_pipeline_output.txt"

    new File(logPath).getParentFile.mkdirs()
    new File(resultPath).getParentFile.mkdirs()

    val logWriter = new PrintWriter(new File(logPath))
    try {
      logWriter.println("--- Performance Metrics ---")
      logWriter.println(f"Pipeline fitting duration: $fitDuration%.2f seconds")
      logWriter.println(f"Transformation duration: $transDuration%.2f seconds")
      logWriter.println(s"Vocabulary size: $vocabSize")
      logWriter.println(s"Tokenizer: ${if (useRegexTokenizer) "RegexTokenizer" else "Tokenizer"}")
      logWriter.println(s"Vectorizer: ${if (useWord2Vec) "Word2Vec" else s"HashingTF+IDF(numFeatures=$vectorSize)"}")
      logWriter.println(s"Logistic Regression: $enableLogisticRegression")
    } finally logWriter.close()

    val nResults = 20
    val results = transformedDF.limit(nResults).collect()
    val resWriter = new PrintWriter(new File(resultPath))
    try {
      resWriter.println(s"--- NLP Pipeline Output (First $nResults results) ---")
      resWriter.println(s"Output file: $resultPath\n")
      results.foreach { row =>
        val text = row.getAs[String]("text")
        resWriter.println("=" * 80)
        resWriter.println(s"Original Text: ${text.take(100)}...")
        if (enableLogisticRegression) {
          val pred = row.getAs[Double]("prediction")
          val label = row.getAs[Double]("label")
          resWriter.println(s"Label: $label, Prediction: $pred")
        } else {
          val features = row.getAs[org.apache.spark.ml.linalg.Vector]("features")
          resWriter.println(s"Features: $features")
        }
        resWriter.println("=" * 80)
        resWriter.println()
      }
    } finally resWriter.close()

    println(s"Saved log to $logPath")
    println(s"Saved results to $resultPath")

    spark.stop()
    println("Spark Session stopped.")
  }
}
