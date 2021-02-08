package com.test.aop;

import com.test.services.AuditService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alexander Zubkov
 */
@Aspect
@Component
public class AuditAspect {

    private final AuditService auditService;
    private final CharSequence delimiter = ", ";

    public AuditAspect(AuditService auditService) {
        this.auditService = auditService;
    }

    private String getUsername() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) return "";
        return attributes.getRequest().getHeader("Username");
    }

    private String getAction(JoinPoint joinPoint) {
        return ((MethodSignature) joinPoint.getSignature())
                .getMethod()
                .getAnnotation(Audit.class)
                .value();
    }

    private String getParams(JoinPoint joinPoint) {
        CodeSignature methodSignature = (CodeSignature) joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        List<String> result = new LinkedList<>();
        for (int i = 0; i < parameterNames.length; i++) {
            result.add(parameterNames[i] + " = " + args[i].toString());
        }
        return result.stream().collect(Collectors.joining(delimiter));
    }

    @Pointcut("@annotation(Audit)")
    public void auditCall() { }

    @SuppressWarnings("unchecked")
    @Around("auditCall()")
    public Object aroundAudit(ProceedingJoinPoint joinPoint) throws Throwable {
        String result = null;
        Object retVal;
        try {
            retVal = joinPoint.proceed();
            if (retVal instanceof List) {
                result = "Success: [" + ((List<AuditData>) retVal).stream()
                        .map(AuditData::get)
                        .collect(Collectors.joining(delimiter)) + "]";
            } else {
                result = "Success: " + retVal;
            }
        } catch (Exception e) {
            result = "Fail: " + e.getMessage();
            throw e;
        } finally {
            auditService.sendMessage(
                    getUsername(),
                    getAction(joinPoint),
                    getParams(joinPoint),
                    result
            );
        }
        return retVal;
    }

}
