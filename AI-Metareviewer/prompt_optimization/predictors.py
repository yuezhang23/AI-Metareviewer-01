from abc import ABC, abstractmethod
from typing import List, Dict, Callable
from liquid import Template

import utils
import tasks
import utils_ds
class GPT4Predictor(ABC):
    def __init__(self, opt):
        self.opt = opt

    @abstractmethod
    def inference(self, ex, prompt):
        pass

class BinaryPredictor(GPT4Predictor):
    categories = ['No', 'Yes']

    def inference(self, ex, prompt, model):
        prompt = Template(prompt).render(text=ex['text'])
        response = utils.chatgpt(
            prompt, max_tokens=4, n=1, timeout=20, 
            temperature=self.opt['temperature'], model=model)[0]
        pred = 1 if response.strip().upper().startswith('YES') else 0
        return pred

    def batch_inference(self, examples, prompt, model):
        # Prepare batch of prompts
        prompts = [Template(prompt).render(text=ex['text']) for ex in examples]
        
        # Process batch using OpenAI's batch processing
        responses = utils.chatgpt_batch(
            prompts, max_tokens=4, n=1, timeout=20,
            temperature=self.opt['temperature'], model=model)
        
        # Process responses
        preds = [1 if response.strip().upper().startswith('YES') else 0 
                for response in responses]
        
        return preds

class DeepSeekPredictor(GPT4Predictor):
    categories = ['No', 'Yes']

    def inference(self, ex, prompt, model):
        sections = utils.parse_sectioned_prompt(prompt)
        task_section = sections['task'].strip()

        response = utils_ds.make_decision(ex['text'], task_section, temperature=self.opt['temperature'] , model=model)
        pred = 1 if response.strip().upper().startswith('YES') else 0
        return pred
