package integrationtests.oauth;

public class OauthServerInfo {
    public final String clientId;
    public final String clientSecret;
    public final String tokenInfoUri;

    public OauthServerInfo(String clientId, String clientSecret, String tokenInfoUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.tokenInfoUri = tokenInfoUri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OauthServerInfo that = (OauthServerInfo) o;

        if (clientId != null ? !clientId.equals(that.clientId) : that.clientId != null) return false;
        if (clientSecret != null ? !clientSecret.equals(that.clientSecret) : that.clientSecret != null) return false;
        return tokenInfoUri != null ? tokenInfoUri.equals(that.tokenInfoUri) : that.tokenInfoUri == null;
    }

    @Override
    public int hashCode() {
        int result = clientId != null ? clientId.hashCode() : 0;
        result = 31 * result + (clientSecret != null ? clientSecret.hashCode() : 0);
        result = 31 * result + (tokenInfoUri != null ? tokenInfoUri.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OauthServerInfo{" +
            "clientId='" + clientId + '\'' +
            ", clientSecret='" + clientSecret + '\'' +
            ", tokenInfoUri='" + tokenInfoUri + '\'' +
            '}';
    }
}
