Для работы требуется запущенный локально экземплять ActiveMQ
Для ActiveMQ должен быть в настройках jetty.xml включено приложение fileserver

1. Перейти в ru.vm.labs.mq/apache-activemq-5.12/bin/

запустить ./activemq start

2. Запустить mvn test из корня ru.vm.labs.mq

 Тест №1 создает простое текстовое сообщение
 Тест №2 создает сообщение с файловым вложение (при этом файлики при помещении в activemq сохраняются тут ru.vm.labs.mq/apache-activemq-5.12/webapps/fileserver/)
 Test №3 считывает одно случайное сообщение
 Тест №4 выполняет браузеринг сообщений в тестовой очереди, но не считывает их

4. Перейти по ссылке http://localhost:8161/admin. Открыть test_queue. Увидеть сообщения


