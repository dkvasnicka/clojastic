package net.danielkvasnicka.elasticsearch.scriptingengine;

import clojure.lang.Var;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Scorer;
import org.elasticsearch.script.SearchScript;
import org.elasticsearch.search.lookup.SearchLookup;

import java.util.Map;

/**
 * User: dkvasnicka
 * Date: 11/22/12
 * Time: 10:52
 */
public class ClojureSearchScript extends ExecutableClojureScript implements SearchScript {

    private SearchLookup lookup;

    public ClojureSearchScript(Var compiledScript, SearchLookup lookup, Map<String, Object> vars) {
        super(compiledScript, vars);
        this.lookup = lookup;
        this.vars.put("_fields", this.lookup.fields());
        this.vars.put("_source", this.lookup.source());

        // are those 3 fields enough? to be sure, let's put all lookup has to offer
        this.vars.putAll(lookup.asMap());
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
        this.vars.put("_score", score);
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
