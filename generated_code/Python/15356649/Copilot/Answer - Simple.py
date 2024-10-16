import string

class SafeFormatter(string.Formatter):
    def get_field(self, field_name, args, kwargs):
        # Whitelist allowed attributes and indexes
        if field_name not in ['allowed_attribute1', 'allowed_attribute2']:
            raise ValueError(f"Access to '{field_name}' is not allowed")
        return super().get_field(field_name, args, kwargs)

    def format_field(self, value, format_spec):
        # Limit padding length
        if '>' in format_spec or '<' in format_spec or '^' in format_spec:
            padding_length = int(format_spec[1:])
            if padding_length > 100:  # Example limit
                raise ValueError("Padding length too large")
        return super().format_field(value, format_spec)

# Usage
formatter = SafeFormatter()
formatted_string = formatter.format("{allowed_attribute1}", allowed_attribute1="Hello")
print(formatted_string)
