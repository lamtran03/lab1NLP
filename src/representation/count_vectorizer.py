from typing import List, Dict
from src.core.interfaces import Vectorizer, Tokenizer
import numpy as np

class CountVectorizer(Vectorizer):
    def __init__(self, tokenizer: Tokenizer):
        self.tokenizer = tokenizer
        self.vocabulary_: Dict[str, int] = {}

    def fit(self, corpus: List[str]):

        unique_tokens = set()
        for doc in corpus:
            tokens = self.tokenizer.tokenize(doc)
            unique_tokens.update(tokens)

        sorted_tokens = sorted(unique_tokens)
        self.vocabulary_ = {token: idx for idx, token in enumerate(sorted_tokens)}

    def transform(self, documents: List[str]) -> List[List[int]]:

        if not self.vocabulary_:
            raise ValueError("Vectorizer not fitted")

        vectors = []
        for doc in documents:
            tokens = self.tokenizer.tokenize(doc)
            vector = [0] * len(self.vocabulary_)
            for token in tokens:
                if token in self.vocabulary_:
                    vector[self.vocabulary_[token]] += 1
            vectors.append(vector)
        return vectors

    def fit_transform(self, corpus: List[str]) -> List[List[int]]:

        self.fit(corpus)
        return self.transform(corpus)
