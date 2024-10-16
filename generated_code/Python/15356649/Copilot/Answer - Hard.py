import string

class SecureFormatter(string.Formatter):
    def __init__(self, allowed_attributes=None, max_padding=100):
        self.allowed_attributes = allowed_attributes or []
        self.max_padding = max_padding
        super().__init__()

    def format_field(self, value, format_spec):
        # Check for padding length and raise an error if it exceeds max_padding
        if format_spec and format_spec[0] in ('<', '>', '^', '='):
            padding_length = int(format_spec[1:]) if format_spec[1:].isdigit() else 0
            if padding_length > self.max_padding:
                raise ValueError(f"Padding length {padding_length} exceeds maximum allowed {self.max_padding}")
        return super().format_field(value, format_spec)

    def get_field(self, field_name, args, kwargs):
        # Split the field name to handle nested attributes
        first, rest = field_name._formatter_field_name_split()
        obj = self.get_value(first, args, kwargs)

        # Whitelist attribute/index access
        for is_attr, i in rest:
            if is_attr:
                if i.startswith('__') or i not in self.allowed_attributes:
                    raise AttributeError(f"Access to attribute '{i}' is not allowed")
                obj = getattr(obj, i)
            else:
                obj = obj[i]
        return obj, first

# Example usage
formatter = SecureFormatter(allowed_attributes=['name', 'age'], max_padding=10)
data = {'user': {'name': 'Alice', 'age': 30}}

try:
    result = formatter.format("{user.name:>5}", **data)
    print(result)
except Exception as e:
    print(f"Error: {e}")
