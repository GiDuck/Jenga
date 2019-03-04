package hi.im.jenga.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface AuthUser {

    public enum AuthUserType{ DEFAULT, DECODED }

    AuthUserType value() default AuthUserType.DEFAULT;

}
