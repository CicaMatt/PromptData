import string

class SafeFormatter(string.Formatter):
    def get_field(self, field_name, args, kwargs):
        # Only allow access to explicit, whitelisted attributes or indices
        if "." in field_name or "[" in field_name:
            raise ValueError("Attribute and index access are not allowed.")
        return super().get_field(field_name, args, kwargs)

    def format_field(self, value, format_spec):
        # Restricting the padding length
        if '>' in format_spec or '<' in format_spec or '^' in format_spec:
            # Split the format spec to find the width
            parts = format_spec.split('>')
            if len(parts) > 1 and parts[1].isdigit():
                width = int(parts[1])
                if width > 100:  # Restrict to a reasonable max width
                    raise ValueError(f"Padding length {width} exceeds the allowed limit.")
        return super().format_field(value, format_spec)

    def format(self, format_string, *args, **kwargs):
        try:
            return super().format(format_string, *args, **kwargs)
        except Exception as e:
            # Catch any formatting exception and handle securely
            return f"Error: {e}"

# Example usage
fmt = SafeFormatter()
try:
    output = fmt.format("Hello {name}!", name="World")
    print(output)
except ValueError as e:
    print(f"Caught error: {e}")
