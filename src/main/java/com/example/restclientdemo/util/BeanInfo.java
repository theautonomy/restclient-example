package com.example.restclientdemo.util;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Utility to list all beans configured by Spring.
 *
 * <p>This component runs at startup and prints all beans registered in the Spring application
 * context.
 */
@Component
public class BeanInfo implements CommandLineRunner {

    private final ApplicationContext applicationContext;

    public BeanInfo(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("Spring Beans (filtered by package/type)");
        System.out.println("=".repeat(70) + "\n");

        String[] beanNames = applicationContext.getBeanDefinitionNames();
        Arrays.sort(beanNames);

        // Print application beans (com.example.*)
        System.out.println("--- Application Beans (com.example.*) ---\n");
        int appCount = 0;
        for (String beanName : beanNames) {
            Object bean = applicationContext.getBean(beanName);
            String className = bean.getClass().getName();
            if (className.startsWith("com.example")) {
                System.out.printf("  %-40s -> %s%n", beanName, className);
                appCount++;
            }
        }
        System.out.println("\n  Total: " + appCount + " beans\n");

        // Print Jackson-related beans
        System.out.println("--- Jackson Beans ---\n");
        int jacksonCount = 0;
        for (String beanName : beanNames) {
            if (beanName.toLowerCase().contains("jackson")
                    || beanName.toLowerCase().contains("objectmapper")
                    || beanName.toLowerCase().contains("jsonmapper")) {
                Object bean = applicationContext.getBean(beanName);
                System.out.printf("  %-40s -> %s%n", beanName, bean.getClass().getName());
                jacksonCount++;
            }
        }
        System.out.println("\n  Total: " + jacksonCount + " beans\n");

        // Print RestClient-related beans
        System.out.println("--- RestClient Beans ---\n");
        int restClientCount = 0;
        for (String beanName : beanNames) {
            if (beanName.toLowerCase().contains("restclient")
                    || beanName.toLowerCase().contains("httpclient")) {
                Object bean = applicationContext.getBean(beanName);
                System.out.printf("  %-40s -> %s%n", beanName, bean.getClass().getName());
                restClientCount++;
            }
        }
        System.out.println("\n  Total: " + restClientCount + " beans\n");

        // Summary
        System.out.println("--- Summary ---\n");
        System.out.println("  Total beans in context: " + beanNames.length);

        System.out.println("\n" + "=".repeat(70) + "\n");
    }
}
