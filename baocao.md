# Lab2 - Spark NLP Pipeline
## Cấu trúc thư mục

- Lab2/
  - build.sbt
  - data/
    - c4-train.00000-of-01024-30K.json
  - log/
    - lab2_metrics.log
  - results/
    - lab2_pipeline_output.txt
  - src/
    - main/
      - scala/
        - lab2.scala



---

## Cài đặt môi trường
Yêu cầu:
- **Java**: 17 (LTS)
- **Scala**: 2.12.18
- **sbt**: >= 1.8.0
- **Apache Spark**: 3.5.1 

Cài đặt sbt dependencies:
```bash
sbt update
````

Chạy code:

```bash
sbt run
```

Kết quả:

* Log: `log/lab2_metrics.log`
* Output: `results/lab2_pipeline_output.txt`

---

## Kết quả mẫu

**Log (`lab2_metrics.log`):**

```
--- Performance Metrics ---
Pipeline fitting duration: 2.98 seconds
Transformation duration: 1.25 seconds
Vocabulary size: 31438
Tokenizer: RegexTokenizer
Vectorizer: HashingTF+IDF(numFeatures=20000)
Logistic Regression: false
```

**Output (`lab2_pipeline_output.txt`):**

---

## Giải thích kết quả

* **Tokenizer (Ex1)**

  * `RegexTokenizer`: tách từ theo regex, loại bỏ nhiều ký tự đặc biệt.
  * `Tokenizer`: tách từ theo khoảng trắng, đơn giản hơn.

* **Vectorizer (Ex2)**

  * `HashingTF + IDF` với `numFeatures=20000`: vector thưa, ít va chạm.
  * Khi giảm xuống `numFeatures=1000`, nhiều va chạm hơn → mất thông tin.

* **Logistic Regression (Ex3)**

  * Thêm cột `label`.
  * Pipeline thêm bước huấn luyện mô hình → có `prediction`.

* **Word2Vec (Ex4)**

  * Thay vì TF-IDF, dùng Word2Vec để biểu diễn câu bằng embedding trung bình các từ.
  * Vector ngắn hơn (100 chiều), có tính ngữ nghĩa.

---

## Khó khăn & Cách giải quyết

* **Lỗi "object apache is not a member of package org"**
  -> Do chưa khai báo Spark dependencies trong `build.sbt`. Đã sửa bằng cách thêm:

  ```scala
  "org.apache.spark" %% "spark-core" % "3.5.1",
  "org.apache.spark" %% "spark-sql" % "3.5.1",
  "org.apache.spark" %% "spark-mllib" % "3.5.1"
  ```
* **Không đọc được file `.gz`**
  -> Giải nén file thành `.json` và đổi `dataPath` trong code.
* **Lỗi Array() khi thêm LogisticRegression**
  -> Dùng `Array.empty[PipelineStage]` thay vì `Array()`. (Chatgpt chỉ)


---

## Tham khảo

* [Spark MLlib Feature Extraction](https://spark.apache.org/docs/latest/ml-features.html)
* [Spark MLlib Classification](https://spark.apache.org/docs/latest/ml-classification-regression.html)
* https://azweb.com.vn/pipeline-la-gi/#:~:text=Trong%20ng%E1%BB%AF%20c%E1%BA%A3nh%20l%E1%BA%ADp%20tr%C3%ACnh%20v%C3%A0%20ph%C3%A1t%20tri%E1%BB%83n,cho%20th%C3%A0nh%20ph%E1%BA%A7n%20k%E1%BA%BF%20ti%E1%BA%BFp%20ngay%20sau%20n%C3%B3.
* ChatGPT

---

## Công cụ & Model đã dùng

* **Apache Spark MLlib**

  * `RegexTokenizer`, `Tokenizer`
  * `StopWordsRemover`
  * `HashingTF`, `IDF`, `Word2Vec`
  * `LogisticRegression` (huấn luyện pipeline đơn giản)
* **Dataset**: Trích 1000 bản ghi từ tập `c4-train.00000-of-01024-30K.json`

