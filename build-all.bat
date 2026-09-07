@echo off
chcp 65001 > nul
echo ========================================
echo 🔨 СБОРКА ВСЕХ МИКРОСЕРВИСОВ
echo ========================================
echo.

set SERVICES=api-gateway-service auth-service booking-service catalog-service history-service notification-service review-service

for %%s in (%SERVICES%) do (
    echo.
    echo 📦 Собираем: %%s
    echo ----------------------------------------

    if not exist "%%s" (
        echo ❌ Папка %%s не найдена! Пропускаем...
        goto :next_service
    )

    cd %%s

    if not exist "gradlew.bat" (
        echo ❌ gradlew.bat не найден в %%s! Пропускаем...
        cd ..
        goto :next_service
    )

    echo 🔨 Выполняем: gradlew.bat clean build -x test
    call gradlew.bat clean build -x test

    if %errorlevel% neq 0 (
        echo ❌ ОШИБКА при сборке %%s!
        echo Код ошибки: %errorlevel%
        pause
        exit /b 1
    )

    echo ✅ %%s собран успешно!
    cd ..

    :next_service
)

echo.
echo ========================================
echo ✅ ВСЕ СЕРВИСЫ УСПЕШНО СОБРАНЫ!
echo ========================================
pause
