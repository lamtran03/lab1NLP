import re
from typing import List
from src.core.interfaces import Tokenizer

class RegexTokenizer (Tokenizer):
    def tokenize(self, text):
        text = text.lower()

        tokens = re.findall(r"\w+|[.,?!]", text)
        return tokens