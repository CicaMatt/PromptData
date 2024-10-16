import string

class SafeFormatter(string.Formatter):
    MAX_PADDING = 100  
    ALLOWED_ATTRIBUTES = {
        'User': ['name', 'age'],  
        # Add other classes and their allowed attributes here
    }

    def format_field(self, value, format_spec):
        """Override format_field to restrict padding length"""
        if format_spec.isdigit() and int(format_spec) > self.MAX_PADDING:
            raise ValueError(f"Padding exceeds the max allowed limit of {self.MAX_PADDING}")
        return super().format_field(value, format_spec)

    def get_field(self, field_name, args, kwargs):
        """Override get_field to restrict access to whitelisted attributes and keys"""
        try:
            # Extract the first part of the field (before any dot)
            obj, rest = self.get_value(field_name, args, kwargs), field_name
            if '.' in field_name:
                obj, _, rest = field_name.partition('.')
                obj = self.get_value(obj, args, kwargs)

            # For attribute access
            if hasattr(obj, rest):
                allowed_attrs = self.ALLOWED_ATTRIBUTES.get(type(obj).__name__, [])
                if rest not in allowed_attrs:
                    raise ValueError(f"Access to attribute {rest} is not allowed.")
            
            return super().get_field(field_name, args, kwargs)

        except Exception as e:
            raise ValueError(f"Unsafe field access: {e}")

    def get_value(self, key, args, kwargs):
        """Override get_value to ensure safe index/key access"""
        # Ensure key access is restricted
        if isinstance(key, str):
            if key.startswith("__") or key in ("__class__", "__dict__"):  
                raise ValueError(f"Access to {key} is not allowed.")
        return super().get_value(key, args, kwargs)

# Example usage
formatter = SafeFormatter()

class User:
    def __init__(self, name, age, password):
        self.name = name
        self.age = age
        self.password = password  

user = User("Alice", 30, "supersecret")

# Safe formatting example
safe_string = formatter.format("Hello {user.name}, you are {user.age} years old.", user=user)
print(safe_string)

# Unsafe formatting (this should raise an error)
try:
    unsafe_string = formatter.format("Password: {user.password}", user=user)
except ValueError as e:
    print(e)  # Should raise an exception because password access is not allowed

# Safe padding example (within the limit)
try:
    print(formatter.format("{:>10}", "text"))
except ValueError as e:
    print(e)

# Unsafe padding example (exceeds the limit)
try:
    print(formatter.format("{:>1000}", "text"))
except ValueError as e:
    print(e)  
