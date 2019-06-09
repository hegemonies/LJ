# Java language Compiler on Java

## Complete features:
```
- Grammar (grammar.txt)
- Lexer (src/main/java/LJ/Lexer)
- Parser with AST (src/main/java/LJ/Parser)
- Semantic analyzer with Identifier Table (src/main/java/LJ/IdentifierTable)
```

## In progress:
```
- Code Generator
```

## To run compiler (the system doesnt metter):
```
$./gradlew run -Dexec.args="test.lj out_test.lj"
```
(or use Intellij IDEA, because it have presetup settings for run)

# How to compile use nasm on linux_x64:
```
- nasm -f elf64 main.asm -o main.o
- ld main.o -o main
```