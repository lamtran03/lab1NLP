

## Cấu trúc
LAB1/
│
├── data/
│   └── UD_English-EWT/               
│
├── src/
│   ├── core/
│   │   ├── dataset_loaders.py        
│   │   └── interfaces.py             
│   │
│   ├── preprocessing/
│   │   ├── simple_tokenizer.py       
│   │   └── regex_tokenizer.py        
│   │
│   └── representation/
│       └── count_vectorizer.py       
│
├── test/
│   └── lab2_test.py                  
│
└── main.py 
                        
```

---

## Cài đặt môi trường
```bash
pip install -r requirements.txt.
```

---

**Kết quả mẫu**:
```

Input: Hello, world! This is a test.
SimpleTokenizer: ['hello', ',', 'world', '!', 'this', 'is', 'a', 'test', '.']
RegexTokenizer : ['hello', ',', 'world', '!', 'this', 'is', 'a', 'test', '.']

Input: NLP is fascinating... isn't it?
SimpleTokenizer: ['nlp', 'is', 'fascinating', '...', 'is', "n't", 'it', '?']
RegexTokenizer : ['nlp', 'is', 'fascinating', '.', '.', '.', 'isn', 't', 'it', '?']

Input: Let's see how it handles 123 numbers and punctuation!
SimpleTokenizer: ['let', "'s", 'see', 'how', 'it', 'handles', '123', 'numbers', 'and', 'punctuation', '!']
RegexTokenizer : ['let', 's', 'see', 'how', 'it', 'handles', '123', 'numbers', 'and', 'punctuation', '!']

```

**UD_English-EWT**:
```
--- Tokenizing Sample Text from UD_English-EWT ---
Original Sample: Al - Zaman : American forces killed Shaikh Abdullah al - Ani , the preacher at the mosque in the tow...
SimpleTokenizer Output (first 20 tokens): ['al', '-', 'zaman', ':', 'american', 'forces', 'killed', 'shaikh', 'abdullah', 'al', '-', 'ani', ',', 'the', 'preacher', 'at', 'the', 'mosque', 'in', 'the']
RegexTokenizer Output (first 20 tokens): ['al', 'zaman', 'american', 'forces', 'killed', 'shaikh', 'abdullah', 'al', 'ani', ',', 'the', 'preacher', 'at', 'the', 'mosque', 'in', 'the', 'town', 'of', 'qaim']
Vocabulary: {'but': 0, 'fun': 1, 'hard': 2, 'is': 3, 'nlp': 4}
Vectors:
NLP is fun -> [0, 1, 0, 1, 1]
NLP is hard but fun -> [1, 1, 1, 1, 1]
hard hard hard is NLP -> [0, 0, 3, 1, 1]
```

---

## Chạy CountVectorizer (Lab 2)


**Kết quả mẫu**:
```
Vocabulary: {'.': 0, 'a': 1, 'ai': 2, 'i': 3, 'is': 4, 'love': 5, 'nlp': 6, 'of': 7, 'programming': 8, 'subfield': 9}

Document-Term Matrix:
I love NLP. -> [1, 0, 0, 1, 0, 1, 1, 0, 0, 0]
I love love programming. -> [1, 0, 0, 1, 0, 2, 0, 0, 1, 0]
NLP is a subfield of AI. -> [1, 1, 1, 0, 1, 0, 1, 1, 0, 1]
```

---

## Giải thích kết quả
- **SimpleTokenizer vs RegexTokenizer**  
  - `SimpleTokenizer` dùng NLTK Treebank: một tokenizer trong thư viện NLTK :
    - Tách các khoảng trắng, dấu câu chuẩn (.,?!:;), dấu nháy đơn và kép. nhưng từ có gạch nối thì giữ nguyên 
  - `RegexTokenizer` \w+ tìm bất kỳ word character nào liên tiếp ; [.,?!] : tìm các dấu  

- **CountVectorizer**  
  - Coi doc là một bag_of_word chỉ quan tâm số lần xuất hiện
  - Dùng tokenizer để tạo vocabulary  
  - Biểu diễn văn bản dưới dạng vector đếm số lần xuất hiện của từ.
    - fit : tạo vocab bằng cách dùng tokenizer để tách từ, tập hợp các token lại (không lấy trùng) và tạo thành vocab
    - transform : tạo các vector tương đương với document (chiều dài = vocab). sau đó tokenize doc và đếm, mỗi token có trong vocab thì tại index đó tăng thêm 1
    - fit_transform : gọi fit xây vocab rồi gọi transform xử lý corpus

---

## Khó khăn & Cách giải quyết
- Không biết đọc UD_English-EWT data 
-> Chatgpt chỉ cách tạo dataset_loaders.py

- không tải được requirements.txt
-> Đổi về dùng python 3.11 

- Chưa rõ count vectorize là gì và chức năng của fit, transform
-> Chatgpt giải thích và code mẫu (sau có tự code lại)

---

## Tham khảo
- https://www.geeksforgeeks.org/nlp/vectorization-techniques-in-nlp/
- ChatGPT
---

## Công cụ & Model đã dùng
- NLTK TreebankWordTokenizer (SimpleTokenizer)  

