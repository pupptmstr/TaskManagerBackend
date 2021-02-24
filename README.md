# Курсовая работа по дисциплине "Операционные системы"

### Оглавление
* [Цели работы](#Aims)
* [Описание](#Description)
* [Пояснение](#Explanation)
* [Endpoint's](#Endpoint)
* [Пример работы](#Examples)
* [Вывод](#Conclusion)

### <a name="Aims"></a>	Цели работы
* Применение полученных в ходе выполнения курса лабораторных работ практических навыков при создании проекта

* Создание приложения “Task manager”

### <a name="Description"></a>	Описание
Был создан бэкэнд приложения Task manager, которое частично заменит мне и моим домашним ежедневник. 
Когда будет написан фронтенд. Если он будет написан.

За основу взята простейшая идея ежедневника, руками был написан приемник и обработка http запросов,
где на каждый новый запрос создается новый поток. Так же была реализована внешняя синхронизация потоков для таск менеджера,
что позволит потокам не сражаться за ресурсы, поможет избежать дедлоков и данные всегда будут в актуальном состоянии.

В будущем, возможно, будет добавлена система авторизации, или любой другой способ идентификации пользователей.

Однако, из-за ограничений raspberry pi 3 (на которой запускается данное приложение) 
установленно ограничение в 2 максимум одновременно работающих потока.


### <a name="Explanation"></a> Пояснение (Ход работы)

Все, что непосредственно отвечает за работу приложения лежит в пакете 
[server](https://github.com/pupptmstr/TaskManagerBackend/tree/master/src/main/kotlin/com/pupptmstr/taskmanager/server).

Запуск приложения начинается с main() метода ([тут](https://github.com/pupptmstr/TaskManagerBackend/blob/master/src/main/kotlin/com/pupptmstr/taskmanager/server/Main.kt)),
где определяется порт на котором запустится приложение, а так же запуск самого сервера.

Далее в классе [Server](https://github.com/pupptmstr/TaskManagerBackend/blob/master/src/main/kotlin/com/pupptmstr/taskmanager/server/Server.kt)
происходит создание тредпула и таск менеджера, в котором будут выполняться запросы и храниться задачи.
Метод handle() вызывается каждый раз при запросе, в котором происходит запуск 
нового потока в тред пуле для выполнения определенного запроса. 

За обработку запроса отвечает класс 
[ClientHandler](https://github.com/pupptmstr/TaskManagerBackend/blob/master/src/main/kotlin/com/pupptmstr/taskmanager/server/ClientHandler.kt).
Там происходит разбор метода и пути запроса, присланных данных и непосредственно ответ.

Дополнительные модели, которые помогут в процессе работы лежат [тут](https://github.com/pupptmstr/TaskManagerBackend/tree/master/src/main/kotlin/com/pupptmstr/taskmanager/models)

### <a name="Endpoint"></a> Endpoint's
 
- GET (тип запроса)
    * /get, /get/all - получение всего списка задач
    * /get/#id - получение задачи с id = #id
- POST
    * /update, /change - один и тот же по факту запрос на изменение задачи (пример json'ов запросов смотрите ниже)
    * /create - запрос на создание новой задачи
    * /delete, /remove - запрос на удаление таска
    
#### Примеры JSON для запросов:


- Создание

      {
        "id" : 0, //любой id, будет установлен все равно id по порядку создания
        "status" : "started",
        "start-time" : "", // формат времени фиксированный
        "deadline" : "",
        "description" : "первая задача"
      }

- Обновление

      {
        "id" : "0",
        "status" : "in progress",
        "start-time" : "2021-02-21",
        "deadline" : "2021-02-23",
        "description" : "new task"
      }

- Удаление (по факту может быть пустой таск, главное, чтобы был указан нужный id)
    * То же самое, что и в предыдущем пункте, только с помаркой указанной выше.


#### Примеры JSON для запросов:


- /get/all
      
      [
        {
          "id": 0,
          "status": "started",
          "start-time": {
            "year": 2021,
            "month": 2,
            "day": 21
          },
          "deadline": {
            "year": 2021,
            "month": 2,
            "day": 23
          },
          "description": "new task"
        },
        {
          "id": 1,
          "status": "started",
          "start-time": {
            "year": 2021,
            "month": 2,
            "day": 21
          },
          "deadline": {
            "year": 2021,
            "month": 2,
            "day": 23
          },
          "description": "one more task"
        }
      ]

### <a name="Examples"></a> Пример работы программы

Ниже приведены скриншоты работы программы через клиент Postman.
Можем наблюдать следующий порядок действий:
- Создание нескольких тасков
- Получение через гет их списка
- Удаление второго таска
- Убедились, что удалился
- Создание нового второго таска
- Убедились, что все в порядке
  

![Иллюстрация 1](https://github.com/pupptmstr/TaskManagerBackend/blob/master/images/1.png)

![Иллюстрация 2](https://github.com/pupptmstr/TaskManagerBackend/blob/master/images/2.png)

![Иллюстрация 3](https://github.com/pupptmstr/TaskManagerBackend/blob/master/images/3.png)

![Иллюстрация 4](https://github.com/pupptmstr/TaskManagerBackend/blob/master/images/4.png)

![Иллюстрация 5](https://github.com/pupptmstr/TaskManagerBackend/blob/master/images/5.png)

![Иллюстрация 6](https://github.com/pupptmstr/TaskManagerBackend/blob/master/images/6.png)



### <a name="Conclusion"></a> Вывод

В ходе выполнения курсового проекта по операционным системам было 
создано приложение Task Manager, в котором удалось поработать с многопоточностью +
пришло понимание того, как изнутри работают различные бэкэнд библиотеки и фреймворки.


