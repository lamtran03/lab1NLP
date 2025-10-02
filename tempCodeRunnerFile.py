from src.preprocessing.simple_tokenizer import SimpleTokenizer
from src.preprocessing.regex_tokenizer import RegexTokenizer
from src.core.dataset_loaders import load_raw_text_data
from src.representation.count_vectorizer import CountVectorizer

if __name__ == "__main__":
    simple_tokenizer = SimpleTokenizer()
    regex_tokenizer = RegexTokenizer()

    sentences = [
        "Hello, world! This is a test.",
        "NLP is fascinating... isn't it?",
        "Let's see how it handles 123 numbers and punctuation!"
    ]

    for sentence in sentences:
        print(f"\nInput: {sentence}")
        print("SimpleTokenizer:", simple_tokenizer.tokenize(sentence))
        print("RegexTokenizer :", regex_tokenizer.tokenize(sentence))

    dataset_path = "data/UD_English-EWT/en_ewt-ud-train.conllu"
    raw_text = load_raw_text_data(dataset_path)

    sample_text = raw_text[:500]
    print("\n--- Tokenizing Sample Text from UD_English-EWT ---")
    print(f"Original Sample: {sample_text[:100]}...")

    simple_tokens = simple_tokenizer.tokenize(sample_text)
    print(f"SimpleTokenizer Output (first 20 tokens): {simple_tokens[:20]}")

    regex_tokens = regex_tokenizer.tokenize(sample_text)
    print(f"RegexTokenizer Output (first 20 tokens): {regex_tokens[:20]}")


    vectorizer = CountVectorizer(simple_tokenizer)

    corpus = [
        "NLP is fun",
        "NLP is hard but fun",
        "hard hard hard is NLP"
    ]

    vectors = vectorizer.fit_transform(corpus)

    print("Vocabulary:", vectorizer.vocabulary_)
    print("Vectors:")
    for doc, vec in zip(corpus, vectors):
        print(f"{doc} -> {vec}")
