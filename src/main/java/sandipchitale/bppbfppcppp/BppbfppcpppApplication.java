package sandipchitale.bppbfppcppp;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBean;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class BppbfppcpppApplication {

	@Component
	public static class BPP implements BeanPostProcessor, ApplicationContextAware {
		private  ApplicationContext applicationContext;

		public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
			this.applicationContext = applicationContext;
		}

		@Override
		public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
			ConfigurationPropertiesBean configurationPropertiesBean = ConfigurationPropertiesBean.get(this.applicationContext, bean, beanName);
			if (configurationPropertiesBean != null) {
				// This is a ConfigurationProperties bean
//				System.out.println(String.format("Configuration Properties : %150.150s Beans Class: %s", beanName, bean.getClass().getName()));
			} else {
				System.out.println(String.format("Bean                     : %150.150s Beans Class: %s", beanName, bean.getClass().getName()));
			}
			return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
		}
	}

	@Component
	public static class BFPP implements BeanFactoryPostProcessor {
		@Override
		public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
			System.out.println(String.format("Bean Factory             : %150.150s", beanFactory.getClass().getName()));

		}
	}

	@Component
	public static class CPPP extends ConfigurationPropertiesBindingPostProcessor implements ApplicationContextAware {
		private  ApplicationContext applicationContext;

		@Override
		public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
			super.setApplicationContext(applicationContext);
			this.applicationContext = applicationContext;
		}

		@Override
		public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
			if (ConfigurationPropertiesBean.get(this.applicationContext, bean, beanName) != null) {
				// This is a ConfigurationProperties bean
				System.out.println(String.format("Configuration Properties : %150.150s Beans Class: %s", beanName, bean.getClass().getName()));
			}
			return super.postProcessAfterInitialization(bean, beanName);
		}
	}

	@RestController
	public static class IndexController {
		@GetMapping("/")
		public String index() {
			return "Hello World!";
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(BppbfppcpppApplication.class, args);
	}

}
