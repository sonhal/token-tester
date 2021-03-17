# Token Tester

## Description
Application intended to test OpenID Connect and OAuth 2.0 proxies.

## Usage
Typical usage is to run the application using docker, configured with environment
variables.
### Configuration

- ISSUER_NAME: (Default) `http://localhost:8888/default`
- OIDC_DISCOVERY_URL: (Default) `http://localhost:8888/default/.well-known/openid-configuration`
- OIDC_ACCEPTED_AUDIENCE: (Default) `aud-localhost`
- COOKIE_NAME: (Default) `localhost-idtoken`
## Endpoints
Returns HTML

-  (Get) `/safe` Secured endpoint that requires valid tokens (returns 401 Unauthorized)
-  (Get)`/` open endpoint with no validation
