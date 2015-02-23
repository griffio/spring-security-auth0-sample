package griffio.auth0.spring.mvc;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CurrentUserResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter methodParameter) {
    return methodParameter.getParameterAnnotation(CurrentUser.class) != null
        && methodParameter.getParameterType().equals(UserAccount.class);
  }

  @Override
  public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer,
                                NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    if (this.supportsParameter(methodParameter)) {
      User user = (User) webRequest.getUserPrincipal();
      return UserAccount.create(user.getUsername(), user.getUsername() + "@example.com");
    } else {
      return WebArgumentResolver.UNRESOLVED;
    }
  }
}
