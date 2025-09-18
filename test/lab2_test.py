import sys
import os
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), "..")))
from src.preprocessing.regex_tokenizer import RegexTokenizer
from src.representation.count_vectorizer import CountVectorizer
from src.core.dataset_loaders import load_raw_text_data


def main():
    tokenizer = RegexTokenizer()

    #Test Normal corpus
    vectorizer1 = CountVectorizer(tokenizer)
    corpus = [
        "I love NLP.",
        "I love love programming.",
        "NLP is a subfield of AI."
    ]

    vectors = vectorizer1.fit_transform(corpus)

    print("Vocab Normal:", vectorizer1.vocabulary_)
    print("\nNormal test:")
    for doc, vec in zip(corpus, vectors):
        print(f"{doc} -> {vec}")

    #Test Dataset
    dataset_path = "data/UD_English-EWT/en_ewt-ud-train.conllu"
    raw_text = load_raw_text_data(dataset_path)

    corpus2 = raw_text.split("\n")[:5]   
    print("\nSample Sentences from Dataset")
    for i, sent in enumerate(corpus2):
        print(f"{i+1}. {sent}")

    vectorizer2 = CountVectorizer(tokenizer)  
    vectors2 = vectorizer2.fit_transform(corpus2)

    print("\nVocab Dataset:")
    print(vectorizer2.vocabulary_)

    print("\nDataset test:")
    for doc2, vec2 in zip(corpus2, vectors2):
        print(f"{doc2} -> {vec2}")


if __name__ == "__main__":
    main()
