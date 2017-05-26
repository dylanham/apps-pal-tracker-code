package test.pivotal.pal.continuum.support;

import com.jayway.jsonpath.DocumentContext;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class JsonPathAssert extends AbstractAssert<JsonPathAssert, DocumentContext> {

    public JsonPathAssert(DocumentContext json) {
        super(json, JsonPathAssert.class);
    }

    public static JsonPathAssert assertThat(DocumentContext json) {
        return new JsonPathAssert(json);
    }

    public JsonPathAssert hasString(String path, String value) {
        Assertions.assertThat(actual.read(path, String.class)).isEqualTo(value);
        return this;
    }

    public JsonPathAssert hasString(String path) {
        Assertions.assertThat(actual.read(path, Object.class)).isInstanceOf(String.class);
        return this;
    }

    public JsonPathAssert hasInt(String path, int value) {
        Assertions.assertThat(actual.read(path, Integer.class)).isEqualTo(value);
        return this;
    }

    public JsonPathAssert hasInt(String path) {
        Assertions.assertThat(actual.read(path, Object.class)).isInstanceOf(Integer.class);
        return this;
    }
}
