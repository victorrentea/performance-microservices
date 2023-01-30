package victor.training.performance;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.*;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;

@Component
@Aspect
public class GDPRFilter {

  @Autowired
  private RestTemplate rest;

  @Retention(RetentionPolicy.RUNTIME)
  @interface VisibleFor{
    String value();
  }

  @Around("@within(org.springframework.web.bind.annotation.RestController))") // method of @Facade classes
  public Object clearNonVisibleFields(ProceedingJoinPoint pjp) throws Throwable {
    String currentUser = "uu"; //pretend
    String jurisdiction = new RestTemplate().getForObject("http://localhost:8084/jurisdiction/" + currentUser, String.class);
    Object result = pjp.proceed();
    if (result == null) {
      return result;
    }
    if (!result.getClass().getPackageName().startsWith("victor")) {
      return result;
    }
    for (Field field : result.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      VisibleFor annot = field.getAnnotation(VisibleFor.class);
      if (annot != null) {
        if (!annot.value().equals(jurisdiction)) {
          field.set(result, null);
        }
      }
    }
    System.out.println("Filtered columns OK");
    return result;
  }
}
