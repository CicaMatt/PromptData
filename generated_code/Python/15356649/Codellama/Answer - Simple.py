from string import Formatter

class SecureFormatter(Formatter):
    def get_field(self, field_name, params):
        # Whitelist attribute/index access
        if field_name.startswith('__'):
            return None
        
        # Check if the field name is in the list of allowed fields
        if field_name not in self.allowed_fields:
            raise ValueError(f'Invalid field name {field_name}')
        
        # Return the value for the given field name
        return params[field_name]