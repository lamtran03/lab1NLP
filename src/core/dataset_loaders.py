from typing import List

def load_raw_text_data(path: str) -> str:

    sentences: List[str] = []
    with open(path, "r", encoding="utf-8") as f:
        current_sentence = []
        for line in f:
            line = line.strip()
            if not line:
                if current_sentence:
                    sentences.append(" ".join(current_sentence))
                    current_sentence = []
            elif not line.startswith("#"):  # skip comments
                parts = line.split("\t")
                if len(parts) > 1:
                    word = parts[1]
                    current_sentence.append(word)
        if current_sentence:  # last sentence if file doesn’t end with blank line
            sentences.append(" ".join(current_sentence))
    return "\n".join(sentences)
