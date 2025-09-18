import sys
import os

# Change project root to LAB1 to be able to locate src
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), "..")))

from src.preprocessing.regex_tokenizer import RegexTokenizer
from src.representation.count_vectorizer import CountVectorizer

def main():
    tokenizer = RegexTokenizer()
    vectorizer = CountVectorizer(tokenizer)
    corpus = [
        "I love NLP.",
        "I love love programming.",
        "NLP is a subfield of AI."
    ]

    vectors = vectorizer.fit_transform(corpus)

    #Result
    print("Vocabulary:", vectorizer.vocabulary_)
    print("\nDocument-Term Matrix:")
    for doc, vec in zip(corpus, vectors):
        print(f"{doc} -> {vec}")

if __name__ == "__main__":
    main()
