### ClientRegistration
> `OAuth2UserRequest` -> `OAuthUser` by `OAuth2UserService`
<details>
  <summary>Code</summary>
  <p>

```java
@FunctionalInterface
public interface OAuth2UserService<R extends OAuth2UserRequest, U extends OAuth2User> {
    U loadUser(R userRequest) throws OAuth2AuthenticationException;
}

public class DefaultOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{
    
}

public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        OAuth2User oAuth2User = super.loadUser(userRequest);
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        
        // TODO: process OAuth2 User
        
    }
}
```
  </p>
</details>

<details>
  <summary>Example</summary>
  <p>

```properties
registrationId='google',
clientId='8437735853-a6elj40tmmdqs43k82i1n3hvmcd5fg6d.apps.googleusercontent.com',
clientSecret='xxxxxxxxxxxxxxxxx',
clientAuthenticationMethod=org.springframework.security.oauth2.core.ClientAuthenticationMethod@4fcef9d3,
authorizationGrantType=org.springframework.security.oauth2.core.AuthorizationGrantType@5da5e9f3,
redirectUri='{baseUrl}/{action}/oauth2/code/{registrationId}',
scopes=[email, profile],
providerDetails=org.springframework.security.oauth2.client.registration.ClientRegistration$ProviderDetails@1306ed02,
clientName='Google'
```
  </p>
</details>

---

### OAuthUser
<details>
  <summary>Code</summary>
  <p>

```java
public interface OAuth2User extends OAuth2AuthenticatedPrincipal {
    
}

public interface OAuth2AuthenticatedPrincipal extends AuthenticatedPrincipal {
	@Nullable
	@SuppressWarnings("unchecked")
	default <A> A getAttribute(String name) {
		return (A) getAttributes().get(name);
	}
	
	Map<String, Object> getAttributes();
    
	Collection<? extends GrantedAuthority> getAuthorities();
}
```
  </p>
</details>

<details>
  <summary>Example</summary>
  <p>

```yaml
Name: 110500195620669062596
Granted Authorities: 
  - ROLE_USER
  - SCOPE_https://www.googleapis.com/auth/userinfo.email
  - SCOPE_https://www.googleapis.com/auth/userinfo.profile
  - SCOPE_openid

User Attributes: 
  sub: 110500195620669062596
  name: 김신영
  given_name: 신영
  family_name: 김
  picture: https://lh3.googleusercontent.com/a-/AOh14Gh0lM7KQWxPOWdhbLpEeZsEINNXQ2-mfHhgA98UWQ=s96-c
  email: rolroralra@gmail.com
  email_verified: true
  locale: ko
```
  </p>
</details>

---
### Google ClientRegistration
<details>
  <summary>Example</summary>
  <p>

```properties
registrationId=google
clientId=8437735853-a6elj40tmmdqs43k82i1n3hvmcd5fg6d.apps.googleusercontent.com
clientSecret=rV3wdM8wI-4W4uyx56ym_L6c
clientAuthenticationMethod=org.springframework.security.oauth2.core.ClientAuthenticationMethod@4fcef9d3
authorizationGrantType=org.springframework.security.oauth2.core.AuthorizationGrantType@5da5e9f3
redirectUri={baseUrl}/{action}/oauth2/code/{registrationId}
scopes=[email, profile]
providerDetails=org.springframework.security.oauth2.client.registration.ClientRegistration$ProviderDetails@1306ed02
clientName='Google'
```
  </p>
</details>
