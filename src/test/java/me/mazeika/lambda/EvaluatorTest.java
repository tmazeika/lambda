package me.mazeika.lambda;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EvaluatorTest {
  Token eof = new Token(Token.Type.EOF, "", 1);
  Token leftParen = new Token(Token.Type.LEFT_PAREN, "(", 1);
  Token rightParen = new Token(Token.Type.RIGHT_PAREN, ")", 1);
  Expr id1 = new Expr.Identifier(eof);
  Expr id2 = new Expr.Identifier(leftParen);
  Expr id3 = new Expr.Identifier(rightParen);
  Evaluator eval = new Evaluator();

  @Test
  public void testIdentifiers() {
    assertEquals(eof, eval.evaluate(id1));
    assertEquals(leftParen, eval.evaluate(id2));
    assertEquals(rightParen, eval.evaluate(id3));
  }

  @Test
  public void testDefines() {

  }
}