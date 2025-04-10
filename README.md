## Описание

Программа предназначена для создания списка покупок, с использованием баз данных PostgreSQL и H2. Реализована с использованием DAO.

### Основные функции программы:

1. Хранение и обновление информации о товарах.

2. Сортировка товаров по категориям, добавление нового товара, его изменение и удаление, а так же использование фильтра по категориям.

### Системные требования: разделение основной логики программы и управления доступом к данным, что позволит использовать в качестве репозитория для хранения данных - коллекцию, файл и БД.

## Основные классы проекта

* PostgreSQLDAO, наследованный из интерфейса ShoppingListDAO: отвечает за работу с базой данных PostgreSQL.
* H2DAO, наследованный из интерфейса ShoppingListDAO: отвечает за работу с базой данных Н2.
* DatabaseManager: отвечает за управление операций с базами данных.

## Установка и запуск

### Клонирование репозитория:
<https://github.com/Maryssika/Pokypki.git>

## Тестирование программы:

[тестирование.docx](..%2F..%2FDesktop%2F%D0%A3%D1%87%D0%B5%D0%B1%D0%B0%2F%D1%82%D0%B5%D1%81%D1%82%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D0%B5.docx)
