package net.danielkvasnicka.elasticsearch.scriptingengine;

import clojure.lang.Var;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Scorer;
import org.elasticsearch.script.SearchScript;
import org.elasticsearch.search.lookup.SearchLookup;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: daniel
 * Date: 11/22/12
 * Time: 10:52
 * To change this template use File | Settings | File Templates.
 */
public class ClojureSearchScript extends ExecutableClojureScript implements SearchScript {

    private Map<String, Object> vars;

    private Var compiledScript;

    private SearchLookup lookup;

    public ClojureSearchScript(Var compiledScript, SearchLookup lookup, Map<String, Object> vars) {
        super(compiledScript, vars);
        this.lookup = lookup;

        if (this.vars == null) {
            this.vars = new HashMap<String, Object>();
        }

        for (Map.Entry<String, Object> entry : lookup.asMap().entrySet()) {
            this.vars.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void setScorer(Scorer scorer) {
        this.lookup.setScorer(scorer);
    }

    @Override
    public void setNextReader(IndexReader reader) {
        this.lookup.setNextReader(reader);
    }

    @Override
    public void setNextDocId(int doc) {
        this.lookup.setNextDocId(doc);
    }

    @Override
    public void setNextSource(Map<String, Object> source) {
        this.lookup.source().setNextSource(source);
    }

    @Override
    public void setNextScore(float score) {
        vars.put("_score", score);
    }

    @Override
    public float runAsFloat() {
        return ((Number) run()).floatValue();
    }

    @Override
    public long runAsLong() {
        return ((Number) run()).longValue();
    }

    @Override
    public double runAsDouble() {
        return ((Number) run()).doubleValue();
    }
}
