package LJ.Parser.AST;

import LJ.Lexer.Token;

public class NodeInitInsideClass implements Node {
    Token dataType;
    TypeInit typeInit;
    ForkInit forkInit;
}
