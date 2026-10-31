# TAMGA Lexer and Parser

An educational Java lexical analyzer and recursive-descent parser for a small programming language with Turkish-inspired keywords. This is a four-person Programming Languages course project at Manisa Celal Bayar University.

TAMGA checks source programs for lexical and syntax errors and performs basic declared-identifier checks. It does not execute programs, evaluate expressions or generate machine code.

## Features

- Turkish keywords: `sayı` (integer declaration), `eğer` (if), `değilse` (else), and `iken` (while).
- Identifiers with Turkish letters and underscores, and integer literals.
- Variable declarations and assignments.
- Arithmetic expressions with parentheses and operator precedence.
- Comparisons using `<`, `>` and `==`.
- Conditional statements and while-style loop syntax.
- A global `HashSet<String>` symbol table for duplicate declarations and undeclared identifiers.
- Error messages for invalid characters, invalid identifiers and syntax errors.

## Requirements

A Java Development Kit (JDK), version 17 or newer. The source files were compiled and the supplied examples checked with OpenJDK 17. No external libraries are required.

## Compile and run

Open a terminal in the directory containing this README.

```sh
javac -encoding UTF-8 -d build src/languageproject/LanguageProject.java src/languageproject/Lexer.java src/languageproject/Parser.java
java -Dfile.encoding=UTF-8 -cp build languageproject.LanguageProject examples/test1_assignment.txt
```

Select any other example by changing the final file path. Running without a file argument selects `examples/test1_assignment.txt`.

## Example TAMGA program

```text
sayı sayaç;
sayaç = 0;
iken (sayaç < 3) {
    sayaç = sayaç + 1;
}
```

The analyzer validates the structure of this program; it does not execute the loop.

## Supplied examples

| File | Purpose | Expected result |
| --- | --- | --- |
| `examples/test1_assignment.txt` | Declarations and assignments | Accepted |
| `examples/test2_arithmetic.txt` | Arithmetic and parentheses | Accepted |
| `examples/test3_condition.txt` | If and else syntax | Accepted |
| `examples/test4_loop.txt` | While-style loop syntax | Accepted |
| `examples/test5_invalid.txt` | An identifier beginning with a digit | Rejected with a lexical error |

The valid examples end with a success message. The invalid example exits with an error; this is the expected behavior. These files are example inputs rather than an automated unit-test suite.

## Source and grammar

| Path | Contents |
| --- | --- |
| `src/languageproject/LanguageProject.java` | Command-line entry point and file loading |
| `src/languageproject/Lexer.java` | Character classification, token recognition and lexical errors |
| `src/languageproject/Parser.java` | Recursive-descent parsing and identifier checks |
| `EBNF.txt` | Language grammar |
| `examples/` | Four valid inputs and one intentionally invalid input |

## Team and contributions

The following responsibilities reflect the original group report:

| Team member | Contribution |
| --- | --- |
| Devran Deniz Akgül | Technical report writing, code analysis and README preparation |
| Eren Öztürk | Lexer and parser analysis, grammar extraction and test design |
| Caner Gündaş | Sample program preparation and symbol-table investigation |
| Efe Özdemir | State diagrams, final formatting and submission review |

This repository presents a collaborative course project. The source files retain their original author comments. The repository owner's documented contribution is code analysis and technical documentation.

## Educational foundation

The project adapts the lexical analysis and recursive-descent parsing approach from Robert W. Sebesta, *Concepts of Programming Languages*, 10th edition, Chapter 4. The course assignment required this foundation. The project extends the approach with Turkish-inspired keywords, statement structures, additional tokens and identifier checks.

## Current limitations

- Source analysis only; no interpreter, code generator or runtime.
- A simple global symbol table; no block scopes, variable values or full type system.
- Processing stops at the first error.
- No functions, arrays, logical operators or unary minus.
- Empty statement blocks are not supported.
