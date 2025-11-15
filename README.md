# JSON Parser in Java — Built Completely From Scratch

This project is a **fully hand‑crafted JSON parser** written in **pure Java**, without using any external libraries, code generators, or parser tools like Lex/Yacc, ANTLR, Jackson, or Gson.

The goal of this project is to simulate how a real parser works internally — from reading raw characters → to generating tokens → to parsing grammar → to producing structured JSON object models — while learning how compilers interpret input.


---

## 🚀 What This Project Does

This parser takes input like:

```json
{"name": "Devam", "skills": ["Java", "Spring", "ML"], "age": 20}
```

And converts it into a structured internal representation:

* `JsonObject` (like a Map)
* `JsonArray` (like a List)
* `JsonPrimitive` (String, Number, Boolean, Null)

It validates the JSON strictly according to the specification and throws **detailed, pretty-formatted errors** if the input is invalid.

---

## 🧠 Why Build a JSON Parser From Scratch?

By writing a parser manually, you learn:

* How compilers tokenize text
* How recursive-descent parsing works
* How JSON numbers, strings, escapes, and unicode are interpreted
* Why JSON parsing is slow & expensive internally
* Why structured text formats require grammars
* How whitespace, line, column, and error reporting work underneath

This is one of the best low-level CS engineering exercises.

---

# 🏗️ Project Architecture

The project is organized into **four major layers**:

```
src/
 └── jsonparser/
 |    ├── lexer/
 |    ├── parser/
 |    └── util/
 └── TestMain.java
```

Let's break down what each module does.

---

## 1️⃣ Lexer (Tokenizer)

Located in: `src/jsonparser/lexer/`

### **Purpose**

Converts a raw JSON string into a stream of tokens.

Example:

```
{"a": 10}
```

becomes:

```
LEFT_BRACE
STRING("a")
COLON
NUMBER(10)
RIGHT_BRACE
EOF
```

### **Important Classes**

* **Lexer** → Reads characters & produces tokens
* **Token** → Represents a token + line + column
* **TokenType** → Enum of all token kinds
* **LexerException** → Errors when encountering invalid characters or malformed literals
* **CharReader** → A low-level character reader tracking line & column

### **Handles:**

* Strings + escape sequences
* Unicode (e.g., `A`)
* Numbers (int, float, scientific)
* Literals (`true`, `false`, `null`)
* Whitespace

---

## 2️⃣ Parser (Recursive Descent)

Located in: `src/jsonparser/parser/`

### **Purpose**

Takes tokens and builds the JSON object structure.

Implements the grammar:

```
value → object | array | STRING | NUMBER | true | false | null
override → '{' members? '}'
members → pair (',' pair)*
pair → STRING ':' value
array → '[' elements? ']'
elements → value (',' value)*
```

### **Important Classes**

* **Parser** → The main recursive-descent parser
* **JsonValue** → Base class for all JSON data
* **JsonObject**
* **JsonArray**
* **JsonPrimitive**
* **ParserException** → Errors with detailed position info

### **Handles:**

* Nested objects
* Nested arrays
* Mixed values
* Strict JSON structure validation

---

## 3️⃣ Utility (Error Handling + Helpers)

Located in: `src/jsonparser/util/`

### **Purpose:**

Provide utilities to enrich error handling.

### **Important Components:**

* **PrettyErrorFormatter** → Shows lines with caret (`^`) pointing at error location
* **Position** → Tracks line + column for tokens

### Example Pretty Error

```
ERROR: Expected STRING but got NUMBER at 1:4

{123: "value"}
   ^
```

---

## 4️⃣ Test Suite

`TestMain.java` runs:

* Valid JSON tests
* Invalid JSON tests
* Pretty error output
* Tokenizer + parser verification

This guarantees that both Lexer & Parser behave exactly as expected.

---