from typing import List
from src.core.interfaces import Tokenizer
from nltk.tokenize import TreebankWordTokenizer

class SimpleTokenizer(Tokenizer):
    def __init__(self):
        self.tokenizer = TreebankWordTokenizer()

    def tokenize(self, text: str) -> List[str]:
        text = text.lower()
        return self.tokenizer.tokenize(text)
