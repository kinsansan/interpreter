package newlang8;

public interface LexicalAnalyzer {
    public LexicalUnit get();
    public boolean expect(LexicalType type);
    public void unget(LexicalUnit token);    
}
