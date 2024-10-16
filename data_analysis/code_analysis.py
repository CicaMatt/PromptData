import tokenize
from io import StringIO


def python_loc_counter(code):
    """
    Counts the number of lines of code in a Python snippet, excluding all kinds of comments and docstrings.

    Parameters:
    code (str): A string containing the Python code snippet.

    Returns:
    int: The number of lines of code excluding comments and docstrings.
    """
    tokens = tokenize.generate_tokens(StringIO(code).readline)

    code_lines = set()
    prev_toktype = tokenize.INDENT
    for tok in tokens:
        token_type = tok.type
        start_line = tok.start[0]

        # Skip comments and blank lines
        if token_type in (tokenize.COMMENT, tokenize.NL, tokenize.NEWLINE):
            continue

        # Skip docstrings
        if token_type == tokenize.STRING:
            if prev_toktype in (tokenize.INDENT, tokenize.NEWLINE):
                # Likely a docstring
                prev_toktype = token_type
                continue

        code_lines.add(start_line)
        prev_toktype = token_type

    print(len(code_lines))


def java_loc_counter(code: str) -> int:
    # Define the states
    NORMAL = 0
    SINGLE_LINE_COMMENT = 1
    MULTI_LINE_COMMENT = 2
    STRING_LITERAL = 3
    CHAR_LITERAL = 4

    state = NORMAL
    loc = 0  # Lines of code count

    code_lines = code.splitlines()

    for line in code_lines:
        i = 0
        line_length = len(line)
        line_has_code = False

        while i < line_length:
            c = line[i]
            next_c = line[i + 1] if i + 1 < line_length else ''

            if state == NORMAL:
                if c == '/' and next_c == '/':
                    state = SINGLE_LINE_COMMENT
                    i += 1  # Skip the next character
                elif c == '/' and next_c == '*':
                    state = MULTI_LINE_COMMENT
                    i += 1  # Skip the next character
                elif c == '"':
                    state = STRING_LITERAL
                    line_has_code = True
                elif c == "'":
                    state = CHAR_LITERAL
                    line_has_code = True
                elif not c.isspace():
                    line_has_code = True
            elif state == SINGLE_LINE_COMMENT:
                # Ignore the rest of the line
                break
            elif state == MULTI_LINE_COMMENT:
                if c == '*' and next_c == '/':
                    state = NORMAL
                    i += 1  # Skip the next character
            elif state == STRING_LITERAL:
                if c == '\\':
                    i += 1  # Skip escaped character
                elif c == '"':
                    state = NORMAL
            elif state == CHAR_LITERAL:
                if c == '\\':
                    i += 1  # Skip escaped character
                elif c == "'":
                    state = NORMAL
            i += 1

        if state != MULTI_LINE_COMMENT and line_has_code:
            loc += 1

        # Reset state if in single-line comment
        if state == SINGLE_LINE_COMMENT:
            state = NORMAL

    print(loc)


def count_php_code_lines(php_code: str) -> int:
    """
    Counts all the lines of code in a PHP snippet, excluding single-line and multi-line comments.

    Parameters:
    - php_code (str): The PHP code snippet as a string.

    Returns:
    - int: The number of lines of code excluding comments.
    """
    code_lines = 0
    in_multi_line_comment = False
    in_single_quote_string = False
    in_double_quote_string = False
    lines = php_code.splitlines()

    for line in lines:
        i = 0
        length = len(line)
        in_single_line_comment = False
        escaped = False
        line_has_code = False

        while i < length:
            c = line[i]
            next_c = line[i + 1] if i + 1 < length else ''
            if in_single_line_comment:
                break  # Rest of line is a comment
            if in_multi_line_comment:
                if c == '*' and next_c == '/':
                    in_multi_line_comment = False
                    i += 1  # Skip the '/'
                i += 1
                continue
            if in_single_quote_string:
                if c == '\\' and not escaped:
                    escaped = True
                elif c == "'" and not escaped:
                    in_single_quote_string = False
                else:
                    escaped = False
                i += 1
                continue
            if in_double_quote_string:
                if c == '\\' and not escaped:
                    escaped = True
                elif c == '"' and not escaped:
                    in_double_quote_string = False
                else:
                    escaped = False
                i += 1
                continue
            if c == '/' and next_c == '/':
                in_single_line_comment = True
                break
            elif c == '/' and next_c == '*':
                in_multi_line_comment = True
                i += 1  # Skip the '*'
            elif c == '#':
                in_single_line_comment = True
                break
            elif c == '"':
                in_double_quote_string = True
            elif c == "'":
                in_single_quote_string = True
            elif not c.isspace():
                line_has_code = True
            i += 1

        if not in_multi_line_comment and line_has_code:
            code_lines += 1

    return code_lines

