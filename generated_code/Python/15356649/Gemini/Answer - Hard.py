from string import Formatter

class SecureFormatter(Formatter):
    def __init__(self, allowed_attributes=None):
        super().__init__()
        self.allowed_attributes = allowed_attributes or []  # Whitelist of accessible attributes

    def get_field(self, field_name, args, kwargs):
        # Restrict field access to prevent arbitrary object exploration
        obj, first = super().get_field(field_name, args, kwargs)

        if first:  # If accessing an attribute or index
            if first not in self.allowed_attributes:
                raise AttributeError("Access to attribute '{}' is forbidden".format(first))

        return obj, first

    def format_field(self, value, format_spec):
        # Disable padding to mitigate DoS risks
        if any(char in format_spec for char in '<>='):
            raise ValueError("Padding is not allowed")

        return super().format_field(value, format_spec)