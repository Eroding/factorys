thymeleaf的用法
先在pom.xml引入
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>

    然后再资源文件配置中设置普通的
    #thymeleaf模版前缀
    spring.thymeleaf.prefix=classpath:/templates/
    spring.thymeleaf.suffix=.html
    spring.thymeleaf.mode=HTML5
    spring.thymeleaf.encoding=utf-8
    spring.thymeleaf.servlet.contype-type=text/html
    #下面这句话是关闭缓存。即时刷新，但是在上线是要改为true的。我直接改为ture了
    spring.thymeleaf.cache=true

