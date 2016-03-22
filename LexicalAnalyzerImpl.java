/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newlang8;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author Admin
 */
public class LexicalAnalyzerImpl implements LexicalAnalyzer {

    Stack<LexicalUnit> stack = new Stack<LexicalUnit>();

    PushbackReader in;
    Map<String, LexicalUnit> reserved;

    public LexicalAnalyzerImpl(InputStream is) {
        Reader r = new InputStreamReader(is);
        in = new PushbackReader(r);

        reserved = new HashMap<String, LexicalUnit>();
        reserved.put("LOOP", new LexicalUnit(LexicalType.LOOP));
        reserved.put("IF", new LexicalUnit(LexicalType.IF));
        reserved.put("THEN", new LexicalUnit(LexicalType.THEN));
        reserved.put("ELSE", new LexicalUnit(LexicalType.ELSE));
        reserved.put("ELSEIF", new LexicalUnit(LexicalType.ELSEIF));
        reserved.put("ENDIF", new LexicalUnit(LexicalType.ENDIF));
        reserved.put("FOR", new LexicalUnit(LexicalType.FOR));
        reserved.put("FORALL", new LexicalUnit(LexicalType.FORALL));
        reserved.put("ENDIF", new LexicalUnit(LexicalType.ENDIF));
        reserved.put("NEXT", new LexicalUnit(LexicalType.NEXT));
        reserved.put("SUB", new LexicalUnit(LexicalType.FUNC));
        reserved.put("DIM", new LexicalUnit(LexicalType.DIM));
        reserved.put("AS", new LexicalUnit(LexicalType.AS));
        reserved.put("END", new LexicalUnit(LexicalType.END));
        reserved.put("DO", new LexicalUnit(LexicalType.DO));
        reserved.put("UNTIL", new LexicalUnit(LexicalType.UNTIL));
        reserved.put("TO", new LexicalUnit(LexicalType.TO));
        reserved.put("WEND", new LexicalUnit(LexicalType.WEND));
        //keyがstring,valueがLexicalUnit
    }

    @Override
    //字句を一つ返す
    public LexicalUnit get() {
        if (!stack.empty()) {
            return stack.pop();
        }
        while (true) {
            char c = getChar();
            if (c == (char) -1) {
                return new LexicalUnit(LexicalType.EOF);
            }
            if (c == '=') {
                return new LexicalUnit(LexicalType.EQ);
            }
            if (c == '<') {
                return new LexicalUnit(LexicalType.LT);
            }
            if (c == '>') {
                return new LexicalUnit(LexicalType.GT);
            }
            if (c == '.') {
                return new LexicalUnit(LexicalType.DOT);
            }
            if (c == '\n') {
                return new LexicalUnit(LexicalType.NL);
            }
            if (c == '+') {
                return new LexicalUnit(LexicalType.ADD);
            }
            if (c == '-') {
                return new LexicalUnit(LexicalType.SUB);
            }
            if (c == '*') {
                return new LexicalUnit(LexicalType.MUL);
            }
            if (c == '/') {
                return new LexicalUnit(LexicalType.DIV);
            }
            if (c == ')') {
                return new LexicalUnit(LexicalType.LP);
            }
            if (c == '(') {
                return new LexicalUnit(LexicalType.RP);
            }
            if (c == ',') {
                return new LexicalUnit(LexicalType.COMMA);
            }
            if (c == ' ' || c == '\t' || c == '\r') {
                continue;
            }

            ungetChar(c);
            if (c >= '0' && c <= '9') {
                return getNum();
            }
            if (c == '"') {
                return getLit();
            }
            if (c >= 'a' && c <= 'z' || (c >= 'A' && c <= 'z')) {
                return getString();
            }
        }
        //return new LexicalUnit();//getされるたびにひとつ作る
        //return null;
    }

    private LexicalUnit getString() {
        String buffer = "";
        while (true) {
            char c = getChar();
            if (c >= 'a' && c <= 'z' || (c >= 'A' && c <= 'z')) {
                buffer += c;
            } else {

                try {
                    in.unread((int) c);
                } catch (IOException e) {
                } catch (Exception e) {
                }
                break;
            }
        }
        LexicalUnit lu = reserved.get(buffer);
        if (lu != null) {
            return lu;
        }
        return new LexicalUnit(LexicalType.NAME, new ValueImpl(buffer));
    }

    private char getChar() {
        if (in == null) {
            return (char) -1;
        }
        try {
            int ci = in.read();
            if (ci < 0) {
                in.close();
                in = null;
                return (char) -1;
            }
            return (char) ci;
        } catch (Exception e) {
            in = null;
            return (char) -1;
        }
    }
    /*
     エラーがおきたらend of file
     ファイルの終わりの処理はする
     end of fileはchar-1で表す
     */

    private void ungetChar(char c) {
        try {
            in.unread((int) c);
        } catch (Exception e) {
        }
    }

    @Override
    public boolean expect(LexicalType type) {
        return false;
    }

    @Override
    //字句いらん
    public void unget(LexicalUnit token) {
        stack.push(token);
    }

    private LexicalUnit getNum() {
        String buffer = "";
        while (true) {
            char c = getChar();
            if (c >= '0' && c <= '9' || c == '.') {
                buffer += c;
            } else {
                try {
                    in.unread((int) c);
                    break;
                } catch (IOException e) {
                }
            }
        }
        if (buffer.contains(".")) {
            return new LexicalUnit(LexicalType.DOUBLEVAL, new ValueImpl(Double.parseDouble(buffer)));
        }
        return new LexicalUnit(LexicalType.INTVAL, new ValueImpl(Integer.parseInt(buffer)));
    }

    private LexicalUnit getLit() {
        String buffer = "";
        char c = getChar();
        while (true) {
            c = getChar();
            if (c != '"') {
                buffer += c;
            } else {
                break;
            }
        }
        return new LexicalUnit(LexicalType.LITERAL, new ValueImpl(buffer));
    }

}
/*
 getの中には値が保持できない
 mapクラスで予約語か判定
 */
