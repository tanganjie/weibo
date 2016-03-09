package cn.edu.bistu.common.nlp.util;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by tanjie on 10/16/15.
 */
public class Segment {
    protected IKSegmenter seg;

    public Segment() {
        seg = new IKSegmenter(null, true);
    }

    public String seg(String text) throws IOException {
        StringReader reader = new StringReader(text);
        seg.reset(reader);
        Lexeme lex = null;
        StringBuilder sb = new StringBuilder();
        while((lex = seg.next()) != null) {
            sb.append(lex.getLexemeText() + "|");
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }
}
