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
- Список расходов с графиком
- Добавление расхода
- Детали расхода

## Путь к APK
Скомпилированный файл находится по пути:
app/build/outputs/apk/free/debug/app-free-debug.apk
