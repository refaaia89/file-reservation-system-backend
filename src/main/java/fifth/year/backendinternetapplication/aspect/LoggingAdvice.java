package fifth.year.backendinternetapplication.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fifth.year.backendinternetapplication.model.Log;
import fifth.year.backendinternetapplication.model.User;
import fifth.year.backendinternetapplication.model.enums.HttpActionLog;
import fifth.year.backendinternetapplication.repository.LogRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Objects;

@Aspect
@Component
public class LoggingAdvice {

    Logger log= LoggerFactory.getLogger(LoggingAdvice.class);

    private final LogRepository logRepository;

    public LoggingAdvice(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Pointcut(value = "execution(* fifth.year.backendinternetapplication.controller.*.*(..))")
    public void myPointcut(){

    }

    @Around("myPointcut()")
    public Object applicationLogger(ProceedingJoinPoint pjp) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        long user_id = 0;
        if(!authentication.getPrincipal().equals("anonymousUser")) {
            user_id = ((User)authentication.getPrincipal()).getId();
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String methodName = pjp.getSignature().getName();
        String className = pjp.getTarget().getClass().getName();
        Object[] args = pjp.getArgs();
        String a="";
        try {
            a=mapper.writeValueAsString(args);
        }catch(Exception e) {
            a = Arrays.toString(args);
        }
        String request="method invoked " + className + "." + methodName + "() args: " + a;
        log.info(request);
        logRepository.save(new Log(HttpActionLog.REQUEST,request,user_id == 0? null: user_id));
        Object object;
        try {
            if (Arrays.stream(args).findAny().isEmpty()) {
                object = pjp.proceed();
            } else {
                object = pjp.proceed(args);
            }
            if(user_id == 0) {
                if(!authentication.getPrincipal().equals("anonymousUser")) {
                    user_id = ((User)authentication.getPrincipal()).getId();
                }
            }
            a="";
            try {
                a = mapper.writeValueAsString(object);
            }catch(Exception e) {
                if (object instanceof ResponseEntity<?>)
                    a = "Download File: " + ((Resource) Objects.requireNonNull(((ResponseEntity<?>) object).getBody())).getFilename();
            }
            String response = className + "." + methodName + "() Response: " + a;
            log.info(response);
            logRepository.save(new Log(HttpActionLog.RESPONSE,response,user_id == 0? null: user_id));
        } catch (Throwable e) {
            if(user_id == 0) {
                if(!authentication.getPrincipal().equals("anonymousUser")) {
                    user_id = ((User)authentication.getPrincipal()).getId();
                }
            }
            String exception = className + "." + methodName + "() Exception: " + mapper.writeValueAsString(e.getMessage());
            log.info(exception);
            logRepository.save(new Log(HttpActionLog.EXCEPTION,exception,user_id == 0? null: user_id));
            throw e;
        }
        return object;
    }
}