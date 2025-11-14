# JSON Specification (Simplified for Parser Implementation)

This document summarizes the full JSON specification required to implement your JSON jsonparser.parser from scratch in Java.

---

# 📌 1. What is JSON?

JSON (JavaScript Object Notation) is a lightweight data-interchange format based on key-value structured data. It supports 6 value types and has strict rules about formatting.

Your jsonparser.parser must recognize:

* **Object**
* **Array**
* **String**
* **Number**
* **Boolean** (`true`, `false`)
* **Null** (`null`)

---

# 📌 2. JSON Value Types

## **2.1 Object**

Objects are unordered collections of key-value pairs:

```
{ "key": value, "another": value }
```

Rules:

* Surrounded by `{` and `}`
* Keys must be **strings only**
* Pairs separated by commas
* A colon `:` separates key and value

## **2.2 Array**

Arrays are ordered lists of values:

```
[ value1, value2, value3 ]
```

Rules:

* Surrounded by `[` and `]`
* Elements separated by commas
* Elements may be of **any JSON type**

## **2.3 String**

Strings must be in double quotes:

```
"hello world"
```

### Escape characters supported:

```
\"  double-quote
\\   backslash
\/   forward slash
\b   backspace
\f   form feed
\n   newline
\r   carriage return
\t   tab
\uXXXX  unicode hex
```

## **2.4 Number**

Valid number examples:

```
0
-1
12.34
0.001
10e4
-3.14E-2
```

Invalid examples:

```
01       (leading zero not allowed)
--1      (double minus)
1.       (trailing dot)
.5       (must start with digit)
```

Grammar:

```
number = int | int frac | int exp | int frac exp
int    = '-'? digit | '-'? nonzero digit digits
frac   = '.' digits
exp    = ('e' | 'E') ('+' | '-')? digits
digits = digit+
```

## **2.5 Boolean**

```
true
false
```

Must be lowercase.

## **2.6 Null**

```
null
```

Must be lowercase.

---

# 📌 3. Whitespace Rules

Whitespace allowed **between tokens only**, not inside literals.

```
space (0x20)
tab (0x09)
carriage return (0x0D)
newline (0x0A)
```

Examples:

```
{    "a"   :   10  }
[1,   2,3]
```

Whitespace is NOT allowed in:

* numbers
* strings
* boolean/null literals

---

# 📌 4. JSON Grammar (The One Your Parser Will Use)

This is the simplified but correct grammar for your recursive descent jsonparser.parser:

```
value       → object
            | array
            | string
            | number
            | "true"
            | "false"
            | "null"

object      → '{' ws '}'
            | '{' members '}'

members     → pair
            | pair ',' members

pair        → string ws ':' ws value

array       → '[' ws ']'
            | '[' elements ']'

elements    → value
            | value ',' elements

string      → '"' chars '"'

chars       → char chars
            | ε

char        → any character except '"' or '\\'
            | escape

escape      → '\\"' | '\\\\' | '\\/' | '\\b' | '\\f' | '\\n' | '\\r' | '\\t' | '\\u' hex hex hex hex

number      → int | int frac | int exp | int frac exp
```

---

# 📌 5. Examples of Valid JSON

### Valid Object

```
{
  "name": "Devam",
  "age": 20,
  "skills": ["java", "spring", "python"],
  "active": true,
  "score": null
}
```

### Valid Array

```
[1, 2, 3, "hello", false, null]
```

### Valid Nested

```
{ "a": { "b": { "c": 10 } } }
```

---

# 📌 6. Examples of Invalid JSON

❌ Trailing comma

```
{ "a": 10, }
```

❌ Single quotes not allowed

```
{ 'a': 10 }
```

❌ Unescaped control characters

```
"hello
world"
```

❌ Invalid number

```
01
```

❌ Missing comma

```
[1 2 3]
```

❌ Key not a string

```
{ a: 10 }
```

---

# 📌 7. Error Types Your Parser Must Detect

1. Unexpected token
2. Unexpected end of file
3. Missing `,` or `:`
4. Invalid escape sequence
5. Invalid Unicode escape
6. Invalid number format
7. Unterminated string
8. Extra characters after document

---

# 📌 8. Summary

This specification is all you need to implement:

* Tokenizer
* Parser
* Native object jsonparser.model
* Error reporting

Follow the grammar strictly to ensure correctness.

---

End of JSON Specification.
