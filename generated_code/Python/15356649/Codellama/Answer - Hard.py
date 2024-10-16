import re
import string
from typing import Any, Dict

class SafeFormatter(string.Formatter):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)

    def get_field(self, field_name, args, kwargs):
        if not re.match(r'[a-zA-Z0-9_\.]+', field_name):
            raise ValueError(f"Invalid field name: {field_name}")
        return super().get_field(field_name, args, kwargs)

    def format(self, *args, **kwargs):
        self.check_formatting()
        return super().format(*args, **kwargs)

def substitute_variables(template: str, variables: Dict[str, Any]) -> str:
    formatter = SafeFormatter()
    for key, value in variables.items():
        variables[key] = value
    return formatter.format(template, **variables)
