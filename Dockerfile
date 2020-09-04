FROM openjdk:8
EXPOSE 3000
ADD target/gal-stock.jar gal-stock.jar
ENTRYPOINT ["java","-jar","/gal-stock.jar"]