from string import Formatter

class SecureFormatter(Formatter):
    def format_field(self, value, format_spec):
        if format_spec.startswith('>'):  # Disable padding
            raise ValueError("Padding is not allowed")
        return super().format_field(value, format_spec)

    def get_field(self, field_name, args, kwargs):
        # Only allow basic attribute/index access
        if not all(part.isalnum() or part == '_' for part in field_name.split('.')):
            raise ValueError("Invalid field name")
        return super().get_field(field_name, args, kwargs)

def secure_format(format_string, *args, **kwargs):
    try:
        return SecureFormatter().format(format_string, *args, **kwargs)
    except (ValueError, KeyError, IndexError) as e:
        # Log the exception or handle it appropriately
        return f"Error in formatting: {e}"