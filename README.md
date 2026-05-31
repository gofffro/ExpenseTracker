# ExpenseTracker

Приложение для отслеживания расходов с использованием современного стека технологий Android разработки.

## Описание
ExpenseTracker позволяет пользователям записывать свои расходы, классифицировать их, просматривать аналитику в виде графиков и получать советы по экономии от искусственного интеллекта (GigaChat). Приложение поддерживает авторизацию через Яндекс или в гостевом режиме.

## Технологический стек и критерии выполнения
В приложении реализованы следующие обязательные компоненты:

1. Архитектура (Clean Architecture + MVVM)
- Слой Domain: Содержит модели данных и бизнес-логику (Use Cases). Находится в модуле :domain.
- Слой Data: Реализация репозиториев, работа с сетью и базой данных. Находится в модуле :data.
- Слой Presentation: UI на Jetpack Compose и ViewModels. Находится в модулях :app и :feature:*.

2. Внедрение зависимостей (Hilt)
- Настройка приложения: ExpenseTrackerApplication.kt (аннотация @HiltAndroidApp).
- Модули DI: Папка app/src/main/java/com/example/expensetracker/di/.
- Инъекции в Activity: MainActivity.kt (@AndroidEntryPoint).

3. Локальная база данных (Room)
- Описание сущностей и DAO: Модуль :data.
- База данных: ExpenseDatabase.kt.
- DAO: ExpenseDao.kt.

4. Сетевое взаимодействие (Retrofit + OkHttp)
- Интерфейс API: GigaChatApi.kt в модуле :data.
- Конфигурация клиента с поддержкой SSL-сертификатов: DataModule.kt.

5. Аналитика (AppMetrica)
- Инициализация: ExpenseTrackerApplication.kt.
- Сервис аналитики: AppMetricaAnalyticsService.kt.
- Отправка событий: Используется в ViewModels для отслеживания действий пользователя.

6. Удаленная конфигурация (Firebase Remote Config)
- Сервис: FirebaseRemoteConfigService.kt.
- Использование: MainActivity.kt и AboutScreen.kt для изменения параметров приложения без обновления.

7. Отчеты об ошибках (Firebase Crashlytics)
- Реализация: FirebaseCrashReporter.kt.
- Сбор логов: CompositeCrashReporter.kt объединяет логирование в консоль и в облако.

8. Push-уведомления (Firebase Cloud Messaging)
- Сервис обработки: PushMessagingService.kt.
- Регистрация токена: Выполняется при входе в систему в LoginViewModel.kt.

9. Фоновые задачи (WorkManager)
- Реализация воркера: ExpenseReminderWorker.kt.
- Планирование: Выполняется в ExpenseTrackerApplication.kt (ежедневные напоминания).

10. Безопасное хранение данных (EncryptedSharedPreferences)
- Реализация: AuthServiceImpl.kt.
- Использование: Хранение токенов авторизации и данных профиля в зашифрованном виде.

## Скриншоты
(Место для скриншотов)
- Экран авторизации
  <img width="365" height="576" alt="image" src="https://github.com/user-attachments/assets/ee08de8c-adbc-4ace-9bc6-f303303deef2" />

- Список расходов с графиком
  <img width="379" height="775" alt="image" src="https://github.com/user-attachments/assets/a76e0d29-9e3a-4234-8924-0e91ea545f77" />

- Добавление расхода
- <img width="379" height="776" alt="image" src="https://github.com/user-attachments/assets/55e3c80a-104e-41ae-be15-7565c5e440e0" />

- Детали расхода
  <img width="379" height="410" alt="image" src="https://github.com/user-attachments/assets/770a3542-cc41-48bf-98bc-be79dcc4af49" />

  <img width="379" height="775" alt="image" src="https://github.com/user-attachments/assets/961ea788-9ac4-41d0-b8ce-bba179732e5f" />

