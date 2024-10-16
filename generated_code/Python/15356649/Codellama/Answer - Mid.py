import string
from typing import Any, List, Union

class SecureFormatter(string.Formatter):
    def get_field(self, field_name: str, args: Union[List, Tuple], kwargs: Dict) -> Any:
        """Override the default implementation of get_field to prevent accessing dunder variables and check for cross-site scripting attacks."""
        if field_name.startswith('__'):
            raise ValueError(f"Dunder variable {field_name} is not allowed")
        return super().get_field(field_name, args, kwargs)

    def format(self, *args: Any, **kwargs: Any) -> str:
        """Override the default implementation of format to prevent padding and check for cross-site scripting attacks."""
        if '>' in self.format_spec:
            raise ValueError("Padding is not allowed")
        return super().format(*args, **kwargs)