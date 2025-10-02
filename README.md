

## Cấu trúc

- LAB1/
  - data/
    - UD_English-EWT/
  - src/
    - core/
      - dataset_loaders.py
      - interfaces.py
    - preprocessing/
      - simple_tokenizer.py
      - regex_tokenizer.py
    - representation/
      - count_vectorizer.py
  - test/
    - lab2_test.py
  - main.py

                        
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
**UD_English-EWT**:
```
Sample Sentences from Dataset
1. Al - Zaman : American forces killed Shaikh Abdullah al - Ani , the preacher at the mosque in the town of Qaim , near the Syrian border .
2. [ This killing of a respected cleric will be causing us trouble for years to come . ]
3. DPA : Iraqi authorities announced that they had busted up 3 terrorist cells operating in Baghdad .
4. Two of them were being run by 2 officials of the Ministry of the Interior !
5. The MoI in Iraq is equivalent to the US FBI , so this would be like having J. Edgar Hoover unwittingly employ at a high level members of the Weathermen bombers back in the 1960s .

Vocab Dataset:
{'!': 0, ',': 1, '.': 2, '1960s': 3, '2': 4, '3': 5, 'a': 6, 'abdullah': 7, 'al': 8, 'american': 9, 'ani': 10, 'announced': 11, 'at': 12, 'authorities': 13, 'back': 14, 'baghdad': 15, 'be': 16, 'being': 17, 'bombers': 18, 'border': 19, 'busted': 20, 'by': 21, 'causing': 22, 'cells': 23, 'cleric': 24, 'come': 25, 'dpa': 26, 'edgar': 27, 'employ': 28, 'equivalent': 29, 'fbi': 30, 'for': 31, 'forces': 32, 'had': 33, 'having': 34, 'high': 35, 'hoover': 36, 'in': 37, 'interior': 38, 'iraq': 39, 'iraqi': 40, 'is': 41, 'j': 42, 'killed': 43, 'killing': 44, 'level': 45, 'like': 46, 'members': 47, 'ministry': 48, 'moi': 49, 'mosque': 50, 'near': 51, 'of': 52, 'officials': 53, 'operating': 54, 'preacher': 55, 'qaim': 56, 'respected': 57, 'run': 58, 'shaikh': 59, 'so': 60, 'syrian': 61, 'terrorist': 62, 'that': 63, 'the': 64, 'them': 65, 'they': 66, 'this': 67, 'to': 68, 'town': 69, 'trouble': 70, 'two': 71, 'unwittingly': 72, 'up': 73, 'us': 74, 'weathermen': 75, 'were': 76, 'will': 77, 'would': 78, 'years': 79, 'zaman': 80}

Dataset test:
Al - Zaman : American forces killed Shaikh Abdullah al - Ani , the preacher at the mosque in the town of Qaim , near the Syrian border . -> [0, 2, 1, 0, 0, 0, 0, 1, 2, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0, 4, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1]
[ This killing of a respected cleric will be causing us trouble for years to come . ] -> [0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0]
DPA : Iraqi authorities announced that they had busted up 3 terrorist cells operating in Baghdad . -> [0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0]
Two of them were being run by 2 officials of the Ministry of the Interior ! -> [1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 3, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0]
The MoI in Iraq is equivalent to the US FBI , so this would be like having J. Edgar Hoover unwittingly employ at a high level members of the Weathermen bombers back in the 1960s . -> [0, 1, 2, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 2, 0, 1, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 4, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 1, 0, 0, 1, 0, 0]

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

